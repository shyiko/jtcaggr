package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.crawler.CrawlerException;
import com.appspot.jtcaggr.crawler.HTMLCrawler;
import com.appspot.jtcaggr.crawler.Tag;
import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.Competition;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Thread-safety: Thread safe provided that input parameters are thread safe.
 *
 * @author shyiko
 * @since Aug 5, 2010
 */
public class ActiveContestsParserImpl implements ActiveContestsParser {

    private static final Logger logger = Logger.getLogger(ActiveContestsParserImpl.class);
    private static Pattern tagPattern = Pattern.compile("(<).*?(>)", Pattern.DOTALL);
    private final static ThreadLocal<SimpleDateFormat> shortDateFormat =
            new ThreadLocal<SimpleDateFormat>() {
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("MM.dd.yyyy");
                }
            };

    public List<ActiveContest> parse(URL url) throws ParsingException {
        logger.debug("Starting to parse " + url);
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            HTMLCrawler crawler = new HTMLCrawler(inputStream);
            List<ActiveContest> result = new LinkedList<ActiveContest>();
            crawler.findNext("table", "class", "stat");
            // skip first two tr tags
            crawler.findNext("tr");
            crawler.findNext("tr");
            Tag trTag;
            while ((trTag = crawler.findNext("tr")) != null) {
                String cls = trTag.getAttributeValue("class");
                if (cls == null || (!cls.equals("light") && !cls.equals("dark")))
                    break;
                try {
                    Competition competition = parseType(crawler);
                    Catalog catalog = parseCatalog(crawler);
                    crawler.findNext("td");
                    Tag linkTag = crawler.findNext("a");
                    String name = parseName(crawler.getText());
                    String id = parseLink(linkTag.getAttributeValue("href"));
                    // skip two td tags
                    crawler.findNext("td");
                    crawler.findNext("td");
                    // further parsing
                    crawler.findNext("td");
                    Date registerBy = extractDate(crawler.getText());
                    crawler.findNext("td");
                    Date submitBy = extractDate(crawler.getText());
                    crawler.findNext("td");
                    int payment = parseMoney(crawler.getText());
                    crawler.findNext("td");
                    int reliabilityBonus = parseMoney(crawler.getText());
                    crawler.findNext("td");
                    int digitalRunPoint = Integer.parseInt(crawler.getText());
                    crawler.findNext("td");
                    String registrantsHtml = crawler.getText();
                    String registrants = tagPattern.matcher(registrantsHtml).replaceAll("").trim();
                    int delimIndex = registrants.indexOf("/");
                    if (delimIndex < 0)
                        throw new ParsingException("Registrants column doesn't contain '\\' character. It's value = \"" + registrants + "\"");
                    int ratedRegistrantsCount = Integer.parseInt(registrants.substring(0, delimIndex).trim());
                    int unratedRegistrantsCount = Integer.parseInt(registrants.substring(delimIndex + 1).trim());
                    crawler.findNext("td");
                    int submissions = Integer.parseInt(crawler.getText());
                    result.add(new ActiveContest(competition, catalog, name, id, registerBy, submitBy, payment, reliabilityBonus,
                            digitalRunPoint, ratedRegistrantsCount, unratedRegistrantsCount, submissions));
                } catch (ParsingException ex) {
                    logger.warn(ex.getMessage());
                }
            }
            return result;
        } catch (IOException e) {
            throw new ParsingException(e);
        } catch (CrawlerException e) {
            throw new ParsingException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Exception", e);
                }
            }
        }
    }

    private Competition parseType(HTMLCrawler crawler) throws CrawlerException, ParsingException {
        crawler.findNext("td");
        String type = crawler.getText();
        Competition result;
        if (type.toLowerCase().contains("development"))
            result = Competition.DEVELOPMENT;
        else
            throw new ParsingException("Couldn't parse type \"" + type + "\"");
        return result;
    }

    private Catalog parseCatalog(HTMLCrawler crawler) throws CrawlerException, ParsingException {
        crawler.findNext("td");
        String catalogTag = crawler.getText();
        if (catalogTag.contains("Java Custom"))
            return Catalog.JAVA;
        throw new ParsingException("Couldn't parse catalog \"" + catalogTag + "\"");
    }

    private String parseName(String name) {
        String[] parts = name.split("\n");
        if (parts.length == 0)
            return name;
        String result = parts[0].trim();
        if (parts.length > 2) {
            result = result + " (" + parts[2].trim() + ")";
        }
        return result;
    }

    private String parseLink(String link) throws ParsingException {
        int index = link.lastIndexOf("=");
        if (index < 0)
            throw new ParsingException("Couldn't parse link \"" + link + "\"");
        return link.substring(index + 1);
    }

    private int parseMoney(String money) {
        money = money.replace("$", "");
        int dotIndex = money.indexOf(".");
        if (dotIndex > -1)
            money = money.substring(0, dotIndex);
        return Integer.parseInt(money.trim());
    }

    private Date extractDate(String html) throws ParsingException {
        String text = tagPattern.matcher(html).replaceAll(" ").trim();
        int index = text.indexOf(" ");
        if (index < 0)
            throw new ParsingException("Failed to extract date from string \"" + html + "\"");
        Date result;
        try {
            result = shortDateFormat.get().parse(text.substring(0, index));
        } catch (ParseException e) {
            throw new ParsingException(e);
        }
        return result;
    }
}

package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.crawler.Crawler;
import com.appspot.jtcaggr.crawler.CrawlerException;
import com.appspot.jtcaggr.crawler.HTMLCrawler;
import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread-safety: Thread safe provided that input parameters are thread safe.
 *
 * @author shyiko
 * @since Aug 14, 2010
 */
public class ContestPageParserImpl implements ContestPageParser {

    private static final Logger logger = LoggerFactory.getLogger(ContestPageParserImpl.class);

    @Override
    public UpcomingContest parse(URL url) throws ParsingException {
        logger.debug("Starting to parse " + url);
        try {
            InputStream inputStream = url.openStream();
            try {
                Crawler crawler = new HTMLCrawler(inputStream);
                // find category
                Catalog catalog = getCatalog(crawler);
                // find registerBy
                Date registerBy = getRegisterBy(crawler);
                // find submitBy
                Date submitBy = getSubmitBy(crawler);
                // find payment
                Integer payment = getPayment(crawler);

                return new UpcomingContest(null, catalog, null, null, registerBy, submitBy, payment);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new ParsingException(e);
        } catch (CrawlerException e) {
            throw new ParsingException(e);
        }
    }

    private Catalog getCatalog(Crawler crawler) throws CrawlerException, ParsingException {
        do {
            if (crawler.findNext("p", "class", "bodySubtitle") == null)
                break;
            crawler.findNext("strong");
        } while (!crawler.getText().equals("Technologies"));
        crawler.findNext("ul");
        String technologies = crawler.getText();
        if (technologies == null
                || !(technologies.toLowerCase().contains("java")
                || technologies.toLowerCase().contains("j2ee")))
            throw new ParsingException("Contest doesn't use Java");
        return Catalog.JAVA;
    }

    private Date getRegisterBy(Crawler crawler) throws CrawlerException {
        do {
            crawler.findNext("td", "class", "bodyText");
        } while (!crawler.getText().equals("Posting Date:"));
        crawler.findNext("td");
        Date registerBy;
        try {
            registerBy = parseDate(crawler.getText());
        } catch (ParseException e) {
            throw new CrawlerException(e);
        }
        return registerBy;
    }

    private Date getSubmitBy(Crawler crawler) throws CrawlerException {
        do {
            crawler.findNext("td", "class", "bodyText");
        } while (!crawler.getText().equals("Initial Submission Due Date:"));
        crawler.findNext("td");
        Date registerBy;
        try {
            registerBy = parseDate(crawler.getText());
        } catch (ParseException e) {
            throw new CrawlerException(e);
        }
        return registerBy;
    }

    private Integer getPayment(Crawler crawler) throws CrawlerException {
        do {
            crawler.findNext("td", "class", "bodyText");
        } while (!crawler.getText().equals("Total Payment -"));
        crawler.findNext("td");
        return parseMoney(crawler.getText());
    }

    private Date parseDate(String date) throws ParseException {
        String dateToFormat = date.substring(0, date.indexOf(" "));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
        return dateFormat.parse(dateToFormat);
    }

    private int parseMoney(String money) {
        money = money.replace("$", "");
        int dotIndex = money.indexOf(".");
        if (dotIndex > -1)
            money = money.substring(0, dotIndex);
        return Integer.parseInt(money.trim());
    }

}

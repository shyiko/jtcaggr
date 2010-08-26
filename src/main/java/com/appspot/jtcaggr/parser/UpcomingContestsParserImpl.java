package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.crawler.CrawlerException;
import com.appspot.jtcaggr.crawler.XMLCrawler;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Thread-safety: Thread safe provided that input parameters are thread safe.
 *
 * @author shyiko
 * @since Aug 14, 2010
 */
public class UpcomingContestsParserImpl implements UpcomingContestsParser {

    private static final Logger logger = LoggerFactory.getLogger(UpcomingContestsParserImpl.class);
    public static final String COMPONENT_DEVELOPMENT_PREFIX = "component development - ";

    @Override
    public List<UpcomingContest> parse(URL url) throws ParsingException {
        logger.debug("Starting to parse " + url);
        XMLCrawler crawler = null;
        try {
            InputStream inputStream = url.openStream();
            try {
                crawler = new XMLCrawler(new BufferedInputStream(inputStream));
                List<UpcomingContest> result = new LinkedList<UpcomingContest>();
                while (crawler.findNext("item") != null) {
                    crawler.findNext("title");
                    String name = crawler.getText();
                    Competition competition;
                    if (name.toLowerCase().startsWith(COMPONENT_DEVELOPMENT_PREFIX))
                        competition = Competition.DEVELOPMENT;
                    else
                        continue;
                    name = name.substring(COMPONENT_DEVELOPMENT_PREFIX.length());
                    crawler.findNext("link");
                    String link = crawler.getText();
                    String id;
                    try {
                        id = parseLink(link);
                    } catch (ParsingException e) {
                        logger.warn(e.getMessage());
                        continue;
                    }
                    result.add(new UpcomingContest(competition, null, name, id, null, null, null));
                }
                return result;
            } finally {
                inputStream.close();
            }
        } catch (CrawlerException e) {
            throw new ParsingException(e);
        } catch (IOException e) {
            throw new ParsingException(e);
        } finally {
            if (crawler != null)
                crawler.close();
        }
    }

    private String parseLink(String link) throws ParsingException {
        int index = link.lastIndexOf("=");
        if (index < 0)
            throw new ParsingException("Couldn't parse link \"" + link + "\"");
        return link.substring(index + 1);
    }

}

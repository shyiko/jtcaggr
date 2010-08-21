package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.crawler.CrawlerException;
import com.appspot.jtcaggr.crawler.XMLCrawler;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 14, 2010
 */
public class UpcomingContestsParserImpl implements MultipleContestsParser {

    private static final Logger logger = LoggerFactory.getLogger(UpcomingContestsParserImpl.class);
    public static final String COMPONENT_DEVELOPMENT_PREFIX = "component development - ";

    private SingleContestParser parser;

    protected UpcomingContestsParserImpl() {
    }

    @Inject
    public UpcomingContestsParserImpl(SingleContestParser parser) {
        this.parser = parser;
    }

    @Override
    public List<Contest> parse(URL url) throws ParsingException {
        logger.debug("Starting to parse " + url);
        XMLCrawler crawler = null;
        try {
            InputStream inputStream = url.openStream();
            try {
                crawler = new XMLCrawler(new BufferedInputStream(inputStream));
                List<Contest> result = new LinkedList<Contest>();
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
                    UpcomingContest partialContest;
                    String id;
                    try {
                        partialContest = (UpcomingContest) parser.parse(new URL(link));
                        id = parseLink(link);
                    } catch (ParsingException e) {
                        logger.warn(e.getMessage());
                        continue;
                    }
                    result.add(new UpcomingContest(competition, partialContest.getCatalog(), name, id, partialContest.getRegisterBy(),
                            partialContest.getSubmitBy(), partialContest.getPayment()));
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

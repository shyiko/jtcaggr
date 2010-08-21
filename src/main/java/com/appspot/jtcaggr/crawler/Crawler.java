package com.appspot.jtcaggr.crawler;

import java.util.Map;

/**
 * @author shyiko
 * @since Aug 14, 2010
 */
public interface Crawler {

    Tag findNext(String tagName) throws CrawlerException;
    Tag findNext(String tagName, String tagAttributeName, String tagAttributeValue) throws CrawlerException;
    Tag findNext(String tagName, Map<String, String> tagAttributes) throws CrawlerException;
    String getText() throws CrawlerException;
}

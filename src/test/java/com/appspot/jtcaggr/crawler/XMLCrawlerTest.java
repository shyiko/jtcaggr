package com.appspot.jtcaggr.crawler;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
public class XMLCrawlerTest {

    @Test
    public void testFindNext() throws Exception {
        XMLCrawler crawler;
        FileInputStream stream = new FileInputStream("src/test/resources/upcoming-contests-rss.xml");
        BufferedInputStream inputStream = new BufferedInputStream(stream);
        try {
            crawler = new XMLCrawler(inputStream);
            try {
                crawler.findNext("title");
                Assert.assertNotNull(crawler.findNext("title"));
                Assert.assertEquals(crawler.getText(), "UI Prototype Competition - Hestia B2B \"Template and Search\" Merge 24hr Prototype 1.0");
                Assert.assertNotNull(crawler.findNext("link"));
                Assert.assertEquals(crawler.getText(), "http://www.topcoder.com/tc?module=ProjectDetail&pj=30013885");
                Assert.assertNotNull(crawler.findNext("title"));
                Assert.assertEquals(crawler.getText(), "UI Prototype Competition - Hestia B2B Prototype (Cart) 1.0");
                Assert.assertNotNull(crawler.findNext("link"));
                Assert.assertEquals(crawler.getText(), "http://www.topcoder.com/tc?module=ProjectDetail&pj=30013935");
            } finally {
                crawler.close();
            }
        } finally {
            inputStream.close();
        }
    }
}

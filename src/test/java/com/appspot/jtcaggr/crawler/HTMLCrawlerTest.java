package com.appspot.jtcaggr.crawler;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author shyiko
 * @since Aug 6, 2010
 */
public class HTMLCrawlerTest {
    private static final String ACTIVE_CONTESTS_HTML_FILE = "src/test/resources/active-contests-page.html";

    @Test
    public void testNestedTags() throws Exception {
        Crawler crawler;
        InputStream inputStream = IOUtils.toInputStream(
                "<TD class=\"valueC\" nowrap=\"nowrap\">\n" +
                "<STRONG>08.20.2010</STRONG><BR>15:02 EDT\n" +
                "</TD>");
        try {
            crawler = new HTMLCrawler(inputStream);
            Tag firstTag = crawler.findNext("td");
            Assert.assertNotNull(firstTag);
            Assert.assertEquals(crawler.getText(), "<STRONG>08.20.2010</STRONG><BR>15:02 EDT");
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testCaseInsensity() throws Exception {
        Crawler crawler;
        InputStream inputStream = IOUtils.toInputStream("a <A hRef=\"hello\"> text </a> hello<a>text2</  A>");
        try {
            crawler = new HTMLCrawler(inputStream);
            Tag firstTag = crawler.findNext("a");
            Assert.assertNotNull(firstTag);
            Assert.assertEquals(firstTag.getAttributesCount(), 1);
            Assert.assertEquals(firstTag.getAttributeValue("href"), "hello");
            Assert.assertEquals(crawler.getText(), "text");
            Tag secondTag = crawler.findNext("a");
            Assert.assertEquals(secondTag.getAttributesCount(), 0);
            Assert.assertEquals(crawler.getText(), "text2");
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testFindNext() throws Exception {
        HTMLCrawler crawler;
        FileInputStream inputStream = new FileInputStream(ACTIVE_CONTESTS_HTML_FILE);
        try {
            crawler = new HTMLCrawler(inputStream);
            Tag tableTag = crawler.findNext("table");
            Assert.assertNotNull(tableTag);
            Assert.assertEquals(tableTag.getName(), "table");
            Assert.assertEquals(tableTag.getAttributeValue("width"), "100%");
            Assert.assertEquals(tableTag.getAttributeValue("border"), "0");
            Assert.assertEquals(tableTag.getAttributeValue("cellpadding"), "0");
            Assert.assertEquals(tableTag.getAttributeValue("cellspacing"), "0");
            Assert.assertNull(tableTag.getAttributeValue("class"));
            Assert.assertEquals(tableTag.getAttributesCount(), 4);
            Tag nextTableTag = crawler.findNext("table");
            Assert.assertNotNull(nextTableTag);
            Assert.assertEquals(nextTableTag.getName(), "table");
            Assert.assertEquals(nextTableTag.getAttributeValue("class"), "pageTitleTable");
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testFindNextWithoutAnyAttributes() throws Exception {
        HTMLCrawler crawler;
        FileInputStream inputStream = new FileInputStream(ACTIVE_CONTESTS_HTML_FILE);
        try {
            crawler = new HTMLCrawler(inputStream);
            crawler.findNext("table", new HashMap<String, String>() {{
                put("class", "stat");
            }});
            Tag tag = crawler.findNext("tr");
            Assert.assertEquals(tag.getAttributesCount(), 0);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testFindNextWithAttributes() throws Exception {
        HTMLCrawler crawler;
        FileInputStream inputStream = new FileInputStream(ACTIVE_CONTESTS_HTML_FILE);
        try {
            crawler = new HTMLCrawler(inputStream);
            HashMap<String, String> map = new HashMap<String, String>() {{
                put("class", "light");
            }};
            Tag trTag = crawler.findNext("tr", map);
            Assert.assertNotNull(trTag);
            Assert.assertEquals(trTag.getName(), "tr");
            Assert.assertEquals(trTag.getAttributeValue("class"), "light");
            Assert.assertEquals(trTag.getAttributesCount(), 1);
            Tag nextTrTag = crawler.findNext("tr", map);
            Assert.assertNull(nextTrTag);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testGetText() throws Exception {
        HTMLCrawler crawler;
        FileInputStream inputStream = new FileInputStream(ACTIVE_CONTESTS_HTML_FILE);
        try {
            crawler = new HTMLCrawler(inputStream);
            crawler.findNext("tr", new HashMap<String, String>() {{
                put("class", "light");
            }});
            crawler.findNext("td");
            Assert.assertEquals(crawler.getText(), "Development");
        } finally {
            inputStream.close();
        }
    }

}

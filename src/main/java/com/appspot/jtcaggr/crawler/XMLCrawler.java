package com.appspot.jtcaggr.crawler;

import org.apache.commons.lang.StringEscapeUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shyiko
 * @since Aug 14, 2010
 */
public class XMLCrawler implements Crawler {

    private XMLStreamReader reader;

    public XMLCrawler(InputStream inputStream) throws CrawlerException{
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try {
            reader = inputFactory.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            throw new CrawlerException(e);
        }
    }

    @Override
    public Tag findNext(String tagName) throws CrawlerException {
        return findNext(tagName, Collections.<String, String>emptyMap());
    }

    @Override
    public Tag findNext(String tagName, String tagAttributeName, String tagAttributeValue) throws CrawlerException {
        Map<String, String> map = new HashMap<String, String>();
        map.put(tagAttributeName, tagAttributeValue);
        return findNext(tagName, map);
    }

    @Override
    public Tag findNext(String tagName, Map<String, String> tagAttributes) throws CrawlerException {
        root:
        while (moveToNext(tagName)) {
            Map<String, String> currentTagAttributes = getAttributes();
            if (!tagAttributes.isEmpty()) {
                if (currentTagAttributes.size() < tagAttributes.size())
                    continue;
                for (String attributeName : tagAttributes.keySet()) {
                    String actualTagAttributeValue = currentTagAttributes.get(attributeName);
                    String expectedTagAttributeValue = tagAttributes.get(attributeName);
                    if (!expectedTagAttributeValue.equals(actualTagAttributeValue))
                        continue root;
                }
            }
            return new Tag(tagName, currentTagAttributes);
        }
        return null;
    }

    private boolean moveToNext(String tagName) throws CrawlerException {
        try {
            while (reader.hasNext()) {
                reader.next();
                if (reader.isStartElement()) {
                    if (reader.getLocalName().equals(tagName))
                        return true;
                }
            }
        } catch (XMLStreamException e) {
            throw new CrawlerException(e);
        }
        return false;
    }

    private Map<String, String> getAttributes() {
        int count = reader.getAttributeCount();
        if (count == 0)
            return Collections.emptyMap();
        Map<String, String> result = new HashMap<String, String>(count);
        int index = -1;
        while (++index < count) {
            String attributeName = reader.getAttributeName(index).getLocalPart();
            String attributeValue = reader.getAttributeValue(index);
            result.put(attributeName, safeTrim(StringEscapeUtils.unescapeXml(attributeValue)));
        }
        return result;
    }

    @Override
    public String getText() throws CrawlerException {
        try {
            return safeTrim(StringEscapeUtils.unescapeXml(reader.getElementText()));
        } catch (XMLStreamException e) {
            throw new CrawlerException(e);
        }
    }

    public void close() {
        try {
            reader.close();
        } catch (XMLStreamException e) {
        }
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}

package com.appspot.jtcaggr.crawler;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
public class HTMLCrawler implements Crawler {

    private StringBuilder buffer;
    private int index;
    private String prevTag;

    public HTMLCrawler(InputStream inputStream) throws CrawlerException {
        char[] charArray;
        try {
            charArray = IOUtils.toCharArray(inputStream);
        } catch (IOException e) {
            throw new CrawlerException(e);
        }
        buffer = new StringBuilder();
        buffer.append(charArray);
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
        if (index == buffer.length())
            return null;
        prevTag = tagName;
        root:
        while (true) {
            int beginning = indexOfOpeningTag(tagName, index);
            if (beginning == -1) {
                index = buffer.length();
                return null;
            }
            int end = buffer.indexOf(">", beginning);
            if (end == -1) {
                index = buffer.length();
                return null;
            }
            index = end + 1;
            String fullTag = buffer.substring(beginning, end + 1).trim();
            Tag tag = parseTag(fullTag);
            for (String key : tagAttributes.keySet()) {
                String expectedValue = tagAttributes.get(key);
                String value = tag.getAttributeValue(key);
                if (!expectedValue.equalsIgnoreCase(value)) {
                    continue root;
                }
            }
            return tag;
        }
    }

    private int indexOfOpeningTag(String tag, int offset) {
        tag = "<" + tag;
        char[] lower = tag.toLowerCase().toCharArray();
        char[] upper = tag.toUpperCase().toCharArray();
        int index = offset, end = buffer.length(), limit = lower.length;
        for (int pos = 0; index < end; index++) {
            char c = buffer.charAt(index);
            if (pos == limit) {
                if (c != ' ' && c != '>')
                    pos = 0;
                else
                    break;
            }
            if (c != lower[pos] && c != upper[pos]) {
                pos = 0;
                if (c != lower[pos] && c != upper[pos])
                    pos = -1;
            }
            pos++;
        }
        return index == end ? -1 : index - limit;
    }

    @Override
    public String getText() throws CrawlerException {
        if (prevTag == null)
            throw new IllegalStateException("You should find tag before trying to extract its text");
        int end = indexOfClosingTag(prevTag, index);
        if (end == -1)
            return null;
        String result = buffer.substring(index, end);
        return result == null ? null : StringEscapeUtils.unescapeXml(result.trim());
    }

    /**
     * Search for the first occurrence of &lt;/[ ]?tag&gt;
     * @param tag name of tag to search
     * @param offset offset to begin from
     * @return index of the first occurrence of a closing tag for given name
     */
    private int indexOfClosingTag(String tag, int offset) {
        char[] lower = tag.toLowerCase().toCharArray();
        char[] upper = tag.toUpperCase().toCharArray();
        int index = offset, end = buffer.length(), beginning = -1;
        for (int pos = -1, limit = lower.length; index < end; index++) {
            char c = buffer.charAt(index);
            if (pos == limit) {
                if (c != '>')
                    pos = 0;
                else
                    break;
            }
            if (pos > -1) {

                if (pos == 0) {
                    if (c == ' ')
                        continue;
                }
                if (c != lower[pos] && c != upper[pos]) {
                    pos = 0;
                    if (c != lower[pos] && c != upper[pos])
                        pos = -2;
                    else
                        pos = -1;
                }
                pos++;
            } else {
                if (buffer.charAt(index - 1) == '<' && c == '/') {
                    beginning = index - 1;
                    pos = 0;
                }
            }
        }
        return index == end ? -1 : beginning;
    }

    /**
     * @param fullTag Tag in form '<tagName attributes>'
     * @return Tag
     */
    private Tag parseTag(String fullTag) {
        // remove &lt; and &gt; from tag
        String tag = fullTag.substring(1, fullTag.length() - 1).replace("\n", " ").trim();
        int index = tag.indexOf(" ");
        if (index < 0) {
            return new Tag(tag.toLowerCase(), Collections.<String, String>emptyMap());
        }
        String tagName = tag.substring(0, index);
        tag = tag.substring(index + 1);
        Map<String, String> map = parseTagAttributes(tag);
        return new Tag(tagName.toLowerCase(), map);
    }

    /**
     * @param attributes Attributes in form 'attrname="attvalue" attr2name=1'
     * @return Map, each key of which corresponds to attribute name and value to attribute value
     */
    private Map<String, String> parseTagAttributes(String attributes) {
        Map<String, String> result = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        String tagName = null;
        boolean needToUseQuote = false;
        byte[] bytes = attributes.getBytes();
        for (byte aByte : bytes) {
            char c = (char) aByte;
            if (tagName == null) {
                if (c == ' ')
                    continue;
                if (Character.isLetter(c))
                    sb.append(c);
                else {
                    tagName = sb.toString();
                    sb.delete(0, sb.length());
                }
            } else {
                if (c == '\"' || c == '\'') {
                    if (!needToUseQuote) {
                        needToUseQuote = true;
                    } else {
                        needToUseQuote = false;
                        result.put(tagName.toLowerCase(), StringEscapeUtils.unescapeXml(sb.toString()));
                        tagName = null;
                        sb.delete(0, sb.length());
                    }
                    continue;
                }
                if (!needToUseQuote && c == ' ') {
                    result.put(tagName.toLowerCase(), StringEscapeUtils.unescapeXml(sb.toString()));
                    continue;
                }
                sb.append(c);
            }
        }
        return result;
    }

}

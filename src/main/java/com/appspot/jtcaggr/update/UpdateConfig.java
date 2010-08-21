package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.parser.MultipleContestsParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author shyiko
 * @since Aug 14, 2010
 */
public class UpdateConfig {

    private Map<String, MultipleContestsParser> urlParserPair
            = new HashMap<String, MultipleContestsParser>();

    public void add(String url, MultipleContestsParser parser) {
        urlParserPair.put(url, parser);
    }

    public Set<String> getUrls() {
        return urlParserPair.keySet();
    }
    
    public MultipleContestsParser getParser(String url) {
        return urlParserPair.get(url);
    }
}

package com.appspot.jtcaggr.update;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author shyiko
 * @since Aug 14, 2010
 */
public class UpdateConfiguration {

    private Map<String, String> urlHandlerPair = new HashMap<String, String>();

    public void add(String url, String handler) {
        urlHandlerPair.put(url, handler);
    }

    public Set<String> getUrls() {
        return urlHandlerPair.keySet();
    }

    public String getHandler(String url) {
        return urlHandlerPair.get(url);
    }
}

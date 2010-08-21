package com.appspot.jtcaggr.crawler;

import java.util.Collections;
import java.util.Map;

/**
 * @author shyiko
 * @since Aug 6, 2010
 */
public class Tag {

    private String name;
    private Map<String, String> attributes;

    Tag(String name, Map<String, String> attributes) {
        this.name = name;
        if (attributes == null)
            this.attributes = Collections.unmodifiableMap(Collections.<String, String>emptyMap());
        else
            this.attributes = Collections.unmodifiableMap(attributes);
    }

    public String getName() {
        return name;
    }

    public String getAttributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    public int getAttributesCount() {
        return attributes.size();
    }
}

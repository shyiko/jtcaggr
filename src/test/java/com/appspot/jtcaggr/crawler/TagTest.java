package com.appspot.jtcaggr.crawler;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * @author shyiko
 * @since Aug 6, 2010
 */
public class TagTest {

    @Test
    public void testGetName() throws Exception {
        Tag tag = new Tag("name", null);
        Assert.assertEquals(tag.getName(), "name");
    }

    @Test
    public void testGetAttributeValue() throws Exception {
        Tag tag = new Tag("name", new HashMap<String, String>() {{
            put("k1", "v1");
            put("k2", "v2");
        }});
        Assert.assertEquals(tag.getAttributeValue("k1"), "v1");
        Assert.assertEquals(tag.getAttributeValue("k2"), "v2");
        Assert.assertNull(tag.getAttributeValue("k3"));
    }

    @Test
    public void testGetAttributesCount() throws Exception {
        Tag tag = new Tag("name", new HashMap<String, String>() {{
            put("k1", "v1");
            put("k2", "v2");
        }});
        Assert.assertEquals(tag.getAttributesCount(), 2);
    }
}

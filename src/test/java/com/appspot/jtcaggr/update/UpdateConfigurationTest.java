package com.appspot.jtcaggr.update;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
public class UpdateConfigurationTest {

    @Test
    public void testGetUrls() {
        UpdateConfiguration updateConfiguration = new UpdateConfiguration();
        Set<String> urls = updateConfiguration.getUrls();
        Assert.assertTrue(urls.isEmpty());
        String url = "url";
        updateConfiguration.add(url, null);
        urls = updateConfiguration.getUrls();
        Assert.assertEquals(urls.size(), 1);
        String urlFromCollection = urls.iterator().next();
        Assert.assertEquals(urlFromCollection, url);
    }
}

package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.parser.MultipleContestsParser;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
public class UpdateConfigTest {

    @Test
    public void testGetParser() {
        MultipleContestsParser parserMock = Mockito.mock(MultipleContestsParser.class);

        UpdateConfig updateConfig = new UpdateConfig();
        String url = "url";
        Assert.assertNull(updateConfig.getParser(url));
        updateConfig.add(url, parserMock);
        MultipleContestsParser parser = updateConfig.getParser(url);
        Assert.assertNotNull(parser);
        Assert.assertEquals(parser, parserMock);
    }

    public void testGetUrls() {
        UpdateConfig updateConfig = new UpdateConfig();
        Set<String> urls = updateConfig.getUrls();
        Assert.assertTrue(urls.isEmpty());
        String url = "url";
        updateConfig.add(url, null);
        urls = updateConfig.getUrls();
        Assert.assertEquals(urls.size(), 1);
        String urlFromCollection = urls.iterator().next();
        Assert.assertEquals(urlFromCollection, url);
    }
}

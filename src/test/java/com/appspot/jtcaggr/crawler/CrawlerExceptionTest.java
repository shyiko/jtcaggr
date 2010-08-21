package com.appspot.jtcaggr.crawler;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
public class CrawlerExceptionTest {

    @Test
    public void testConstructorWithThrowable() {
        RuntimeException cause = new RuntimeException();
        CrawlerException ex = new CrawlerException(cause);
        Assert.assertNotNull(ex.getCause());
        Assert.assertEquals(ex.getCause(), cause);
    }
}

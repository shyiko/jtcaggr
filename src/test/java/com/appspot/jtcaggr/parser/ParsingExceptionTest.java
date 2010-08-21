package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.crawler.CrawlerException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
public class ParsingExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "message";
        ParsingException ex = new ParsingException(message);
        Assert.assertNotNull(ex.getMessage());
        Assert.assertEquals(ex.getMessage(), message);
    }

    @Test
    public void testConstructorWithThrowable() {
        RuntimeException cause = new RuntimeException();
        ParsingException ex = new ParsingException(cause);
        Assert.assertNotNull(ex.getCause());
        Assert.assertEquals(ex.getCause(), cause);
    }
}

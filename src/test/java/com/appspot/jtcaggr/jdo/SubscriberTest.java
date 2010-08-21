package com.appspot.jtcaggr.jdo;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shyiko
 * @since Aug 13, 2010
 */
public class SubscriberTest {

    @Test
    public void testGetEmail() throws Exception {
        String email = "body@google.com";
        Subscriber subscriber = new Subscriber(email);
        Assert.assertEquals(subscriber.getEmail(), email);
    }
}

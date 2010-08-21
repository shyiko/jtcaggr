package com.appspot.jtcaggr.subscribing;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shyiko
 * @since Aug 18, 2010
 */
public class AlreadySubscribedExceptionTest {

    @Test
    public void accuracyTestForConstructorWithEmailAsAParameter() {
        String email = "body@gmail.com";
        AlreadySubscribedException ex = new AlreadySubscribedException(email);
        Assert.assertEquals(ex.getMessage(), "Email " + email + " is already subscribed to the announcement of new TopCoder contests.");
    }
}

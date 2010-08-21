package com.appspot.jtcaggr.subscribing;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shyiko
 * @since Aug 18, 2010
 */
public class NotSubscribedExceptionTest {

    @Test
    public void accuracyTestForConstructorWithEmailHashAsAParameter() {
        String emailHash = "body@gmail.com";
        NotSubscribedException ex = new NotSubscribedException(emailHash);
        Assert.assertEquals(ex.getMessage(), emailHash + " is not subscribed.");
    }
}

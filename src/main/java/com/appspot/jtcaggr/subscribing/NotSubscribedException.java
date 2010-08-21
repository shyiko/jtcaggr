package com.appspot.jtcaggr.subscribing;

/**
 * @author shyiko
 * @since Aug 17, 2010
 */
public class NotSubscribedException extends Exception {

    public NotSubscribedException(String emailHash) {
        super(emailHash + " is not subscribed.");
    }
}

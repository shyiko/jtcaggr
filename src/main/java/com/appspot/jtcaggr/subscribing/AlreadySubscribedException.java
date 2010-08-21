package com.appspot.jtcaggr.subscribing;

/**
 * @author shyiko
 * @since Aug 17, 2010
 */
public class AlreadySubscribedException extends Exception {

    public AlreadySubscribedException(String email) {
        super("Email " + email + " is already subscribed to the announcement of new TopCoder contests.");
    }
}

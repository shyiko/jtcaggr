package com.appspot.jtcaggr.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author shyiko
 * @since Aug 13, 2010
 */
@PersistenceCapable
public class Subscriber {

    @PrimaryKey
    private String email;

    public Subscriber(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}

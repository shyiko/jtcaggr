package com.appspot.jtcaggr.subscribing;

import com.appspot.jtcaggr.jdo.Subscriber;
import com.appspot.jtcaggr.jdo.dao.SubscriberDAO;
import com.google.inject.Inject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author shyiko
 * @since Aug 17, 2010
 */
public class SubscribeUtils {

    private static final String HASH_ALGORITHM = "MD5";
    private SubscriberDAO subscriberDAO;

    @Inject
    public SubscribeUtils(SubscriberDAO subscriberDAO) {
        this.subscriberDAO = subscriberDAO;
    }

    private Subscriber getSubscriberByEmailHash(String hash) {
        /* TODO:
           Surely there is no need to get whole collection. The better way may be:
           1. get cursor and traverse through collection in iterator-like way
           2. use recoverable encryption algorithm instead of one-way hash function
           3. whatever else
        */
        for (Subscriber subscriber : subscriberDAO.findAll()) {
            String email = subscriber.getEmail();
            if (getEmailHash(email).equals(hash))
                return subscriber;
        }
        return null;
    }

    public String getEmailHash(String email) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e);
        }
        byte messageDigest[] = md.digest(email.getBytes());
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }
        return hexString.toString();
    }

    public void subscribe(String email) throws AlreadySubscribedException {
        Subscriber subscriber = subscriberDAO.findByEmail(email);
        if (subscriber != null)
            throw new AlreadySubscribedException(email);
        subscriber = new Subscriber(email);
        subscriberDAO.persist(subscriber);
    }

    public void unsubscribe(String hash) throws NotSubscribedException {
        Subscriber subscriber = getSubscriberByEmailHash(hash);
        if (subscriber == null)
            throw new NotSubscribedException(hash);
        subscriberDAO.delete(subscriber);
    }
}

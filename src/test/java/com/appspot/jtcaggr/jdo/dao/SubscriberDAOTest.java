package com.appspot.jtcaggr.jdo.dao;

import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.JDOTest;
import com.appspot.jtcaggr.jdo.Subscriber;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jdo.PersistenceManager;
import java.util.Collection;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 13, 2010
 */
public class SubscriberDAOTest extends JDOTest {

    @Test
    public void testPersist() throws Exception {
        SubscriberDAO subscriberDAO = new SubscriberDAO();
        subscriberDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        Subscriber subscriber = new Subscriber("body@google.com");
        subscriberDAO.persist(subscriber);
        PersistenceManager pm = getNewPersistenceManager();
        try {
            Assert.assertEquals(((List) pm.newQuery(Subscriber.class).execute()).size(), 1);
        } finally {
            pm.close();
        }
    }

    @Test
    public void testFindByEmail() throws Exception {
        SubscriberDAO subscriberDAO = new SubscriberDAO();
        subscriberDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        String email = "body@google.com";
        Subscriber subscriber = new Subscriber(email);
        subscriberDAO.persist(subscriber);
        Subscriber foundSubscriber = subscriberDAO.findByEmail(email);
        Assert.assertNotNull(foundSubscriber);
    }

    @Test
    public void testFindAll() throws Exception {
        SubscriberDAO subscriberDAO = new SubscriberDAO();
        subscriberDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        subscriberDAO.persist(new Subscriber("body1@google.com"));
        subscriberDAO.persist(new Subscriber("body2@google.com"));
        Collection<Subscriber> subscribers = subscriberDAO.findAll();
        Assert.assertEquals(subscribers.size(), 2);
    }

    @Test
    public void testDelete() throws Exception {
        SubscriberDAO subscriberDAO = new SubscriberDAO();
        subscriberDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        String email = "body@google.com";
        Subscriber subscriber = new Subscriber(email);
        subscriberDAO.persist(subscriber);
        subscriberDAO.delete(subscriber);
        PersistenceManager pm = getNewPersistenceManager();
        try {
            Assert.assertEquals(((List) pm.newQuery(Subscriber.class).execute()).size(), 0);
        } finally {
            pm.close();
        }
    }
}

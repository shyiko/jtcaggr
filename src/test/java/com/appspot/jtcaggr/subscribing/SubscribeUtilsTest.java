package com.appspot.jtcaggr.subscribing;

import com.appspot.jtcaggr.jdo.JDOTest;
import com.appspot.jtcaggr.jdo.Subscriber;
import com.appspot.jtcaggr.jdo.dao.SubscriberDAO;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;

/**
 * @author shyiko
 * @since Aug 18, 2010
 */
public class SubscribeUtilsTest extends JDOTest {

    @Test
    public void testIfGetEmailHashAlwaysReturnsSameResultsForTheSameEmail() throws Exception {
        SubscribeUtils subscribeUtils = new SubscribeUtils(null);
        String email = "body@gmail.com";
        Assert.assertEquals(subscribeUtils.getEmailHash(email),
                          subscribeUtils.getEmailHash(email));
    }

    @Test
    public void testSubscribe() throws Exception {
        SubscriberDAO subscriberDAO = new SubscriberDAO();
        subscriberDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        String email = "body@gmail.com";
        SubscribeUtils subscribeUtils = new SubscribeUtils(subscriberDAO);
        subscribeUtils.subscribe(email);
        Collection<Subscriber> subscribers = subscriberDAO.findAll();
        Assert.assertEquals(subscribers.size(), 1);
    }

    @Test(expectedExceptions = AlreadySubscribedException.class)
    public void testSubscribeAlreadySubscribedEmail() throws Exception {
        String email = "body@gmail.com";
        SubscriberDAO subscriberDAOMock = Mockito.mock(SubscriberDAO.class);
        Mockito.when(subscriberDAOMock.findByEmail(Mockito.anyString())).thenReturn(new Subscriber(email));
        SubscribeUtils subscribeUtils = new SubscribeUtils(subscriberDAOMock);
        subscribeUtils.subscribe(email);
    }

    @Test
    public void testUnsubscribe() throws Exception {
        SubscriberDAO subscriberDAO = new SubscriberDAO();
        subscriberDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        String email = "body@gmail.com";
        subscriberDAO.persist(new Subscriber(email));
        SubscribeUtils subscribeUtils = new SubscribeUtils(subscriberDAO);
        String hash = subscribeUtils.getEmailHash(email);
        subscribeUtils.unsubscribe(hash);
        Collection<Subscriber> subscribers = subscriberDAO.findAll();
        Assert.assertTrue(subscribers.isEmpty());
    }

    @Test(expectedExceptions = NotSubscribedException.class)
    public void testUnsubscribeWrongUser() throws Exception {
        String email = "body@gmail.com";
        SubscriberDAO subscriberDAOMock = Mockito.mock(SubscriberDAO.class);
        Mockito.when(subscriberDAOMock.findAll()).thenReturn(Collections.<Subscriber>emptyList());
        SubscribeUtils subscribeUtils = new SubscribeUtils(subscriberDAOMock);
        String hash = subscribeUtils.getEmailHash(email);
        subscribeUtils.unsubscribe(hash);

    }
}

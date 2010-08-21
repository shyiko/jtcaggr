package com.appspot.jtcaggr.jdo;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.util.Date;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 13, 2010
 */
@SuppressWarnings({"unchecked"})
public class SubscriberJDOTest extends JDOTest {

    @Test
    public void testContestSave() {
        String email = "body@google.com";
        Subscriber subscriber = new Subscriber(email);
        PersistenceManager pm = getNewPersistenceManager();
        Transaction txn = pm.currentTransaction();
        txn.begin();
        try {
            pm.makePersistent(subscriber);
            txn.commit();
        } finally {
            if (txn.isActive())
                txn.rollback();
            pm.close();
        }
        pm = getNewPersistenceManager();
        try {
            List<Subscriber> result = (List<Subscriber>) pm.newQuery(Subscriber.class).execute();
            Assert.assertEquals(result.size(), 1);
            Subscriber s = result.get(0);
            Assert.assertEquals(s.getEmail(), email);
        } finally {
            pm.close();
        }
    }

    @Test
    public void testContestDelete() {
        Subscriber subscriber = new Subscriber("body@google.com");
        PersistenceManager pm = getNewPersistenceManager();
        Transaction txn = pm.currentTransaction();
        txn.begin();
        try {
            pm.makePersistent(subscriber);
            txn.commit();
        } finally {
            if (txn.isActive())
                txn.rollback();
            pm.close();
        }
        pm = getNewPersistenceManager();
        txn = pm.currentTransaction();
        txn.begin();
        try {
            Subscriber s = ((List<Subscriber>) pm.newQuery(Subscriber.class).execute()).get(0);
            pm.deletePersistent(s);
            txn.commit();
            Assert.assertEquals(((List) pm.newQuery(Subscriber.class).execute()).size(), 0);
        } finally {
            if (txn.isActive())
                txn.rollback();
            pm.close();
        }
    }
}

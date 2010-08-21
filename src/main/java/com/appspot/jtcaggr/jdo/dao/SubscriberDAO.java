package com.appspot.jtcaggr.jdo.dao;

import com.appspot.jtcaggr.jdo.Subscriber;
import com.appspot.jtcaggr.subscribing.AlreadySubscribedException;
import com.appspot.jtcaggr.subscribing.NotSubscribedException;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.Collection;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 13, 2010
 */
@SuppressWarnings("unchecked")
public class SubscriberDAO extends GenericDAO<Subscriber> {

    public Subscriber findByEmail(String email) {
        PersistenceManager pm = getNewPersistenceManager();
        try {
            Query query = pm.newQuery(Subscriber.class, "email == \"" + email + "\"");
            List result = (List) query.execute();
            Subscriber subscriber = null;
            if (result.size() > 0) {
                subscriber = (Subscriber) pm.detachCopy(result.iterator().next());
            }
            return subscriber;
        } finally {
            pm.close();
        }
    }

    public Collection<Subscriber> findAll() {
        PersistenceManager pm = getNewPersistenceManager();
        try {
            return pm.detachCopyAll((Collection<Subscriber>) pm.newQuery(Subscriber.class).execute());
        } finally {
            pm.close();
        }
    }

    public void delete(Subscriber subscriber) {
        PersistenceManager pm = getNewPersistenceManager();
        Transaction txn = pm.currentTransaction();
        try {
            txn.begin();
            Subscriber objectToDelete = pm.getObjectById(Subscriber.class, subscriber.getEmail());
            if (objectToDelete == null)
                return;
            pm.deletePersistent(objectToDelete);
            txn.commit();
        } finally {
            if (txn.isActive())
                txn.rollback();
            pm.close();
        }
    }
}

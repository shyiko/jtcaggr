package com.appspot.jtcaggr.jdo.dao;

import com.appspot.jtcaggr.jdo.*;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
@SuppressWarnings({"unchecked"})
public class ContestDAO extends GenericDAO<Contest> {

    public <T extends Contest> T find(Class<T> domainClass, String id) {
        PersistenceManager pm = getNewPersistenceManager();
        try {
            Contest contest = findContest(pm, domainClass, id);
            return contest == null ? null : (T) pm.detachCopy(contest);
        } finally {
            pm.close();
        }
    }

    public List<Contest> find(Class<? extends Contest> domainClass, int startPoint, int displayAmount, Competition competition) {
        PersistenceManager pm = getNewPersistenceManager();
        try {
            Query query = pm.newQuery(domainClass);
            query.setFilter("competition == " + competition);
            query.setOrdering("registerBy ascending");
            query.setRange(startPoint, startPoint + displayAmount);
            return (List<Contest>) pm.detachCopyAll((Collection) query.execute());
        } finally {
            pm.close();
        }
    }

    public int getRecordsTotalNumber(Class<? extends Contest> domainClass, Competition competition) {
        PersistenceManager pm = getNewPersistenceManager();
        try {
            Query query = pm.newQuery(domainClass);
            query.setFilter("competition == " + competition);
            query.setResult("count(this)");
            return (Integer) query.execute();
        } finally {
            pm.close();
        }
    }

    public <T extends Contest> List<T> findAll(Class<T> domainClass) {
        PersistenceManager pm = getNewPersistenceManager();
        try {
            Query query = pm.newQuery(domainClass);
            return (List<T>) pm.detachCopyAll((Collection) query.execute());
        } finally {
            pm.close();
        }
    }

    public void delete(Class<? extends Contest> domainClass, String id) {
        PersistenceManager pm = getNewPersistenceManager();
        Transaction txn = pm.currentTransaction();
        try {
            Contest objectToDelete = findContest(pm, domainClass, id);
            if (objectToDelete == null)
                return;
            txn.begin();
            pm.deletePersistent(objectToDelete);
            txn.commit();
        } finally {
            if (txn.isActive())
                txn.rollback();
            pm.close();
        }
    }

    private Contest findContest(PersistenceManager pm, Class<? extends Contest> domainClass, String id) {
        Query query = pm.newQuery(domainClass, "id == idToFind");
        query.declareParameters("String idToFind");
        List result = (List) query.execute(id);
        Iterator iterator = result.iterator();
        if (!iterator.hasNext())
            return null;
        return (Contest) iterator.next();
    }
}

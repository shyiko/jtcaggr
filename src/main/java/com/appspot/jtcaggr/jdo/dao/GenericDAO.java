package com.appspot.jtcaggr.jdo.dao;

import com.google.inject.Inject;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
public abstract class GenericDAO<T> {

    private PersistenceManagerFactory pmf;

    @Inject
    public void setPersistenceManagerFactory(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    protected PersistenceManager getNewPersistenceManager() {
        return pmf.getPersistenceManager();
    }

    /**
     * Persist object to database.
     *
     * @param obj Object to persist
     * @return Detached copy of newly persisted object
     */
    public T persist(T obj) {
        PersistenceManager pm = getNewPersistenceManager();
        Transaction txn = pm.currentTransaction();
        try {
            txn.begin();
            pm.makePersistent(obj);
            txn.commit();
            return pm.detachCopy(obj);
        } finally {
            if (txn.isActive())
                txn.rollback();
            pm.close();
        }
    }

}

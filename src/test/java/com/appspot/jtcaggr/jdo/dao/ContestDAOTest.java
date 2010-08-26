package com.appspot.jtcaggr.jdo.dao;

import com.appspot.jtcaggr.jdo.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 8, 2010
 */
public class ContestDAOTest extends JDOTest {

    @Test
    public void testPersist() throws Exception {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        Contest contest = contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        Assert.assertNotNull(contest);
        Assert.assertEquals(contest.getName(), "name");
        Contest contest2 = contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name2", "link2",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        Assert.assertNotNull(contest2);
        Assert.assertEquals(contest2.getName(), "name2");
        PersistenceManager pm = getNewPersistenceManager();
        try {
            Assert.assertEquals(((List) pm.newQuery(ActiveContest.class).execute()).size(), 2);
        } finally {
            pm.close();
        }
    }

    @Test
    public void testPersistAll() throws Exception {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        List<ActiveContest> contests = new LinkedList<ActiveContest>();
        contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name2", "link2",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        contestDAO.persistAll(contests);
        PersistenceManager pm = getNewPersistenceManager();
        try {
            List actual = (List) pm.newQuery(ActiveContest.class).execute();
            Assert.assertEquals(actual.size(), 2);
        } finally {
            pm.close();
        }
    }

    @Test
    public void testDeleteAll() throws Exception {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        List<ActiveContest> contests = new LinkedList<ActiveContest>();
        contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name2", "link2",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        Collection<ActiveContest> persistedContests = contestDAO.persistAll(contests);
        contestDAO.deleteAll(persistedContests);
        PersistenceManager pm = getNewPersistenceManager();
        try {
            List actual = (List) pm.newQuery(ActiveContest.class).execute();
            Assert.assertEquals(actual.size(), 0);
        } finally {
            pm.close();
        }
    }

    @Test
    public void testFindByLink() throws Exception {
        Contest contest = new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7);
        PersistenceManager pm = getNewPersistenceManager();
        Transaction txn = pm.currentTransaction();
        txn.begin();
        try {
            pm.makePersistent(contest);
            txn.commit();
        } finally {
            if (txn.isActive())
                txn.rollback();
            pm.close();
        }
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        Contest c = contestDAO.find(contest.getClass(), contest.getId());
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), "name");
    }

    @Test
    public void testFindWithPagination() throws Exception {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name2", "link2",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        List<Contest> contests = contestDAO.find(ActiveContest.class, 0, 1, Competition.DEVELOPMENT);
        Assert.assertEquals(contests.size(), 1);
        Assert.assertEquals(contests.get(0).getName(), "name");
        contests = contestDAO.find(ActiveContest.class, 1, 1, Competition.DEVELOPMENT);
        Assert.assertEquals(contests.size(), 1);
        Assert.assertEquals(contests.get(0).getName(), "name2");
    }

    @Test
    public void testGetRecordsTotalNumber() throws Exception {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "some", "link2",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        int totalContestsNumber = contestDAO.getRecordsTotalNumber(ActiveContest.class, Competition.DEVELOPMENT);
        Assert.assertEquals(totalContestsNumber, 2);
    }
}

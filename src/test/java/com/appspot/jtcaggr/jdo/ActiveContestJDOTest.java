package com.appspot.jtcaggr.jdo;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.util.Date;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 8, 2010
 */
@SuppressWarnings({"unchecked"})
public class ActiveContestJDOTest extends JDOTest {

    @Test
    public void testContestSave() {
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
        pm = getNewPersistenceManager();
        try {
            List<ActiveContest> result = (List<ActiveContest>) pm.newQuery(ActiveContest.class).execute();
            Assert.assertEquals(result.size(), 1);
            ActiveContest c = result.get(0);
            Assert.assertEquals(c.getCompetition(), Competition.DEVELOPMENT);
            Assert.assertEquals(c.getCatalog(), Catalog.JAVA);
            Assert.assertEquals(c.getName(), "name");
            Assert.assertEquals(c.getId(), "link");
            Assert.assertEquals(c.getRegisterBy(), new Date(0));
            Assert.assertEquals(c.getSubmitBy(), new Date(1));
            Assert.assertEquals(c.getPayment(), Integer.valueOf(2));
            Assert.assertEquals(c.getReliabilityBonus(), Integer.valueOf(3));
            Assert.assertEquals(c.getDigitalRunPoint(), Integer.valueOf(4));
            Assert.assertEquals(c.getRatedRegistrantsCount(), Integer.valueOf(5));
            Assert.assertEquals(c.getUnratedRegistrantsCount(), Integer.valueOf(6));
            Assert.assertEquals(c.getSubmissions(), Integer.valueOf(7));
        } finally {
            pm.close();
        }
    }

    @Test
    public void testContestDelete() {
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
        pm = getNewPersistenceManager();
        try {
            pm.currentTransaction().begin();
            Contest c = ((List<Contest>) pm.newQuery(ActiveContest.class).execute()).get(0);
            pm.deletePersistent(c);
            pm.currentTransaction().commit();
            Assert.assertEquals(((List) pm.newQuery(ActiveContest.class).execute()).size(), 0);
        } finally {
            pm.close();
        }
    }

}

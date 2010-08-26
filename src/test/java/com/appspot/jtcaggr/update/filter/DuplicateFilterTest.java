package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.*;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * @author shyiko
 * @since Aug 23, 2010
 */
public class DuplicateFilterTest extends JDOTest {

    private ContestDAO contestDAO;
    private DuplicateFilter<Contest> filter;

    @BeforeClass
    public void setUp() {
        contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());

        filter = new DuplicateFilter<Contest>();
        filter.setContestDAO(contestDAO);
    }

    @Test
    public void testIfPersistedActiveContestIsNotValid() {
        Contest contest = contestDAO.persist(createActiveContest("a"));
        Assert.assertFalse(filter.isValid(contest));
    }

    @Test
    public void testIfPersistedUpcomingContestIsNotValid() {
        Contest contest = contestDAO.persist(createUpcomingContest("a"));
        Assert.assertFalse(filter.isValid(contest));
    }

    @Test
    public void testIfNotPersistedActiveContestIsValid() {
        Contest contest = createActiveContest("a");
        Assert.assertTrue(filter.isValid(contest));
    }

    @Test
    public void testIfNotPersistedUpcomingContestIsValid() {
        Contest contest = createUpcomingContest("a");
        Assert.assertTrue(filter.isValid(contest));
    }

    @Test
    public void testIfNotPersistedUpcomingContestIsNotValidWhenSameActiveContestExists() {
        String link = "a";
        contestDAO.persist(createActiveContest(link));
        Contest contest = createUpcomingContest(link);
        Assert.assertFalse(filter.isValid(contest));
    }

    private ActiveContest createActiveContest(String link) {
        return new ActiveContest(Competition.DEVELOPMENT,
                Catalog.JAVA, "name", link, new Date(0), new Date(1), 2, 3, 4, 5, 6, 7);
    }

    private UpcomingContest createUpcomingContest(String link) {
        return new UpcomingContest(Competition.DEVELOPMENT,
                Catalog.JAVA, "name", link, new Date(0), new Date(1), 2);
    }
}

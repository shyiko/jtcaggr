package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.MailService;
import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.*;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.parser.MultipleContestsParser;
import com.appspot.jtcaggr.parser.ParsingException;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 8, 2010
 */
public class UpdaterTest extends JDOTest {

    @Test
    public void testUpdate() throws Exception {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.add("http://", new MultipleContestsParser() {
            @Override
            public List<Contest> parse(URL url) throws ParsingException {
                List<Contest> contests = new ArrayList<Contest>(1);
                contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                        new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
                return contests;
            }
        });
        Updater updater = new Updater(contestDAO, Mockito.mock(MailService.class), updateConfig);
        updater.doGet(null, null);
        PersistenceManager pm = getNewPersistenceManager();
        try {
            List result = (List) pm.newQuery(ActiveContest.class).execute();
            Assert.assertEquals(result.size(), 1);
        } finally {
            pm.close();
        }
    }

    @Test
    public void testUpdateWithDuplicates() throws Exception {
        final List<Contest> contests = new ArrayList<Contest>(1);
        contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name2", "link2",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));

        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.add("http://", new MultipleContestsParser() {
            @Override
            public List<Contest> parse(URL url) throws ParsingException {
                return contests;
            }
        });
        Updater updater = new Updater(contestDAO, Mockito.mock(MailService.class), updateConfig);
        updater.doGet(null, null);

        contests.clear();
        contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        updater.doGet(null, null);

        PersistenceManager pm = getNewPersistenceManager();
        try {
            List result = (List) pm.newQuery(ActiveContest.class).execute();
            Assert.assertEquals(result.size(), 2);
        } finally {
            pm.close();
        }
    }

    @Test
    public void testIfUpcomingContestGetsDeletedOnceActiveOneArrives() throws Exception {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        contestDAO.persist(new UpcomingContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2));
        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.add("http://", new MultipleContestsParser() {
            @Override
            public List<Contest> parse(URL url) throws ParsingException {
                List<Contest> contests = new ArrayList<Contest>(1);
                contests.add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                        new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
                return contests;
            }
        });
        Updater updater = new Updater(contestDAO, Mockito.mock(MailService.class), updateConfig);
        updater.doGet(null, null);
        PersistenceManager pm = getNewPersistenceManager();
        try {
            List result = (List) pm.newQuery(ActiveContest.class).execute();
            Assert.assertEquals(result.size(), 1);
            result = (List) pm.newQuery(UpcomingContest.class).execute();
            Assert.assertEquals(result.size(), 0);
        } finally {
            pm.close();
        }
    }
}

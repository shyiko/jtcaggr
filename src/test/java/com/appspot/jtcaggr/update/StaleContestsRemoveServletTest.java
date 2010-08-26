package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.guice.FilterForNewContestsProvider;
import com.appspot.jtcaggr.guice.FilterForPersistedContestsProvider;
import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.JDOTest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 26, 2010
 */
public class StaleContestsRemoveServletTest extends JDOTest {

    @Test
    public void testUpdate() throws Exception {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());

        contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "Delete me 1.0", "link",
                new Date(), new Date(), 2, 3, 4, 5, 6, 7));
        contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link2",
                new Date(), new Date(), 2, 3, 4, 5, 6, 7));

        FilterForPersistedContestsProvider filterProvider = new FilterForPersistedContestsProvider(contestDAO);

        StaleContestsRemoveServlet servlet = new StaleContestsRemoveServlet(contestDAO, filterProvider.get());
        servlet.service(null, null);

        List<ActiveContest> contests = contestDAO.findAll(ActiveContest.class);
        Assert.assertEquals(contests.size(), 1);
        Assert.assertEquals(contests.iterator().next().getName(), "name");
    }
}

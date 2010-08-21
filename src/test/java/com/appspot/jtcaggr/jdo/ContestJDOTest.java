package com.appspot.jtcaggr.jdo;

import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * @author shyiko
 * @since Aug 18, 2010
 */
public class ContestJDOTest extends JDOTest {

    @Test
    public void testIfActiveAndUpcomingContestsDoesNotConflictWithEachOther() {
        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());
        contestDAO.persist(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2, 3, 4, 5, 6, 7));
        contestDAO.persist(new UpcomingContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(0), new Date(1), 2));
    }

}

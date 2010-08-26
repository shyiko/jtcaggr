package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
@Singleton
public class StaleContestsRemoveServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUpcomingContestServlet.class);

    private ContestDAO contestDAO;

    @Inject
    public StaleContestsRemoveServlet(ContestDAO contestDAO) {
        this.contestDAO = contestDAO;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Stale contests remove service started.");
        removeUpcomingContestsBeingReplacedWithActive();
        removeContestsWithOldSubmitByDate();
    }

    private void removeUpcomingContestsBeingReplacedWithActive() {
        List<UpcomingContest> upcomingContests = contestDAO.findAll(UpcomingContest.class);
        for (UpcomingContest upcomingContest : upcomingContests) {
            String id = upcomingContest.getId();
            ActiveContest activeContest = contestDAO.find(ActiveContest.class, id);
            if (activeContest != null) {
                logger.info("Stale contest found (id=" + id + "). Removing...");
                contestDAO.delete(UpcomingContest.class, id);
            }
        }
    }

    private void removeContestsWithOldSubmitByDate() {
        List<UpcomingContest> deletedUpcomingContests = contestDAO.deleteObsolete(UpcomingContest.class);
        for (UpcomingContest contest : deletedUpcomingContests) {
            logger.info("Upcoming contest with old 'submit by' date found (id=" + contest.getId() + "). Removing...");
        }
        List<ActiveContest> deletedActiveContests = contestDAO.deleteObsolete(ActiveContest.class);
        for (ActiveContest contest : deletedActiveContests) {
            logger.info("Active contest with old 'submit by' date found (id=" + contest.getId() + "). Removing...");
        }
    }
}

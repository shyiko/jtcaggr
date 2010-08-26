package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.update.filter.Filter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
@Singleton
public class StaleContestsRemoveServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUpcomingContestServlet.class);

    private ContestDAO contestDAO;
    private Filter<Contest> filter;

    @SuppressWarnings("unchecked")
    @Inject
    public StaleContestsRemoveServlet(ContestDAO contestDAO, @Named("PersistedFilter") Filter filter) {
        this.contestDAO = contestDAO;
        this.filter = filter;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Stale contests remove service started.");
        List<Contest> contests = new LinkedList<Contest>();
        contests.addAll(contestDAO.findAll(UpcomingContest.class));
        contests.addAll(contestDAO.findAll(ActiveContest.class));
        List<Contest> staleContests = filter.filterInvalid(contests);
        if (staleContests.isEmpty()) {
            logger.info("No stale contests were found. Skipping...");
        }
        logger.info(String.format("Found %d stale contests", staleContests.size()));
        contestDAO.deleteAll(staleContests);
    }
}

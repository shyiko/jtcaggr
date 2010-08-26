package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.mail.MailQueue;
import com.appspot.jtcaggr.parser.ActiveContestsParser;
import com.appspot.jtcaggr.parser.ParsingException;
import com.appspot.jtcaggr.update.filter.Filter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
@Singleton
public class UpdateActiveContestsServlet extends ContestUpdateServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUpcomingContestServlet.class);

    private ActiveContestsParser parser;
    private Filter<ActiveContest> filter;
    private ContestDAO contestDAO;
    private MailQueue mailQueue;

    @SuppressWarnings("unchecked")
    @Inject
    public UpdateActiveContestsServlet(ActiveContestsParser parser, Filter filter, ContestDAO contestDAO, MailQueue mailQueue) {
        this.parser = parser;
        this.filter = filter;
        this.contestDAO = contestDAO;
        this.mailQueue = mailQueue;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Update active contests service started.");
        String link = req.getParameter(LINK);

        List<ActiveContest> contests;
        try {
            contests = parser.parse(new URL(link));
        } catch (ParsingException e) {
            logger.error("Couldn't parse page at " + link, e);
            return;
        }

        List<ActiveContest> newContests = filter.filterValid(contests);
        int newContestsNumber = newContests.size();
        if (newContestsNumber > 0) {
            logger.info("Found " + newContestsNumber + " new contest(s) at " + link);
            Collection<ActiveContest> persistedContests = contestDAO.persistAll(newContests);
            mailQueue.markAsNew(persistedContests);
        }
    }
}

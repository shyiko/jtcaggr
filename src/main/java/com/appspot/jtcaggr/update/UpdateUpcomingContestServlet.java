package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.mail.MailQueue;
import com.appspot.jtcaggr.parser.ContestPageParser;
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
import java.util.List;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
@Singleton
public class UpdateUpcomingContestServlet extends ContestUpdateServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUpcomingContestServlet.class);

    public static final String COMPETITION = "competition";
    public static final String NAME = "name";
    public static final String ID = "id";

    private ContestPageParser parser;
    private ContestDAO contestDAO;
    private Filter<UpcomingContest> filter;
    private MailQueue mailQueue;

    @Inject
    public UpdateUpcomingContestServlet(ContestPageParser parser, Filter filter, ContestDAO contestDAO, MailQueue mailQueue) {
        this.parser = parser;
        this.filter = filter;
        this.contestDAO = contestDAO;
        this.mailQueue = mailQueue;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String link = req.getParameter(LINK);
        Competition competition = Competition.valueOf(req.getParameter(COMPETITION));
        String name = req.getParameter(NAME);
        String id = req.getParameter(ID);
        Contest partialContest;
        try {
            partialContest = parser.parse(new URL(link));
        } catch (ParsingException e) {
            logger.warn("Couldn't parse contest at " + link + " : " + e.getMessage());
            return;
        }
        UpcomingContest contest = new UpcomingContest(competition, partialContest.getCatalog(), name, id, partialContest.getRegisterBy(),
                partialContest.getSubmitBy(), partialContest.getPayment());
        if (filter.isValid(contest)) {
            Contest persistedContest = contestDAO.persist(contest);
            mailQueue.markAsNew(persistedContest);
        }
    }
}

package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.parser.ParsingException;
import com.appspot.jtcaggr.parser.UpcomingContestsParser;
import com.appspot.jtcaggr.update.filter.Filter;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
@Singleton
public class UpdateUpcomingContestsServlet extends ContestUpdateServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUpcomingContestServlet.class);

    private UpcomingContestsParser parser;
    private String updateUpcomingContestTaskURL;
    private Queue updateQueue;

    @Inject
    public UpdateUpcomingContestsServlet(UpcomingContestsParser parser,
                                         @Named("UpdateUpcomingContestTaskURL") String updateUpcomingContestTaskURL,
                                         @Named("UpdateQueue") Queue updateQueue) {
        this.parser = parser;
        this.updateUpcomingContestTaskURL = updateUpcomingContestTaskURL;
        this.updateQueue = updateQueue;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Update upcoming contests service started.");
        String link = req.getParameter(LINK);
        List<UpcomingContest> contests;
        try {
            contests = parser.parse(new URL(link));
        } catch (ParsingException e) {
            logger.error("Couldn't parse page at " + link, e);
            return;
        }

        for (UpcomingContest contest : contests) {
            TaskOptions task = url(updateUpcomingContestTaskURL);
            task.param(UpdateUpcomingContestServlet.LINK, contest.getProjectURL());
            task.param(UpdateUpcomingContestServlet.COMPETITION, contest.getCompetition().name());
            task.param(UpdateUpcomingContestServlet.NAME, contest.getName());
            task.param(UpdateUpcomingContestServlet.ID, contest.getId());
            updateQueue.add(task);
        }
    }
}

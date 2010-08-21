package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.MailService;
import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.parser.MultipleContestsParser;
import com.appspot.jtcaggr.parser.ParsingException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
@Singleton
public class Updater extends HttpServlet {

    private static final Logger logger = Logger.getLogger(Updater.class);

    private ContestDAO contestDAO;
    private MailService mailService;
    private UpdateConfig updateConfig;

    @Inject
    public Updater(ContestDAO contestDAO, MailService mailService, UpdateConfig updateConfig) {
        this.contestDAO = contestDAO;
        this.mailService = mailService;
        this.updateConfig = updateConfig;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Starting update service");
        List<Contest> updates = new LinkedList<Contest>();
        for (String url : updateConfig.getUrls()) {
            MultipleContestsParser parser = updateConfig.getParser(url);
            List<Contest> contests;
            try {
                contests = parser.parse(new URL(url));
            } catch (ParsingException e) {
                logger.error(e.getMessage(), e);
                continue;
            }
            int contestsNumber = contests.size();
            if (contestsNumber == 0) {
                logger.info("No contests were found at \"" + url + "\"");
                continue;
            }
            logger.info("Found " + contests.size() + " contest(s) at " + url + "");
            List<Contest> newContests = findNewContests(contests);
            if (newContests.size() > 0) {
                logger.info(newContests.size() + " new contest(s) were added.");
                updates.addAll(newContests);
            }
            else
                logger.info("There were no new contests found.");
        }
        if (updates.size() > 0) {
            persist(updates);
            removeStaleContests();
            mailService.informSubscribesAboutNewContests(updates);
        }
    }

    private List<Contest> findNewContests(List<Contest> contests) {
        List<Contest> result = new LinkedList<Contest>();
        for (Contest contest : contests) {
            if (!isFake(contest) && !alreadyExists(contest)) {
                if (contest instanceof UpcomingContest && isActivated((UpcomingContest) contest)) {
                    continue;
                }
                result.add(contest);
            }
        }
        return result;
    }

    private boolean isFake(Contest contest) {
        return contest.getName().toLowerCase().contains("delete me");
    }

    private boolean alreadyExists(Contest contest) {
        return contestDAO.find(contest.getClass(), contest.getId()) != null;
    }

    private boolean isActivated(UpcomingContest contest) {
        return contestDAO.find(ActiveContest.class, contest.getId()) != null;
    }

    private void persist(List<Contest> contests) {
        for (Contest contest : contests) {
            contestDAO.persist(contest);
        }
    }

    private void removeStaleContests() {
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

}

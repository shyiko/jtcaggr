package com.appspot.jtcaggr.contests;

import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.google.inject.Singleton;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 4, 2010
 */
@Singleton
public class DefaultContestsProviderServletImpl extends ContestsProviderServlet {

    @Override
    public ContestsTO getContests(Class<? extends Contest> domainClass, Competition competition,
                                  int startPoint, int displayAmount, String searchString) {
        ContestDAO contestDAO = getContestDAO();
        int totalContestsNumber = contestDAO.getRecordsTotalNumber(domainClass, competition);
        List<Contest> list = contestDAO.find(domainClass, startPoint, displayAmount, competition);
        String[][] result = new String[list.size()][];
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
        for (Contest contest : list) {
            result[i] = new String[8];
            result[i][0] = "<a href=\"" + contest.getProjectURL() + "\">" + contest.getName() + "</a>";
            result[i][1] = dateFormat.format(contest.getRegisterBy());
            result[i][2] = dateFormat.format(contest.getSubmitBy());
            result[i][3] = "" + contest.getPayment();
            if (contest instanceof ActiveContest) {
                ActiveContest activeContest = (ActiveContest) contest;
                result[i][4] = "" + activeContest.getReliabilityBonus();
                result[i][5] = "" + activeContest.getDigitalRunPoint();
                result[i][6] = activeContest.getRatedRegistrantsCount() + " / " + activeContest.getUnratedRegistrantsCount();
                result[i][6] = "<a href=\"" + contest.getRegistrantsURL() + "\">" + result[i][6] + "</a>";
                result[i][7] = "" + activeContest.getSubmissions();
            } else {
                result[i][4] = result[i][5] = result[i][6] = result[i][7] = "";
            }
            i++;
        }
        return new ContestsTO(totalContestsNumber, result);
    }

}

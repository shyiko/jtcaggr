package com.appspot.jtcaggr.contests;

import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.google.inject.Inject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shyiko
 * @since Aug 7, 2010
 */
public abstract class ContestsProviderServlet extends HttpServlet {

    private ContestDAO contestDAO;

    @Inject
    public void setContestDAO(ContestDAO contestDAO) {
        this.contestDAO = contestDAO;
    }

    public ContestDAO getContestDAO() {
        return contestDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int startPoint = Integer.valueOf(request.getParameter("iDisplayStart"));
        int displayAmount = Integer.valueOf(request.getParameter("iDisplayLength"));
        Class<? extends Contest> domainClass = getDomainClass(request);
        Competition competition = Competition.DEVELOPMENT;
        String searchString = request.getParameter("sSearch");
        request.setAttribute(ContestsJSONFilter.CONTESTS, getContests(domainClass, competition,
                startPoint, displayAmount, searchString));
    }

    private Class<? extends Contest> getDomainClass(HttpServletRequest request) {
        String contestsType = request.getParameter("type");
        Class<? extends Contest> domainClass;
        if (contestsType.equals("active"))
            domainClass = ActiveContest.class;
        else
        if (contestsType.equals("upcoming"))
            domainClass = UpcomingContest.class;
        else
            throw new IllegalArgumentException("Request parameter \"type\" is not valid");
        return domainClass;
    }

    public abstract ContestsTO getContests(Class<? extends Contest> domainClass, Competition competition,
                                           int startPoint, int displayAmount, String searchString);

}

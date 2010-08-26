package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
public class DuplicateFilter<T extends Contest> extends AbstractFilter<T> {

    private ContestDAO contestDAO;

    public DuplicateFilter() {
    }

    public DuplicateFilter(Filter<T> nextFilter) {
        super(nextFilter);
    }

    public void setContestDAO(ContestDAO contestDAO) {
        this.contestDAO = contestDAO;
    }

    @Override
    protected boolean validate(T contest) {
        boolean result = true;
        String id = contest.getId();
        if (contest instanceof UpcomingContest) {
            result = result & contestDAO.find(ActiveContest.class, id) == null;
        }
        result = result & contestDAO.find(contest.getClass(), id) == null; 
        return result;
    }
}

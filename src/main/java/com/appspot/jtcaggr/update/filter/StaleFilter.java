package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
public class StaleFilter<T extends Contest> extends AbstractFilter<T> {

    private ContestDAO contestDAO;

    public StaleFilter() {
    }

    public StaleFilter(Filter<T> nextFilter) {
        super(nextFilter);
    }

    public void setContestDAO(ContestDAO contestDAO) {
        this.contestDAO = contestDAO;
    }

    @Override
    protected boolean validate(T contest) {
        boolean result = true;
        if (contest instanceof UpcomingContest) {
            String id = contest.getId();
            result = result & contestDAO.find(ActiveContest.class, id) == null;
        }
        return result;
    }
}

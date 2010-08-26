package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;

import java.util.Calendar;
import java.util.Date;

/**
 * @author shyiko
 * @since Aug 21, 2010
 */
public class ObsoleteFilter<T extends Contest> extends AbstractFilter<T> {

    public ObsoleteFilter() {
    }

    public ObsoleteFilter(Filter<T> nextFilter) {
        super(nextFilter);
    }

    @Override
    protected boolean validate(T contest) {
        Calendar submitByCalendar = Calendar.getInstance();
        submitByCalendar.setTime(contest.getSubmitBy());
        removeTimeInformation(submitByCalendar);

        Calendar todayCalendar = Calendar.getInstance();
        removeTimeInformation(todayCalendar);

        return (submitByCalendar.after(todayCalendar) || submitByCalendar.equals(todayCalendar));
//        return (contest.getSubmitBy().getTime() / 1000) >= (new Date().getTime() / 1000);
    }

    private void removeTimeInformation(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}

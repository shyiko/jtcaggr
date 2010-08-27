package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author shyiko
 * @since Aug 23, 2010
 */
public class ObsoleteFilterTest {

    private ObsoleteFilter<Contest> filter;

    @BeforeClass
    public void setUp() {
        filter = new ObsoleteFilter<Contest>();
    }

    @Test
    public void testIfYesterdayIsNotValid() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, -1);
        Contest contest = new UpcomingContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(), instance.getTime(), 0);
        Assert.assertFalse(filter.isValid(contest));
    }

    @Test
    public void testIfTomorrowIsValid() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, 1);
        Contest contest = new UpcomingContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                new Date(), instance.getTime(), 0);
        Assert.assertTrue(filter.isValid(contest));
    }

    @Test
    public void testIfTodayIsValid() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 1);
        calendar.set(Calendar.SECOND, 2);
        Date submitByDate = calendar.getTime();
        Contest contest = new UpcomingContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link",
                submitByDate, submitByDate, 0);
        Assert.assertTrue(filter.isValid(contest));
    }

}

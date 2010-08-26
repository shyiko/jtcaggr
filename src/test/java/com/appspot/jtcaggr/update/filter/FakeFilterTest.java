package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shyiko
 * @since Aug 23, 2010
 */
public class FakeFilterTest {

    private FakeFilter<Contest> filter;

    @BeforeClass
    public void setUp() {
        filter = new FakeFilter<Contest>();
    }

    @Test
    public void testIfContestWithNameDeleteMeIsInvalid() {
        Assert.assertFalse(filter.isValid(createUpcomingContest("DelETE ME 1.0")));
    }

    @Test
    public void testIfContestWithUsualNameIsValid() {
        Assert.assertTrue(filter.isValid(createUpcomingContest("Some valid name")));
    }

    private UpcomingContest createUpcomingContest(String name) {
        return new UpcomingContest(null, null, name, null, null, null, null);
    }
}

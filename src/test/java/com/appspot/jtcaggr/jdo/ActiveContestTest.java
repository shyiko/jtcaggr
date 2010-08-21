package com.appspot.jtcaggr.jdo;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * @author shyiko
 * @since Aug 6, 2010
 */
public class ActiveContestTest {

    private ActiveContest contest;

    @BeforeTest
    public void setUp() {
        contest = new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link", new Date(0),
                new Date(1), 2, 3, 4, 5, 6, 7);
    }

    @Test
    public void testGetType() {
        Assert.assertEquals(contest.getCompetition(), Competition.DEVELOPMENT);
    }

    @Test
    public void testGetCatalog() {
        Assert.assertEquals(contest.getCatalog(), Catalog.JAVA);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(contest.getName(), "name");
    }

    @Test
    public void testGetLink() {
        Assert.assertEquals(contest.getId(), "link");
    }

    @Test
    public void testGetRegisterBy() {
        Assert.assertEquals(contest.getRegisterBy(), new Date(0));
    }

    @Test
    public void testGetSubmitBy() {
        Assert.assertEquals(contest.getSubmitBy(), new Date(1));
    }

    @Test
    public void testGetPayment() {
        Assert.assertEquals(contest.getPayment(), Integer.valueOf(2));
    }

    @Test
    public void testGetReliabilityBonus() {
        Assert.assertEquals(contest.getReliabilityBonus(), Integer.valueOf(3));
    }

    @Test
    public void testGetDigitalRunPoint() {
        Assert.assertEquals(contest.getDigitalRunPoint(), Integer.valueOf(4));
    }

    @Test
    public void testGetRatedRegistrantsCount() {
        Assert.assertEquals(contest.getRatedRegistrantsCount(), Integer.valueOf(5));
    }

    @Test
    public void testGetUnratedRegistrantsCount() {
        Assert.assertEquals(contest.getUnratedRegistrantsCount(), Integer.valueOf(6));
    }

    @Test
    public void testGetSubmissions() {
        Assert.assertEquals(contest.getSubmissions(), Integer.valueOf(7));
    }

}

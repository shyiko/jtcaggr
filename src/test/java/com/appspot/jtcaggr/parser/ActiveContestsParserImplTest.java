package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.Contest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 6, 2010
 */
public class ActiveContestsParserImplTest {

    @Test
    public void testParse() throws Exception {
        URL url = Mockito.mock(URL.class);
        FileInputStream inputStream = new FileInputStream("src/test/resources/tc?module=ViewActiveContests&ph=113.html");
        try {
            Mockito.when(url.openStream()).thenReturn(inputStream);
            MultipleContestsParser parser = new ActiveContestsParserImpl();
            List<Contest> contests = parser.parse(url);
            ActiveContest contest = (ActiveContest) contests.get(0);
            Assert.assertEquals(contest.getCompetition(), Competition.DEVELOPMENT);
            Assert.assertEquals(contest.getCatalog(), Catalog.JAVA);
            Assert.assertEquals(contest.getName(), "Vesta IT Self Service Utility Services (1.0)");
            Assert.assertEquals(contest.getId(), "30013724");
            Assert.assertEquals(contest.getProjectURL(), "http://www.topcoder.com/tc?module=ProjectDetail&pj=30013724");
            Assert.assertEquals(contest.getRegistrantsURL(), "http://www.topcoder.com/tc?module=ViewRegistrants&pj=30013724");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
            Assert.assertEquals(contest.getRegisterBy(), dateFormat.parse("08.06.2010"));
            Assert.assertEquals(contest.getSubmitBy(), dateFormat.parse("08.10.2010"));
            Assert.assertEquals(contest.getPayment(), Integer.valueOf(900));
            Assert.assertEquals(contest.getReliabilityBonus(), Integer.valueOf(180));
            Assert.assertEquals(contest.getDigitalRunPoint(), Integer.valueOf(405));
            Assert.assertEquals(contest.getRatedRegistrantsCount(), Integer.valueOf(17));
            Assert.assertEquals(contest.getUnratedRegistrantsCount(), Integer.valueOf(11));
            Assert.assertEquals(contest.getSubmissions(), Integer.valueOf(0));
            Assert.assertEquals(contests.size(), 1);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testParsePageWithMultipleContests() throws Exception {
        URL url = Mockito.mock(URL.class);
        FileInputStream inputStream = new FileInputStream("src/test/resources/tc?module=ViewActiveContests&ph=113-2.html");
        try {
            Mockito.when(url.openStream()).thenReturn(inputStream);
            MultipleContestsParser parser = new ActiveContestsParserImpl();
            List<Contest> contests = parser.parse(url);
            Assert.assertEquals(contests.size(), 9);
        } finally {
            inputStream.close();
        }
    }
}

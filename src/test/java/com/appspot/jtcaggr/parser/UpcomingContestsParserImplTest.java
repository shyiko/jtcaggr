package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.Competition;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
public class UpcomingContestsParserImplTest {

    @Test
    public void testParse() throws Exception {
        URL url = Mockito.mock(URL.class);
        FileInputStream inputStream = new FileInputStream("src/test/resources/upcoming-contests-rss.xml");
        try {
            Mockito.when(url.openStream()).thenReturn(inputStream);
            UpcomingContestsParser parser = new UpcomingContestsParserImpl();
            List<UpcomingContest> contests = parser.parse(url);
            Contest contest = contests.get(0);
            Assert.assertEquals(contest.getCompetition(), Competition.DEVELOPMENT);
            Assert.assertEquals(contest.getName(), "Vesta Showroom Consultation Smart GWT Client 1.0");
            Assert.assertEquals(contest.getId(), "30013966");
            Assert.assertEquals(contest.getProjectURL(), "http://www.topcoder.com/tc?module=ProjectDetail&pj=30013966");
            Assert.assertEquals(contest.getRegistrantsURL(), "http://www.topcoder.com/tc?module=ViewRegistrants&pj=30013966");
            Assert.assertEquals(contests.size(), 42);
        } finally {
            inputStream.close();
        }
    }
}

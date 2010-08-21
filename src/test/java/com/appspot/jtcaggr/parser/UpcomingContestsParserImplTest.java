package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.Competition;
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
        FileInputStream inputStream = new FileInputStream("src/test/resources/tc?module=BasicRSS&t=new_report&c=rss_Pipeline&dsid=28.xml");
        try {
            Mockito.when(url.openStream()).thenReturn(inputStream);
            SingleContestParser singleContestParserMock = Mockito.mock(SingleContestParser.class);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
            Date registerBy = dateFormat.parse("08.13.2010");
            Date submitBy = dateFormat.parse("08.18.2010");
            Contest contestMock = new UpcomingContest(null, Catalog.JAVA, null, null, registerBy,
                    submitBy, 500);
            Mockito.when(singleContestParserMock.parse(Mockito.any(URL.class))).thenReturn(contestMock);
            UpcomingContestsParserImpl parser = new UpcomingContestsParserImpl(singleContestParserMock);
            List<Contest> contests = parser.parse(url);
            Contest contest = contests.get(0);
            Assert.assertEquals(contest.getCompetition(), Competition.DEVELOPMENT);
            Assert.assertEquals(contest.getCatalog(), Catalog.JAVA);
            Assert.assertEquals(contest.getName(), "Vesta Showroom Consultation Smart GWT Client 1.0");
            Assert.assertEquals(contest.getId(), "30013966");
            Assert.assertEquals(contest.getProjectURL(), "http://www.topcoder.com/tc?module=ProjectDetail&pj=30013966");
            Assert.assertEquals(contest.getRegistrantsURL(), "http://www.topcoder.com/tc?module=ViewRegistrants&pj=30013966");
            Assert.assertEquals(contest.getRegisterBy(), registerBy);
            Assert.assertEquals(contest.getSubmitBy(), submitBy);
            Assert.assertEquals(contest.getPayment(), Integer.valueOf(500));
            Assert.assertEquals(contests.size(), 42);
        } finally {
            inputStream.close();
        }
    }
}

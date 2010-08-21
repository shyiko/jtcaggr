package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.jdo.Catalog;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
public class ContestPageParserImplTest {

    @Test
    public void testParse() throws Exception {
        URL url = Mockito.mock(URL.class);
        FileInputStream inputStream = new FileInputStream("src/test/resources/tc?module=ProjectDetail&pj=30013966.html");
        Mockito.when(url.openStream()).thenReturn(inputStream);
        try {
            ContestPageParserImpl contestPageParser = new ContestPageParserImpl();
            UpcomingContest contest = contestPageParser.parse(url);
            Assert.assertNull(contest.getCompetition());
            Assert.assertEquals(contest.getCatalog(), Catalog.JAVA);
            Assert.assertNull(contest.getName());
            Assert.assertNull(contest.getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
            Assert.assertEquals(contest.getRegisterBy(), dateFormat.parse("08.13.2010"));
            Assert.assertEquals(contest.getSubmitBy(), dateFormat.parse("08.18.2010"));
            Assert.assertEquals(contest.getPayment(), Integer.valueOf(700));
        } finally {
            inputStream.close();
        }
    }
}

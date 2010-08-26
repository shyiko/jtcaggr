package com.appspot.jtcaggr.update;

import com.appspot.jtcaggr.URL;
import com.appspot.jtcaggr.guice.FilterForNewContestsProvider;
import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.JDOTest;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.mail.MailQueue;
import com.appspot.jtcaggr.parser.ActiveContestsParser;
import com.appspot.jtcaggr.parser.ActiveContestsParserImpl;
import static org.mockito.Mockito.*;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 26, 2010
 */
public class UpdateActiveContestsServletTest extends JDOTest {

    @Test
    public void testUpdate() throws Exception {
        URL url = Mockito.mock(URL.class);
        FileInputStream inputStream = new FileInputStream("src/test/resources/active-contests-page-ex2.html");
        List<ActiveContest> contests;
        try {
            Mockito.when(url.openStream()).thenReturn(inputStream);
            ActiveContestsParser parser = new ActiveContestsParserImpl();
            contests = parser.parse(url);
        } finally {
            inputStream.close();
        }

        ActiveContestsParser parser = mock(ActiveContestsParser.class);
        when(parser.parse(Matchers.<URL>any())).thenReturn(contests);

        ContestDAO contestDAO = new ContestDAO();
        contestDAO.setPersistenceManagerFactory(getPersistenceManagerFactory());

        FilterForNewContestsProvider filterProvider = new FilterForNewContestsProvider(contestDAO);

        MailQueue mailQueue = new MailQueue();

        UpdateActiveContestsServlet servlet = new UpdateActiveContestsServlet(parser, filterProvider.get(),
                contestDAO, mailQueue);
        servlet.service(mock(HttpServletRequest.class), null);

        Assert.assertEquals(mailQueue.getNewContests().size(), 3);        
    }
}

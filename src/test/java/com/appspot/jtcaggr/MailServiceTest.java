package com.appspot.jtcaggr;

import com.appspot.jtcaggr.jdo.*;
import com.appspot.jtcaggr.jdo.dao.SubscriberDAO;
import com.appspot.jtcaggr.subscribing.SubscribeUtils;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 13, 2010
 */
public class MailServiceTest extends JDOTest {

    @Test
    public void testInformSubscribesAboutNewContests() throws Exception {
        SubscriberDAO subscriberDAO = Mockito.mock(SubscriberDAO.class);
        List<Subscriber> subscribers = new ArrayList<Subscriber>(1);
        subscribers.add(new Subscriber("body@gmail.com"));
        Mockito.when(subscriberDAO.findAll()).thenReturn(subscribers);
        SubscribeUtils subscribeUtils = new SubscribeUtils(subscriberDAO);
        final MutableBoolean sendInvoked = new MutableBoolean(false);
        MailService mailService = new MailService(subscriberDAO, subscribeUtils) {
            @Override
            protected void sendMessage(Session session, InternetAddress from, InternetAddress to, String subject, String text) throws MessagingException {
                sendInvoked.setValue(true);
                Assert.assertEquals(subject, "TopCoder updates");
                Assert.assertEquals(from.getAddress(), "admin@jtcaggr.appspot.com");
                Assert.assertEquals(from.getPersonal(), "Stanley Shyiko");
                Assert.assertEquals(to.getAddress(), "body@gmail.com");
                Assert.assertEquals(text, "New contests were added to the TopCoder database.<br>" +
                                        "New active contests:<br>" +
                                        "<a href=\"http://www.topcoder.com/tc?module=ProjectDetail&pj=link\">name</a><br>" +
                                        "<br>" +
                                        "This message was automatically generated due to the subscription at " +
                                            "<a href=\"http://jtcaggr.appspot.com\">jtcaggr.appspot.com</a>.<br>" +
                                        "If you wish to unsubscribe, click " +
                                        "<a href=\"http://jtcaggr.appspot.com/unsubscribe?hash=fda922494587dcf722789f530fdf29\">here</a>.");
            }
        };
        List<Contest> contests = new ArrayList<Contest>() {{
            add(new ActiveContest(Competition.DEVELOPMENT, Catalog.JAVA, "name", "link", new Date(0),
                    new Date(1), 2, 3, 4, 5, 6, 7));
        }};
        mailService.informSubscribesAboutNewContests(contests);
        Assert.assertTrue((Boolean) sendInvoked.getValue());
    }
}

package com.appspot.jtcaggr.mail;

import com.appspot.jtcaggr.jdo.ActiveContest;
import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.Subscriber;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import com.appspot.jtcaggr.jdo.dao.SubscriberDAO;
import com.appspot.jtcaggr.subscribing.SubscribeUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author shyiko
 * @since Aug 10, 2010
 */
@Singleton
public class MailServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MailServlet.class);

    private MailQueue mailQueue;
    private SubscriberDAO subscriberDAO;
    private SubscribeUtils subscribeUtils;

    @Inject
    public MailServlet(MailQueue mailQueue, SubscriberDAO subscriberDAO, SubscribeUtils subscribeUtils) {
        this.mailQueue = mailQueue;
        this.subscriberDAO = subscriberDAO;
        this.subscribeUtils = subscribeUtils;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        informSubscribesAboutNewContests();
    }

    public void informSubscribesAboutNewContests() {
        logger.info("Mail service started.");
        List<Contest> newContests = mailQueue.getNewContests();
        if (newContests.isEmpty()) {
            logger.info("No new contests were found. Skipping...");
            return;
        }
        logger.info(String.format("Found %d new contest(s).", newContests.size()));

        Collection<Subscriber> subscribers = subscriberDAO.findAll();
        logger.info(String.format("Found %d subscriber(s).", subscribers.size()));

        Session session = Session.getDefaultInstance(new Properties(), null);
        String messageBody = generateMessageBody(newContests);
        InternetAddress from;
        try {
            from = new InternetAddress("stas.shyiko@gmail.com");
        } catch (AddressException e) {
            logger.error(e.getMessage(), e);
            return;
        }
        for (Subscriber subscriber : subscribers) {
            try {
                InternetAddress to = new InternetAddress(subscriber.getEmail());
                StringBuilder sb = new StringBuilder();
                sb.append(messageBody);
                sb.append("<br>This message was automatically generated due to the subscription at " +
                        "<a href=\"http://jtcaggr.appspot.com\">jtcaggr.appspot.com</a>.");
                sb.append("<br>If you wish to unsubscribe, click <a href=\"http://jtcaggr.appspot.com/unsubscribe?hash=").
                        append(subscribeUtils.getEmailHash(subscriber.getEmail())).append("\">here</a>.");
                sendMessage(session, from, to, "TopCoder updates", sb.toString());
            } catch (AddressException e) {
                logger.error(e.getMessage(), e);
            } catch (MessagingException e) {
                logger.error(e.getMessage(), e);
            } 
        }
    }

    protected void sendMessage(Session session, InternetAddress from, InternetAddress to, String subject, String text)
            throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(from);
        msg.addRecipient(Message.RecipientType.TO, to);
        msg.setSubject(subject);
        msg.setContent(text, "text/html");
        Transport.send(msg);
    }

    private String generateMessageBody(List<Contest> newContests) {
        StringBuilder sb = new StringBuilder();
        sb.append("New contests were added to the TopCoder database.<br>");
        List<ActiveContest> activeContests = new LinkedList<ActiveContest>();
        List<UpcomingContest> upcomingContests = new LinkedList<UpcomingContest>();
        for (Contest contest : newContests) {
            if (contest instanceof ActiveContest)
                activeContests.add((ActiveContest) contest);
            else
            if (contest instanceof UpcomingContest)
                upcomingContests.add((UpcomingContest) contest);
            else
                throw new UnsupportedOperationException();
        }
        addContests(sb, "New active contests:<br>", activeContests);
        addContests(sb, "New upcoming contests:<br>", upcomingContests);
        return sb.toString();
    }

    private void addContests(StringBuilder sb, String paragraphBeginText, List contests) {
        if (contests.size() > 0) {
            sb.append(paragraphBeginText);
            for (Object contestObj : contests) {
                Contest contest = (Contest) contestObj;
                sb.append("<a href=\"").append(contest.getProjectURL()).append("\">").
                        append(contest.getName()).append("</a><br>");
            }
        }
    }
}

package com.appspot.jtcaggr.mail;

import com.appspot.jtcaggr.jdo.Contest;
import com.appspot.jtcaggr.jdo.UpcomingContest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author shyiko
 * @since Aug 25, 2010
 */
public class MailQueueTest {

    @Test
    public void testMarkAsNewAndGetNewContests() {
        MailQueue queue = new MailQueue();
        queue.markAsNew(new UpcomingContest(null, null, null, null, null, null, null));
        List<Contest> contestList = queue.getNewContests();
        Assert.assertEquals(contestList.size(), 1);
    }
}

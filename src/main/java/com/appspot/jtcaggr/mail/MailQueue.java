package com.appspot.jtcaggr.mail;

import com.appspot.jtcaggr.jdo.Contest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread-safety: Thread safe provided that input parameters are thread safe.
 * 
 * @author shyiko
 * @since Aug 25, 2010
 */
public class MailQueue {

    private ConcurrentLinkedQueue<Contest> updatedContests = new ConcurrentLinkedQueue<Contest>();

    public void markAsNew(Contest contest) {
        updatedContests.add(contest);
    }

    public void markAsNew(Collection<? extends Contest> contests) {
        updatedContests.addAll(contests);
    }

    public List<Contest> getNewContests() {
        return Arrays.asList(updatedContests.toArray(new Contest[] {}));
    }
}

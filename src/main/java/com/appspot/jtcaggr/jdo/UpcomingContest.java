package com.appspot.jtcaggr.jdo;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import java.util.Date;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
@PersistenceCapable
public class UpcomingContest extends Contest {

    public UpcomingContest(Competition competition, Catalog catalog, String name, String link,
                           Date registerBy, Date submitBy, Integer payment) {
        super(competition, catalog, name, link, registerBy, submitBy, payment);
    }
}

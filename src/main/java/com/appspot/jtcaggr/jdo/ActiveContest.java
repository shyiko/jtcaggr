package com.appspot.jtcaggr.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Inheritance;
import java.util.Date;

/**
 * @author shyiko
 * @since Aug 16, 2010
 */
@PersistenceCapable
public class ActiveContest extends Contest {

    private Integer reliabilityBonus;
    private Integer digitalRunPoint;
    private Integer ratedRegistrantsCount;
    private Integer unratedRegistrantsCount;
    private Integer submissions;

    public ActiveContest(Competition competition, Catalog catalog, String name, String link,
                         Date registerBy, Date submitBy, Integer payment, Integer reliabilityBonus,
                         Integer digitalRunPoint, Integer ratedRegistrantsCount,
                         Integer unratedRegistrantsCount, Integer submissions) {
        super(competition, catalog, name, link, registerBy, submitBy, payment);
        this.reliabilityBonus = reliabilityBonus;
        this.digitalRunPoint = digitalRunPoint;
        this.ratedRegistrantsCount = ratedRegistrantsCount;
        this.unratedRegistrantsCount = unratedRegistrantsCount;
        this.submissions = submissions;
    }

    public Integer getReliabilityBonus() {
        return reliabilityBonus;
    }

    public Integer getDigitalRunPoint() {
        return digitalRunPoint;
    }

    public Integer getRatedRegistrantsCount() {
        return ratedRegistrantsCount;
    }

    public Integer getUnratedRegistrantsCount() {
        return unratedRegistrantsCount;
    }

    public Integer getSubmissions() {
        return submissions;
    }
}

package com.appspot.jtcaggr.jdo;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Contest {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
    
    @Persistent
    private Competition competition;
    @Persistent
    private Catalog catalog;
    private String name;
    @Unique
    private String id;
    private Date registerBy;
    private Date submitBy;
    private Integer payment;

    protected Contest(Competition competition, Catalog catalog, String name, String id,
                      Date registerBy, Date submitBy, Integer payment) {
        this.competition = competition;
        this.catalog = catalog;
        this.name = name;
        this.id = id;
        this.registerBy = registerBy;
        this.submitBy = submitBy;
        this.payment = payment;
    }

    public Competition getCompetition() {
        return competition;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Date getRegisterBy() {
        return registerBy;
    }

    public Date getSubmitBy() {
        return submitBy;
    }

    public Integer getPayment() {
        return payment;
    }

    public String getProjectURL() {
        return "http://www.topcoder.com/tc?module=ProjectDetail&pj=" + id;
    }

    public String getRegistrantsURL() {
        return "http://www.topcoder.com/tc?module=ViewRegistrants&pj=" + id;
    }
}

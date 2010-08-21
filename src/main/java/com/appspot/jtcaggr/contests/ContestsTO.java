package com.appspot.jtcaggr.contests;

import java.io.Serializable;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
class ContestsTO implements Serializable {

    private int totalRecordsNumber;
    private String[][] data;

    public ContestsTO(int totalRecordsNumber, String[][] data) {
        this.totalRecordsNumber = totalRecordsNumber;
        this.data = data;
    }

    public int getTotalRecordsNumber() {
        return totalRecordsNumber;
    }

    public String[][] getData() {
        return data;
    }
}

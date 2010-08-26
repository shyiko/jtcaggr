package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.Contest;

import java.util.List;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
public interface Filter<T extends Contest> {

    List<T> filter(List<T> contestsToFilter);

    /**
     * Check if contest is valid within the bounds of filter.
     *
     * @param contest Contest to check
     * @return true if contest is valid, false otherwise
     */
    boolean isValid(T contest);
}

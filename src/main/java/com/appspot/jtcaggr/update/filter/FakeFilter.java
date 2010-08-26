package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.Contest;

import java.util.List;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
public class FakeFilter<T extends Contest> extends AbstractFilter<T> {

    public FakeFilter() {
    }

    public FakeFilter(Filter<T> nextFilter) {
        super(nextFilter);
    }

    @Override
    protected boolean validate(T contest) {
        return !contest.getName().toLowerCase().contains("delete me");
    }

}

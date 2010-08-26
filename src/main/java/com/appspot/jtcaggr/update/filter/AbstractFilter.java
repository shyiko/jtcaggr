package com.appspot.jtcaggr.update.filter;

import com.appspot.jtcaggr.jdo.Contest;

import java.util.LinkedList;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 19, 2010
 */
public abstract class AbstractFilter<T extends Contest> implements Filter<T> {

    private Filter<T> nextFilter;

    protected AbstractFilter() {
    }

    protected AbstractFilter(Filter<T> nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    public List<T> filter(List<T> contestsToFilter) {
        List<T> result = new LinkedList<T>();
        for (T contest : contestsToFilter) {
            if (isValid(contest))
                result.add(contest);
        }
        return result;
    }

    @Override
    public boolean isValid(T contest) {
        boolean result = validate(contest);
        if (nextFilter != null)
            result = result && nextFilter.isValid(contest);
        return result;
    }

    protected abstract boolean validate(T contest);
}

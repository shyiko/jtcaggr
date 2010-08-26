package com.appspot.jtcaggr.guice;

import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.update.filter.DuplicateFilter;
import com.appspot.jtcaggr.update.filter.FakeFilter;
import com.appspot.jtcaggr.update.filter.Filter;
import com.appspot.jtcaggr.update.filter.ObsoleteFilter;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author shyiko
 * @since Aug 21, 2010
 */
public class FilterProvider implements Provider<Filter> {

    private ContestDAO contestDAO;

    @Inject
    public FilterProvider(ContestDAO contestDAO) {
        this.contestDAO = contestDAO;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Filter get() {
        DuplicateFilter duplicateFilter = new DuplicateFilter();
        duplicateFilter.setContestDAO(contestDAO);
        return new FakeFilter(new ObsoleteFilter(duplicateFilter));
    }
}

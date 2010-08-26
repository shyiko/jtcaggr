package com.appspot.jtcaggr.guice;

import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.update.filter.*;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author shyiko
 * @since Aug 21, 2010
 */
public class FilterForNewContestsProvider implements Provider<Filter> {

    private ContestDAO contestDAO;

    @Inject
    public FilterForNewContestsProvider(ContestDAO contestDAO) {
        this.contestDAO = contestDAO;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Filter get() {
        DuplicateFilter duplicateFilter = new DuplicateFilter();
        duplicateFilter.setContestDAO(contestDAO);
        StaleFilter staleFilter = new StaleFilter(duplicateFilter);
        staleFilter.setContestDAO(contestDAO);
        return new FakeFilter(new ObsoleteFilter(staleFilter));
    }
}

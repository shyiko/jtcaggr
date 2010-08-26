package com.appspot.jtcaggr.guice;

import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.update.filter.*;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author shyiko
 * @since Aug 21, 2010
 */
public class FilterForPersistedContestsProvider implements Provider<Filter> {

    private ContestDAO contestDAO;

    @Inject
    public FilterForPersistedContestsProvider(ContestDAO contestDAO) {
        this.contestDAO = contestDAO;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Filter get() {
        StaleFilter staleFilter = new StaleFilter();
        staleFilter.setContestDAO(contestDAO);
        return new FakeFilter(new ObsoleteFilter(staleFilter));
    }
}

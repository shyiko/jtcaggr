package com.appspot.jtcaggr.guice;

import com.appspot.jtcaggr.parser.ActiveContestsParserImpl;
import com.appspot.jtcaggr.parser.UpcomingContestsParserImpl;
import com.appspot.jtcaggr.update.UpdateConfiguration;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author shyiko
 * @since Aug 14, 2010
 */
public class UpdateConfigurationProvider implements Provider<UpdateConfiguration> {

    @Override
    public UpdateConfiguration get() {
        UpdateConfiguration updateConfiguration = new UpdateConfiguration();
        updateConfiguration.add("http://www.topcoder.com/tc?module=ViewActiveContests&ph=113",
                GuiceServletConfig.TASKS_UPDATE_ACTIVE_CONTESTS);
        updateConfiguration.add("http://www.topcoder.com/tc?module=BasicRSS&t=new_report&c=rss_Pipeline&dsid=28",
                GuiceServletConfig.TASKS_UPDATE_UPCOMING_CONTESTS);
        return updateConfiguration;
    }
}

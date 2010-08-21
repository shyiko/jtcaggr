package com.appspot.jtcaggr.guice;

import com.appspot.jtcaggr.parser.ActiveContestsParserImpl;
import com.appspot.jtcaggr.parser.UpcomingContestsParserImpl;
import com.appspot.jtcaggr.update.UpdateConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author shyiko
 * @since Aug 14, 2010
 */
public class UpdateConfigProvider implements Provider<UpdateConfig> {

    private ActiveContestsParserImpl activeContestsParser;
    private UpcomingContestsParserImpl upcomingContestsParser;

    @Inject
    public UpdateConfigProvider(ActiveContestsParserImpl activeContestsParser,
                                UpcomingContestsParserImpl upcomingContestsParser) {
        this.activeContestsParser = activeContestsParser;
        this.upcomingContestsParser = upcomingContestsParser;
    }

    @Override
    public UpdateConfig get() {
        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.add("http://www.topcoder.com/tc?module=ViewActiveContests&ph=113", activeContestsParser);
        updateConfig.add("http://www.topcoder.com/tc?module=BasicRSS&t=new_report&c=rss_Pipeline&dsid=28", upcomingContestsParser);
        return updateConfig;
    }
}

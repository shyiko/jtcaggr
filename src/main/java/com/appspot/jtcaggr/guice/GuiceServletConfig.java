package com.appspot.jtcaggr.guice;

import com.appspot.jtcaggr.contests.ContestsJSONFilter;
import com.appspot.jtcaggr.contests.DefaultContestsProviderImpl;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.jdo.dao.SubscriberDAO;
import com.appspot.jtcaggr.parser.ActiveContestsParserImpl;
import com.appspot.jtcaggr.parser.ContestPageParserImpl;
import com.appspot.jtcaggr.parser.SingleContestParser;
import com.appspot.jtcaggr.parser.UpcomingContestsParserImpl;
import com.appspot.jtcaggr.subscribing.SubscribeServlet;
import com.appspot.jtcaggr.subscribing.SubscribeUtils;
import com.appspot.jtcaggr.update.UpdateConfig;
import com.appspot.jtcaggr.update.Updater;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * @author shyiko
 * @since Aug 7, 2010
 */
public class GuiceServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(PersistenceManagerFactory.class).toInstance(JDOHelper.getPersistenceManagerFactory("pmf"));

                bind(ActiveContestsParserImpl.class).in(Singleton.class);
                bind(SingleContestParser.class).to(ContestPageParserImpl.class).in(Singleton.class);
                bind(UpcomingContestsParserImpl.class).in(Singleton.class);

                bind(UpdateConfig.class).toProvider(UpdateConfigProvider.class).in(Singleton.class);
                bind(SubscribeUtils.class).in(Singleton.class);

                bind(ContestDAO.class).in(Singleton.class);
                bind(SubscriberDAO.class).in(Singleton.class);

                serve("/contests/cdev").with(DefaultContestsProviderImpl.class);
                serve("/subscribe").with(SubscribeServlet.class);
                serve("/unsubscribe").with(SubscribeServlet.class);
                serve("/cron/update").with(Updater.class);

                filter("/contests/*").through(ContestsJSONFilter.class);
            }
        });
    }
}

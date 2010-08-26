package com.appspot.jtcaggr.guice;

import com.appspot.jtcaggr.contests.ContestsJSONFilter;
import com.appspot.jtcaggr.contests.DefaultContestsProviderServletImpl;
import com.appspot.jtcaggr.jdo.dao.ContestDAO;
import com.appspot.jtcaggr.jdo.dao.SubscriberDAO;
import com.appspot.jtcaggr.mail.MailQueue;
import com.appspot.jtcaggr.mail.MailServlet;
import com.appspot.jtcaggr.parser.*;
import com.appspot.jtcaggr.subscribing.SubscribeServlet;
import com.appspot.jtcaggr.subscribing.SubscribeUtils;
import com.appspot.jtcaggr.update.*;
import com.appspot.jtcaggr.update.filter.Filter;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * @author shyiko
 * @since Aug 7, 2010
 */
public class GuiceServletConfig extends GuiceServletContextListener {

    public static final String TASKS_UPDATE_ACTIVE_CONTESTS = "/tasks/updateActiveContests";
    public static final String TASKS_UPDATE_UPCOMING_CONTEST = "/tasks/updateUpcomingContest";
    public static final String TASKS_UPDATE_UPCOMING_CONTESTS = "/tasks/updateUpcomingContests";

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(PersistenceManagerFactory.class).toInstance(JDOHelper.getPersistenceManagerFactory("pmf"));

                bind(ActiveContestsParser.class).to(ActiveContestsParserImpl.class).in(Singleton.class);
                bind(ContestPageParser.class).to(ContestPageParserImpl.class).in(Singleton.class);
                bind(UpcomingContestsParser.class).to(UpcomingContestsParserImpl.class).in(Singleton.class);

                bind(UpdateConfiguration.class).toProvider(UpdateConfigurationProvider.class).in(Singleton.class);
                bind(SubscribeUtils.class).in(Singleton.class);

                bind(ContestDAO.class).in(Singleton.class);
                bind(SubscriberDAO.class).in(Singleton.class);
                bind(MailQueue.class).in(Singleton.class);

                bind(Filter.class).annotatedWith(Names.named("PersistedFilter")).
                        toProvider(FilterForPersistedContestsProvider.class).in(Singleton.class);

                bind(Filter.class).toProvider(FilterForNewContestsProvider.class).in(Singleton.class);

                bind(Queue.class).annotatedWith(Names.named("UpdateQueue")).toInstance(QueueFactory.getQueue("update-queue"));
                bind(String.class).annotatedWith(Names.named("UpdateUpcomingContestTaskURL")).toInstance(TASKS_UPDATE_UPCOMING_CONTEST);

                // contests
                serve("/contests/cdev").with(DefaultContestsProviderServletImpl.class);
                // subscribe
                serve("/subscribe").with(SubscribeServlet.class);
                serve("/unsubscribe").with(SubscribeServlet.class);
                // update
                serve("/cron/update").with(UpdateServlet.class);
                serve(TASKS_UPDATE_ACTIVE_CONTESTS).with(UpdateActiveContestsServlet.class);
                serve(TASKS_UPDATE_UPCOMING_CONTEST).with(UpdateUpcomingContestServlet.class);
                serve(TASKS_UPDATE_UPCOMING_CONTESTS).with(UpdateUpcomingContestsServlet.class);
                // mail
                serve("/cron/mail").with(MailServlet.class);
                // stale contests remover
                serve("/cron/stale").with(StaleContestsRemoveServlet.class);

                filter("/contests/*").through(ContestsJSONFilter.class);
            }
        });
    }
}

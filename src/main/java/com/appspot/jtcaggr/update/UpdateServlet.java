package com.appspot.jtcaggr.update;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
@Singleton
public class UpdateServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UpdateServlet.class);

    private UpdateConfiguration updateConfiguration;
    private Queue updateQueue;

    @Inject
    public UpdateServlet(UpdateConfiguration updateConfiguration,
                         @Named("UpdateQueue") Queue updateQueue) {
        this.updateConfiguration = updateConfiguration;
        this.updateQueue = updateQueue;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Update service started.");
        Set<String> urls = updateConfiguration.getUrls();
        logger.info(urls.size() + " urls scheduled for update.");
        for (String url : urls) {
            String handler = updateConfiguration.getHandler(url);
            updateQueue.add(url(handler).param(ContestUpdateServlet.LINK, url));
        }
    }
}

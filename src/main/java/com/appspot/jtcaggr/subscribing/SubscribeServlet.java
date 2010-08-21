package com.appspot.jtcaggr.subscribing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author shyiko
 * @since Aug 17, 2010
 */
@Singleton
public class SubscribeServlet extends HttpServlet {

    /**
     * Pattern is thread safe, so no need for worrying
     */
    private Pattern emailPattern = Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    private static final String UNSUBSCRIBE_REQUEST_HASH_PARAM = "hash";
    private static final String SUBSCRIBE_REQUEST_EMAIL_PARAM = "email";
    private SubscribeUtils subscribeUtils;

    @Inject
    public SubscribeServlet(SubscribeUtils subscribeUtils) {
        this.subscribeUtils = subscribeUtils;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter(SUBSCRIBE_REQUEST_EMAIL_PARAM);
        if (!isEmailValid(email)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email " + email + " is not valid.");
            return;
        }
        try {
            subscribeUtils.subscribe(email);
        } catch (AlreadySubscribedException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hash = req.getParameter(UNSUBSCRIBE_REQUEST_HASH_PARAM);
        if (hash == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    UNSUBSCRIBE_REQUEST_HASH_PARAM + "should not be null.");
            return;
        }
        try {
            subscribeUtils.unsubscribe(hash);
        } catch (NotSubscribedException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }


    private boolean isEmailValid(String email) {
        return email != null && emailPattern.matcher(email).matches();
    }

}

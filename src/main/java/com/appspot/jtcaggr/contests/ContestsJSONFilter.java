package com.appspot.jtcaggr.contests;

import com.appspot.jtcaggr.contests.ContestsTO;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
@Singleton
public class ContestsJSONFilter implements Filter {

    private static final String I_TOTAL_RECORDS = "iTotalRecords";
    private static final String I_TOTAL_DISPLAY_RECORDS = "iTotalDisplayRecords";
    private static final String AA_DATA = "aaData";
    private static final String S_ECHO = "sEcho";
    public static final String CONTESTS = "Contests";

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse aResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) aResponse;

        if (!isRequestValid(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        chain.doFilter(request, response);

        sendJSONResponse(request, response);
    }

    private boolean isRequestValid(ServletRequest request) throws IOException {
        String sEcho = request.getParameter(S_ECHO);
        // checking whether sEcho is a number in order to prevent Cross Site Scripting (XSS) attacks
        return !(sEcho == null || !isNumber(sEcho));
    }

    private boolean isNumber(String value) {
        byte[] bytes = value.getBytes();
        for (byte aByte : bytes) {
            if (!Character.isDigit(aByte))
                return false;
        }
        return true;
    }

    private void sendJSONResponse(ServletRequest request, HttpServletResponse response) throws IOException {
        ContestsTO contests = (ContestsTO) request.getAttribute(CONTESTS);
        if (contests == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving contests");
            return;
        }

        JSONObject result = new JSONObject();
        try {
            result.put(I_TOTAL_RECORDS, contests.getTotalRecordsNumber());
            result.put(S_ECHO, request.getParameter(S_ECHO));
            JSONArray data = new JSONArray();
            String[][] contestsData = contests.getData();
            result.put(I_TOTAL_DISPLAY_RECORDS, contestsData.length);
            for (String[] row : contestsData) {
                JSONArray rowArray = new JSONArray();
                for (String cell : row) {
                    rowArray.put(cell);
                }
                data.put(rowArray);
            }
            result.put(AA_DATA, data);
        } catch (JSONException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(result.toString());
        response.getWriter().close();
    }

    public void destroy() {
    }


}

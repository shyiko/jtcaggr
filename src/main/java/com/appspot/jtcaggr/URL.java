package com.appspot.jtcaggr;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


/**
 * Wrapper for java.net.URL class.
 * java.net.URL is final along with its method openStream().
 * This makes classes that depend on java.net.URL hard to test.
 *
 * @author shyiko
 * @since Aug 14, 2010
 */
public class URL {

    private static final int CONNECTION_TIMEOUT = 10000;

    private String url;

    public URL(String url) {
        this.url = url;
    }

    public InputStream openStream() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new java.net.URL(url).openConnection();
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        return connection.getInputStream();
    }

    @Override
    public String toString() {
        return url;
    }
}

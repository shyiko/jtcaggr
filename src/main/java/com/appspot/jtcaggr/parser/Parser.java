package com.appspot.jtcaggr.parser;

import com.appspot.jtcaggr.URL;

/**
 * @author shyiko
 * @since Aug 5, 2010
 */
public interface Parser<T> {

    T parse(URL url) throws ParsingException;
}

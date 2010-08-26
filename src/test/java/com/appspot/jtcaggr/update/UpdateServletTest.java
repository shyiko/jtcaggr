package com.appspot.jtcaggr.update;

import com.google.appengine.api.labs.taskqueue.Queue;
import static org.mockito.Mockito.*;

import com.google.appengine.api.labs.taskqueue.TaskOptions;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * @author shyiko
 * @since Aug 23, 2010
 */
public class UpdateServletTest {

    @Test
    public void testUpdate() throws Exception {
        UpdateConfiguration updateConfiguration = new UpdateConfiguration();
        updateConfiguration.add("url1", "/handler1");
        updateConfiguration.add("url2", "/handler2");
        Queue queueMock = mock(Queue.class);
        final List<TaskOptions> tasks = new LinkedList<TaskOptions>();
        when(queueMock.add(Matchers.<TaskOptions>any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                tasks.add((TaskOptions) invocationOnMock.getArguments()[0]);
                return null;
            }
        });
        UpdateServlet servlet = new UpdateServlet(updateConfiguration, queueMock);
        servlet.service(null, null);
        assertEquals(tasks.size(), 2);
        TaskOptions firstTask = tasks.get(0);
        assertEquals(firstTask.getUrl(), "/handler1");
    }
}

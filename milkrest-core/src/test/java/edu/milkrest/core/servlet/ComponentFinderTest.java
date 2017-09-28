package edu.milkrest.core.servlet;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ComponentFinderTest {
    private ServletContext  servletContext;
    private ComponentFinder componentFinder;

    @Before
    public void setUp() throws Exception {
        componentFinder = new ComponentFinder();
        componentFinder.reset();

        servletContext = mock(ServletContext.class);
        when(servletContext.getInitParameter("edu.milkrest.scan.skip.packages"))
                .thenReturn("edu.milkrest.core.servlet.Skip1, edu.milkrest.core.servlet.Skip2");
    }

    @Test
    public void skipsClassesThatConfiguredAsSkippedInServletContext() throws Exception {
        HashSet<Class<?>> classes = new HashSet<>(Arrays.asList(Skip1.class, Deploy.class, Skip2.class));
        componentFinder.onStartup(classes, servletContext);
        assertEquals(new HashSet<Class<?>>(Arrays.asList(Deploy.class)), ComponentFinder.findComponents());
    }
}

class Skip1 {

}

class Skip2 {

}

class Deploy {

}

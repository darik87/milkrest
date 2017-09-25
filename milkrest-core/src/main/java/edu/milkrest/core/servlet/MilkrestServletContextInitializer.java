package edu.milkrest.core.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MilkrestServletContextInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(MilkrestServletContextInitializer.class);

    public static final String MILKREST_SCAN_COMPONENTS = "edu.milkrest.scan.components";
    public static final String MILKREST_SCAN_SKIP_PACKAGES = "edu.milkrest.scan.skip.packages";
    public static final String JAXRS_APPLICATION           = "javax.ws.rs.Application";

    protected final ServletContext servletContext;

    public MilkrestServletContextInitializer(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Try get application's FQN from context-param javax.ws.rs.Application and instantiate it. If such parameter is not
     * specified then scan web application's folders WEB-INF/classes and WEB-INF/lib for classes which contains JAX-RS
     * annotations: {@link Path}, {@link Provider} .
     *
     * @return instance of javax.ws.rs.core.Application
     */
    public Application getApplication() {
        Application application = null;
        String applicationFQN = getParameter(JAXRS_APPLICATION);
        boolean scan = getBoolean(MILKREST_SCAN_COMPONENTS, false);
        if (applicationFQN != null) {
            if (scan) {
                String msg = "Scan of JAX-RS components is disabled cause to specified 'javax.ws.rs.Application'.";
                LOG.warn(msg);
            }
            try {
                Class<?> cl = Thread.currentThread().getContextClassLoader().loadClass(applicationFQN);
                application = (Application)cl.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else if (scan) {
            application = new Application() {
                @Override
                public Set<Class<?>> getClasses() {
                    return new LinkedHashSet<>(ComponentFinder.findComponents());
                }
            };
        }
        return application;
    }


    protected List<String> getParameterNames() {
        return Collections.list(servletContext.getInitParameterNames());
    }

    /**
     * Get parameter with specified name from servlet context initial parameters.
     *
     * @param name
     *         parameter name
     * @return value of parameter with specified name
     */
    protected String getParameter(String name) {
        String str = servletContext.getInitParameter(name);
        if (str != null) {
            return str.trim();
        }
        return null;
    }

    protected String getParameter(String name, String def) {
        String value = getParameter(name);
        if (value == null) {
            return def;
        }
        return value;
    }

    protected boolean getBoolean(String name, boolean def) {
        String str = getParameter(name);
        if (str != null) {
            return "true".equalsIgnoreCase(str) || "yes".equalsIgnoreCase(str) || "on".equalsIgnoreCase(str) || "1".equals(str);
        }
        return def;
    }

    protected Double getNumber(String name, double def) {
        String str = getParameter(name);
        if (str != null) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException ignored) {
            }
        }
        return def;
    }
}

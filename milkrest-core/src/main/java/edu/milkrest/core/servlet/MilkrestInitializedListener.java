package edu.milkrest.core.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.Application;

public class MilkrestInitializedListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        servletContext.removeAttribute(Application.class.getName());
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        MilkrestServletContextInitializer initializer = new MilkrestServletContextInitializer(servletContext);
        initializeMilkRestComponents(initializer, servletContext);
    }

    void initializeMilkRestComponents(MilkrestServletContextInitializer initializer, ServletContext servletContext) {
    }
}

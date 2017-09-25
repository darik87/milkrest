package edu.milkrest.core.servlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.*;

import static java.lang.reflect.Modifier.isAbstract;
import static edu.milkrest.core.servlet.MilkrestServletContextInitializer.MILKREST_SCAN_SKIP_PACKAGES;

@HandlesTypes({Path.class, Provider.class})
public class ComponentFinder implements ServletContainerInitializer {

    private static Set<Class<?>> scanned = new LinkedHashSet<>();

    public static Set<Class<?>> findComponents() {
        return scanned;
    }

    private Set<String> defaultSkipPackages = new HashSet<>(Arrays.asList("edu.milkrest.core", "javax.ws.rs"));

    void reset() {
        defaultSkipPackages.clear();
        scanned.clear();
    }

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext ctx) throws ServletException {
        if (classes != null) {
            List<String> skip = new LinkedList<>();
            String skipParameter = ctx.getInitParameter(MILKREST_SCAN_SKIP_PACKAGES);
            if (skipParameter != null) {
                for (String skipPrefix : skipParameter.split(",")) {
                    skip.add(skipPrefix.trim());
                }
            }
            skip.addAll(defaultSkipPackages);
            for (Class<?> clazz : classes) {
                if (!clazz.isInterface()
                    && !isAbstract(clazz.getModifiers())
                    && (clazz.getEnclosingClass() == null)
                    && !isSkipped(skip, clazz)) {
                    scanned.add(clazz);
                }
            }
        }
    }

    private boolean isSkipped(List<String> forSkipping, Class<?> clazz) {
        final String clazzName = clazz.getName();
        for (String skipPrefix : forSkipping) {
            if (clazzName.startsWith(skipPrefix)) {
                return true;
            }
        }
        return false;
    }
}

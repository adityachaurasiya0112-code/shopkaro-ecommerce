package com.shopsphere.listener;

import com.shopsphere.dao.UserDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Application Lifecycle and Session Event Listener.
 * Demonstrates RTU syllabus: ServletContextListener, HttpSessionListener,
 * ServletContext configuration, and active session tracking.
 */
@WebListener
public class AppListener implements ServletContextListener, HttpSessionListener {

    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger(0);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute("appName", "ShopSphere");
        ctx.setAttribute("activeSessions", ACTIVE_SESSIONS);

        // Seed default administrator if absent
        try {
            UserDAO userDAO = new UserDAO();
            if (!userDAO.exists("admin@shopsphere.com")) {
                userDAO.create("Administrator", "admin@shopsphere.com", "admin123", "ADMIN", "9876543210");
                ctx.log("ShopSphere: Admin user seeded successfully.");
            }
        } catch (Exception ex) {
            ctx.log("ShopSphere DB Notice: " + ex.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("ShopSphere application shut down cleanly.");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.incrementAndGet();
        se.getSession().getServletContext().setAttribute("activeSessionCount", count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.decrementAndGet();
        if (count < 0) count = 0;
        se.getSession().getServletContext().setAttribute("activeSessionCount", count);
    }

    public static int getActiveSessionsCount() {
        return Math.max(0, ACTIVE_SESSIONS.get());
    }
}

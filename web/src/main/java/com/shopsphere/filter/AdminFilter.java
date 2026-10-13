package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter protecting admin routes (/admin/*).
 * Satisfies RTU requirements: AdminFilter restricts access strictly to users with role 'ADMIN'.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Unauthenticated user -> redirect to login with redirect parameter
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getRequestURI());
            return;
        }

        if (!user.isAdmin()) {
            // Authenticated but non-admin -> HTTP 403 Forbidden
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrator role required.");
            return;
        }

        // User is authenticated administrator -> proceed
        chain.doFilter(request, response);
    }
}

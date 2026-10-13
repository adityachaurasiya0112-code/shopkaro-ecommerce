package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter protecting customer account routes.
 * Satisfies RTU requirements: AuthenticationFilter prevents unauthenticated access to personal data.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/cart",
        "/checkout",
        "/orders",
        "/order-details",
        "/wishlist",
        "/profile",
        "/review"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            String target = req.getRequestURI();
            if (req.getQueryString() != null) {
                target += "?" + req.getQueryString();
            }
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + java.net.URLEncoder.encode(target, java.nio.charset.StandardCharsets.UTF_8));
            return;
        }

        chain.doFilter(request, response);
    }
}

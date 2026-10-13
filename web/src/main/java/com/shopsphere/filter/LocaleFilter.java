package com.shopsphere.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.jstl.core.Config;

import java.io.IOException;
import java.util.Locale;

/**
 * Filter managing Locale and ResourceBundle selection for Internationalization.
 * Demonstrates RTU syllabus: i18n, Locale, JSTL FMT_LOCALE integration.
 */
@WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(true);

        String paramLang = req.getParameter("lang");
        String currentLang = (String) session.getAttribute("lang");

        if (paramLang != null && !paramLang.isBlank()) {
            currentLang = paramLang.equalsIgnoreCase("hi") ? "hi" : "en";
            session.setAttribute("lang", currentLang);
        } else if (currentLang == null) {
            currentLang = "en";
            session.setAttribute("lang", currentLang);
        }

        Locale locale = "hi".equalsIgnoreCase(currentLang) ? new Locale("hi", "IN") : Locale.ENGLISH;
        session.setAttribute("currentLocale", locale);
        Config.set(session, Config.FMT_LOCALE, locale);

        chain.doFilter(request, response);
    }
}

package com.shopsphere.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller demonstrating traditional JSP elements:
 * Declaration (<%! %>), Scriptlet (<% %>), Expression (<%= %>), and Comment (<%-- --%>).
 * Meets RTU syllabus requirements for JSP elements demonstration.
 */
@WebServlet("/demo/jsp")
public class JspDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("demoStudent", "RTU Advanced Java Scholar");
        req.setAttribute("demoCourse", "Advanced Java & Enterprise Computing");
        req.getRequestDispatcher("/jsp-demo.jsp").forward(req, resp);
    }
}

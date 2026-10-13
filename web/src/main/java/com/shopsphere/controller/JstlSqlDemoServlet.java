package com.shopsphere.controller;

import com.shopsphere.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller preparing datasource parameters for JSTL SQL demo (<sql:setDataSource>, <sql:query>).
 * Note: RTU guidelines specify that the main application strictly uses DAO/JDBC,
 * while JSTL SQL is shown on this isolated syllabus demonstration page.
 */
@WebServlet("/demo/sql")
public class JstlSqlDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("dbUrl", DBUtil.getUrl());
        req.setAttribute("dbUser", DBUtil.getUser());
        req.setAttribute("dbPassword", DBUtil.getPassword());
        req.getRequestDispatcher("/jstl-sql-demo.jsp").forward(req, resp);
    }
}

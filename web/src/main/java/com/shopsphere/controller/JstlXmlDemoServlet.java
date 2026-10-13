package com.shopsphere.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller demonstrating JSTL XML tags (x:parse, x:out, x:forEach).
 * Supplies a sample Product XML document for XPath parsing in JSP.
 */
@WebServlet("/demo/xml")
public class JstlXmlDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<catalog>\n" +
                "    <product id=\"101\">\n" +
                "        <name>Boat Airdopes 141 ANC</name>\n" +
                "        <brand>Boat</brand>\n" +
                "        <category>Electronics</category>\n" +
                "        <price>1499.00</price>\n" +
                "        <stock>45</stock>\n" +
                "    </product>\n" +
                "    <product id=\"102\">\n" +
                "        <name>Noise ColorFit Pro 4</name>\n" +
                "        <brand>Noise</brand>\n" +
                "        <category>Electronics</category>\n" +
                "        <price>2999.00</price>\n" +
                "        <stock>30</stock>\n" +
                "    </product>\n" +
                "    <product id=\"103\">\n" +
                "        <name>Clean Code Handbook</name>\n" +
                "        <brand>Pearson</brand>\n" +
                "        <category>Books</category>\n" +
                "        <price>990.00</price>\n" +
                "        <stock>20</stock>\n" +
                "    </product>\n" +
                "</catalog>";

        req.setAttribute("xmlCatalog", xmlData);
        req.getRequestDispatcher("/jstl-xml-demo.jsp").forward(req, resp);
    }
}

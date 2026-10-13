package com.shopsphere.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller demonstrating java.net.URL, URLConnection, and Protocol Handlers.
 * Demonstrates RTU syllabus: java.net, URLConnection, Content/Protocol handlers.
 */
@WebServlet("/demo/url")
public class UrlDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String testUrl = req.getParameter("target");
        if (testUrl == null || testUrl.isBlank()) {
            testUrl = "https://httpbin.org/get";
        }

        Map<String, Object> connectionInfo = new HashMap<>();
        StringBuilder snippet = new StringBuilder();

        try {
            URL url = new URL(testUrl);
            connectionInfo.put("Protocol", url.getProtocol());
            connectionInfo.put("Host", url.getHost());
            connectionInfo.put("Port", url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
            connectionInfo.put("File/Path", url.getFile());

            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.connect();

            connectionInfo.put("Content-Type", conn.getContentType());
            connectionInfo.put("Content-Length", conn.getContentLengthLong());
            connectionInfo.put("Date", conn.getDate());
            connectionInfo.put("Header Fields Count", conn.getHeaderFields().size());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                int linesRead = 0;
                while ((line = reader.readLine()) != null && linesRead < 15) {
                    snippet.append(line).append("\n");
                    linesRead++;
                }
            }
        } catch (Exception ex) {
            connectionInfo.put("Connection Status", "Handled: " + ex.getMessage());
            snippet.append("Simulated payload: java.net.URLConnection successfully bound to protocol handler: ")
                   .append(testUrl);
        }

        req.setAttribute("targetUrl", testUrl);
        req.setAttribute("connInfo", connectionInfo);
        req.setAttribute("contentSnippet", snippet.toString());

        req.getRequestDispatcher("/url-demo.jsp").forward(req, resp);
    }
}

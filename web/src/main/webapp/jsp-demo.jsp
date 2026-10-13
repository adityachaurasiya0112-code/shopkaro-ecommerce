<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%-- =========================================================================
     RTU Syllabus Demonstration: Traditional JSP Elements
     Demonstrates:
     1. Declaration tag (<%! ... %>)
     2. Scriptlet tag (<% ... %>)
     3. Expression tag (<%= ... %>)
     4. Comment tag (<%-- ... --%>)
     ========================================================================= --%>

<%!
    // 1. JSP DECLARATION: Class-level member variables and helper methods
    private int demoHitCounter = 0;

    public int squareNumber(int num) {
        return num * num;
    }

    public String formatGreeting(String studentName) {
        return "Welcome to RTU Advanced Java Practical, " + (studentName != null ? studentName : "Scholar") + "!";
    }
%>

<%
    // 2. JSP SCRIPTLET: Java code executed during the _jspService() method
    demoHitCounter++;
    java.util.Date timestamp = new java.util.Date();
    String student = (String) request.getAttribute("demoStudent");
    if (student == null) student = "Ayush Yash";
    int sampleValue = 7;
    int calculatedSquare = squareNumber(sampleValue);
%>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="card border-0 shadow-sm p-4 mb-4 bg-light">
        <span class="badge bg-warning text-dark text-uppercase fw-bold align-self-start mb-2">RTU Syllabus Topic</span>
        <h3 class="fw-bold mb-1">Traditional JSP Elements Demonstration</h3>
        <p class="text-muted small mb-0">
            Demonstrates Declaration (&lt;%! %&gt;), Scriptlet (&lt;% %&gt;), Expression (&lt;%= %&gt;), and JSP Server Comments (&lt;%-- --%&gt;) as requested in the university syllabus.
        </p>
    </div>

    <div class="row g-4">
        <!-- 1. Declaration Demo -->
        <div class="col-md-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-primary mb-3">1. JSP Declaration Tag (<code>&lt;%! ... %&gt;</code>)</h5>
                <p class="small text-muted">
                    Variables and methods declared here become instance members of the generated Servlet class (outside of <code>_jspService()</code>).
                </p>
                <div class="p-3 bg-light rounded-3 font-monospace small mb-3">
                    &lt;%! int demoHitCounter = 0; %&gt;<br>
                    &lt;%! public int squareNumber(int x) { return x * x; } %&gt;
                </div>
                <div class="alert alert-info py-2 small mb-0">
                    <strong>Square Method Result:</strong> squareNumber(<%= sampleValue %>) = <strong><%= calculatedSquare %></strong>
                </div>
            </div>
        </div>

        <!-- 2. Scriptlet Demo -->
        <div class="col-md-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-success mb-3">2. JSP Scriptlet Tag (<code>&lt;% ... %&gt;</code>)</h5>
                <p class="small text-muted">
                    Java statements placed inside scriptlets run inside the <code>_jspService(HttpServletRequest, HttpServletResponse)</code> method.
                </p>
                <div class="p-3 bg-light rounded-3 font-monospace small mb-3">
                    &lt;% demoHitCounter++; %&gt;<br>
                    &lt;% Date timestamp = new java.util.Date(); %&gt;
                </div>
                <div class="alert alert-success py-2 small mb-0">
                    <strong>Current Execution Hit:</strong> <%= demoHitCounter %><br>
                    <strong>Timestamp:</strong> <%= timestamp %>
                </div>
            </div>
        </div>

        <!-- 3. Expression Demo -->
        <div class="col-md-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-danger mb-3">3. JSP Expression Tag (<code>&lt;%= ... %&gt;</code>)</h5>
                <p class="small text-muted">
                    Directly evaluates a Java expression, converts the result to String, and outputs it to the HTTP response stream (without needing a semicolon).
                </p>
                <div class="p-3 bg-light rounded-3 font-monospace small mb-3">
                    &lt;%= formatGreeting(student) %&gt;
                </div>
                <div class="alert alert-danger py-2 small mb-0">
                    <strong>Evaluated Expression:</strong> <%= formatGreeting(student) %>
                </div>
            </div>
        </div>

        <!-- 4. JSP Comments Demo -->
        <div class="col-md-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-secondary mb-3">4. JSP Server-side Comment (<code>&lt;%-- ... --%&gt;</code>)</h5>
                <p class="small text-muted">
                    Unlike standard HTML comments <code>&lt;!-- --&gt;</code>, JSP comments are stripped out during servlet translation and <strong>never sent to the client browser</strong>.
                </p>
                <div class="p-3 bg-light rounded-3 font-monospace small mb-3">
                    &lt;%-- Secret server-side developer note --%&gt;
                </div>
                <div class="alert alert-secondary py-2 small mb-0">
                    <i class="bi bi-shield-check text-success me-1"></i> Checked: Inspect "View Page Source" to verify JSP comments are never sent over HTTP!
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

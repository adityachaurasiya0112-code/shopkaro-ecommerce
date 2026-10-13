<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="card border-0 shadow-sm p-4 mb-4 bg-light">
        <span class="badge bg-info text-dark text-uppercase fw-bold align-self-start mb-2">RTU Syllabus Topic</span>
        <h3 class="fw-bold mb-1">JNDI Naming and Directory Interface Demonstration</h3>
        <p class="text-muted small mb-0">
            Demonstrates Java Naming and Directory Interface (JNDI), <code>javax.naming.InitialContext</code>, and resource lookup mechanisms in J2EE / Jakarta EE containers.
        </p>
    </div>

    <div class="row g-4">
        <!-- Concepts Card -->
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-dark mb-3"><i class="bi bi-diagram-2 text-primary me-2"></i>How JNDI Works in J2EE</h5>
                <p class="small text-secondary leading-relaxed">
                    JNDI provides an abstraction layer between enterprise applications and container-managed resources such as JDBC DataSources, JMS Queues, and Mail sessions.
                </p>
                <div class="bg-light p-3 rounded-3 font-monospace small mb-3">
                    // 1. Obtain Container Initial Context<br>
                    Context ctx = new InitialContext();<br><br>
                    // 2. Lookup Resource in java:comp/env<br>
                    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ShopSphereDB");
                </div>
                <div class="small text-muted">
                    <strong>Benefit:</strong> The application does not hardcode database hostnames or passwords; instead, Apache Tomcat's <code>context.xml</code> manages the connection pool.
                </div>
            </div>
        </div>

        <!-- Live Runtime Status Card -->
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-dark mb-3"><i class="bi bi-cpu text-success me-2"></i>Live Runtime Context Inspection</h5>
                <div class="list-group list-group-flush small">
                    <c:forEach var="entry" items="${jndiResults}">
                        <div class="list-group-item px-0 py-2">
                            <span class="fw-bold text-dark d-block"><c:out value="${entry.key}"/>:</span>
                            <span class="text-muted"><c:out value="${entry.value}"/></span>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

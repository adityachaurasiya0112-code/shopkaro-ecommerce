<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

</main>

<footer class="bg-dark text-secondary py-5 mt-5 border-top border-secondary">
    <div class="container">
        <div class="row g-4 mb-4">
            <div class="col-md-4">
                <h5 class="text-white d-flex align-items-center gap-2 mb-3">
                    <i class="bi bi-shop-window text-warning"></i> ShopSphere
                </h5>
                <p class="small text-secondary mb-3">
                    A complete full-stack E-Commerce management platform built for Rajasthan Technical University (RTU) Advanced Java / Enterprise Java practical curriculum.
                </p>
                <div class="small text-muted">
                    <span class="badge bg-secondary me-1"><i class="bi bi-broadcast me-1"></i><fmt:message key="footer.active_sessions"/>: <c:out value="${applicationScope.activeSessionCount != null ? applicationScope.activeSessionCount : 1}"/></span>
                    <span class="badge bg-secondary">Apache Tomcat 11</span>
                    <span class="badge bg-secondary">Jakarta Servlet 6.1</span>
                </div>
            </div>
            <div class="col-md-2 col-6">
                <h6 class="text-white mb-3">Quick Links</h6>
                <ul class="list-unstyled small">
                    <li class="mb-2"><a href="${ctx}/" class="text-secondary text-decoration-none hover-white">Home</a></li>
                    <li class="mb-2"><a href="${ctx}/products" class="text-secondary text-decoration-none hover-white">All Products</a></li>
                    <li class="mb-2"><a href="${ctx}/cart" class="text-secondary text-decoration-none hover-white">Cart</a></li>
                    <li class="mb-2"><a href="${ctx}/wishlist" class="text-secondary text-decoration-none hover-white">Wishlist</a></li>
                    <li class="mb-2"><a href="${ctx}/orders" class="text-secondary text-decoration-none hover-white">Track Orders</a></li>
                </ul>
            </div>
            <div class="col-md-3 col-6">
                <h6 class="text-white mb-3">RTU Syllabus Demos</h6>
                <ul class="list-unstyled small">
                    <li class="mb-2"><a href="${ctx}/demo/jsp" class="text-secondary text-decoration-none hover-white">JSP Elements (<%! %>, <%= %>)</a></li>
                    <li class="mb-2"><a href="${ctx}/demo/xml" class="text-secondary text-decoration-none hover-white">JSTL XML Tag Parsing</a></li>
                    <li class="mb-2"><a href="${ctx}/demo/sql" class="text-secondary text-decoration-none hover-white">JSTL SQL Tag Demo</a></li>
                    <li class="mb-2"><a href="${ctx}/demo/jndi" class="text-secondary text-decoration-none hover-white">JNDI Directory Lookup</a></li>
                    <li class="mb-2"><a href="${ctx}/demo/url" class="text-secondary text-decoration-none hover-white">URLConnection Protocol Demo</a></li>
                </ul>
            </div>
            <div class="col-md-3">
                <h6 class="text-white mb-3">Architecture & Security</h6>
                <ul class="list-unstyled small text-secondary">
                    <li class="mb-2"><i class="bi bi-shield-check text-success me-2"></i>PreparedStatements Only (No SQL Injection)</li>
                    <li class="mb-2"><i class="bi bi-key text-warning me-2"></i>PBKDF2 Password Hashing</li>
                    <li class="mb-2"><i class="bi bi-calculator text-info me-2"></i>Server-side Order Total Computation</li>
                    <li class="mb-2"><i class="bi bi-lock text-danger me-2"></i>AdminFilter & AuthenticationFilter</li>
                </ul>
            </div>
        </div>

        <hr class="border-secondary my-4">

        <div class="d-flex flex-column flex-md-row justify-content-between align-items-center small">
            <div>
                &copy; 2026 <strong>ShopSphere</strong>. RTU Advanced Java Practical Project.
            </div>
            <div class="mt-2 mt-md-0">
                <span class="text-secondary">Designed with JSP, Servlet, JDBC, JSTL, EL, Bootstrap 5, Sockets, Swing & RMI</span>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap 5.3.3 Bundle with Popper JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

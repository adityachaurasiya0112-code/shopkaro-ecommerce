<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-5 col-lg-4">
            <div class="card border-0 shadow-sm p-4">
                <div class="text-center mb-4">
                    <div class="rounded-circle bg-primary bg-opacity-10 text-primary mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 55px; height: 55px;">
                        <i class="bi bi-person-lock fs-3"></i>
                    </div>
                    <h4 class="fw-bold"><fmt:message key="nav.login"/></h4>
                    <p class="text-muted small">Sign in to your ShopSphere account</p>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 small"><i class="bi bi-exclamation-triangle me-1"></i><c:out value="${error}"/></div>
                </c:if>
                <c:if test="${not empty param.logout}">
                    <div class="alert alert-info py-2 small"><i class="bi bi-info-circle me-1"></i>You have been logged out successfully.</div>
                </c:if>

                <form method="post" action="${ctx}/login">
                    <c:if test="${not empty param.redirect}">
                        <input type="hidden" name="redirect" value="<c:out value='${param.redirect}'/>">
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Email Address</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light"><i class="bi bi-envelope"></i></span>
                            <input type="email" name="email" id="emailInput" class="form-control" placeholder="name@example.com" value="<c:out value='${email}'/>" required autofocus>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Password</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light"><i class="bi bi-key"></i></span>
                            <input type="password" name="password" id="passwordInput" class="form-control" placeholder="Enter password" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mb-3">
                        <fmt:message key="nav.login"/>
                    </button>
                </form>

                <!-- Quick Test Credentials Pill Helpers for Viva/Testing -->
                <div class="p-3 bg-light rounded-3 text-center small mb-3 border">
                    <div class="text-muted fw-bold mb-2">⚡ Quick Test Credentials</div>
                    <div class="d-flex justify-content-center gap-2">
                        <button type="button" class="btn btn-xs btn-outline-dark" onclick="fillCreds('admin@shopsphere.com', 'admin123')">
                            Admin Login
                        </button>
                        <button type="button" class="btn btn-xs btn-outline-primary" onclick="fillCreds('ayush@shopsphere.com', 'user123')">
                            Customer Login
                        </button>
                    </div>
                </div>

                <div class="text-center small text-muted">
                    Don't have an account? <a href="${ctx}/register" class="fw-semibold text-decoration-none">Register here</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function fillCreds(email, pass) {
    document.getElementById('emailInput').value = email;
    document.getElementById('passwordInput').value = pass;
}
</script>

<%@ include file="/fragments/footer.jsp" %>

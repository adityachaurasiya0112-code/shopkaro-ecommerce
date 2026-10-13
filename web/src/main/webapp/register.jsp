<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card border-0 shadow-sm p-4">
                <div class="text-center mb-4">
                    <div class="rounded-circle bg-success bg-opacity-10 text-success mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 55px; height: 55px;">
                        <i class="bi bi-person-plus-fill fs-3"></i>
                    </div>
                    <h4 class="fw-bold"><fmt:message key="nav.register"/></h4>
                    <p class="text-muted small">Create your ShopSphere customer account</p>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 small"><i class="bi bi-exclamation-triangle me-1"></i><c:out value="${error}"/></div>
                </c:if>

                <form method="post" action="${ctx}/register">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Full Name</label>
                        <input type="text" name="name" class="form-control" placeholder="e.g. Ayush Yash" value="<c:out value='${name}'/>" required autofocus>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Email Address</label>
                        <input type="email" name="email" class="form-control" placeholder="name@example.com" value="<c:out value='${email}'/>" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Mobile Number</label>
                        <input type="tel" name="mobile" class="form-control" placeholder="10-digit mobile" value="<c:out value='${mobile}'/>">
                    </div>

                    <div class="row g-2 mb-3">
                        <div class="col-sm-6">
                            <label class="form-label small fw-semibold">Password</label>
                            <input type="password" name="password" class="form-control" placeholder="Min 6 characters" minlength="6" required>
                        </div>
                        <div class="col-sm-6">
                            <label class="form-label small fw-semibold">Confirm Password</label>
                            <input type="password" name="confirmPassword" class="form-control" placeholder="Re-enter password" minlength="6" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-success w-100 py-2 fw-semibold mb-3">
                        Create Account
                    </button>
                </form>

                <div class="text-center small text-muted">
                    Already registered? <a href="${ctx}/login" class="fw-semibold text-decoration-none">Login here</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

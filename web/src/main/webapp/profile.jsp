<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <h3 class="fw-bold mb-4"><i class="bi bi-person-badge text-primary me-2"></i>My Profile</h3>

    <!-- Feedback Alerts -->
    <c:if test="${not empty sessionScope.profileSuccess}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle me-2"></i><c:out value="${sessionScope.profileSuccess}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            <c:remove var="profileSuccess" scope="session"/>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.profileError}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle me-2"></i><c:out value="${sessionScope.profileError}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            <c:remove var="profileError" scope="session"/>
        </div>
    </c:if>

    <div class="row g-4">
        <!-- User Info & Change Password -->
        <div class="col-lg-5">
            <!-- Account Card -->
            <div class="card border-0 shadow-sm p-4 mb-4">
                <div class="d-flex align-items-center gap-3 mb-3">
                    <div class="rounded-circle bg-primary bg-opacity-10 text-primary d-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                        <i class="bi bi-person-fill fs-2"></i>
                    </div>
                    <div>
                        <h5 class="fw-bold mb-0"><c:out value="${profileUser.name}"/></h5>
                        <small class="text-muted"><c:out value="${profileUser.email}"/></small>
                    </div>
                </div>

                <ul class="list-group list-group-flush small mb-0">
                    <li class="list-group-item px-0 d-flex justify-content-between">
                        <span class="text-muted">Account Role:</span>
                        <span class="badge bg-primary"><c:out value="${profileUser.role}"/></span>
                    </li>
                    <li class="list-group-item px-0 d-flex justify-content-between">
                        <span class="text-muted">Mobile Number:</span>
                        <span class="fw-semibold"><c:out value="${not empty profileUser.mobile ? profileUser.mobile : 'Not Provided'}"/></span>
                    </li>
                    <li class="list-group-item px-0 d-flex justify-content-between">
                        <span class="text-muted">Member Since:</span>
                        <span class="fw-semibold"><c:out value="${profileUser.createdAt}"/></span>
                    </li>
                </ul>
            </div>

            <!-- Change Password Card -->
            <div class="card border-0 shadow-sm p-4">
                <h6 class="fw-bold mb-3"><i class="bi bi-shield-lock text-warning me-2"></i>Change Password</h6>
                <form method="post" action="${ctx}/profile">
                    <input type="hidden" name="action" value="changePassword">
                    <div class="mb-2">
                        <label class="form-label small">New Password</label>
                        <input type="password" name="newPassword" class="form-control form-control-sm" required minlength="6">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small">Confirm New Password</label>
                        <input type="password" name="confirmPassword" class="form-control form-control-sm" required minlength="6">
                    </div>
                    <button type="submit" class="btn btn-outline-primary btn-sm w-100">Update Password</button>
                </form>
            </div>
        </div>

        <!-- Address Book -->
        <div class="col-lg-7">
            <div class="card border-0 shadow-sm p-4 mb-4">
                <h5 class="fw-bold mb-3"><i class="bi bi-house-door text-danger me-2"></i>Saved Delivery Addresses</h5>

                <c:choose>
                    <c:when test="${not empty addresses}">
                        <div class="d-flex flex-column gap-3 mb-4">
                            <c:forEach var="addr" items="${addresses}">
                                <div class="p-3 border rounded-3 bg-light d-flex justify-content-between align-items-start">
                                    <div>
                                        <span class="badge bg-secondary mb-1"><c:out value="${addr.addressType}"/></span>
                                        <div class="fw-semibold text-dark"><c:out value="${addr.addressLine}"/></div>
                                        <small class="text-muted"><c:out value="${addr.city}"/>, <c:out value="${addr.state}"/> - <c:out value="${addr.pincode}"/></small>
                                    </div>
                                    <form method="post" action="${ctx}/profile">
                                        <input type="hidden" name="action" value="deleteAddress">
                                        <input type="hidden" name="addressId" value="${addr.addressId}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('Delete this address?');">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted small mb-4">No addresses saved yet.</p>
                    </c:otherwise>
                </c:choose>

                <!-- Add New Address Form -->
                <h6 class="fw-bold mb-3">Add New Address</h6>
                <form method="post" action="${ctx}/profile">
                    <input type="hidden" name="action" value="addAddress">
                    <div class="row g-2">
                        <div class="col-12">
                            <input type="text" name="addressLine" class="form-control form-control-sm" placeholder="Street / Flat / Colony" required>
                        </div>
                        <div class="col-md-4">
                            <input type="text" name="city" class="form-control form-control-sm" placeholder="City" required>
                        </div>
                        <div class="col-md-4">
                            <input type="text" name="state" class="form-control form-control-sm" placeholder="State" required>
                        </div>
                        <div class="col-md-4">
                            <input type="text" name="pincode" class="form-control form-control-sm" placeholder="PIN Code" required>
                        </div>
                        <div class="col-md-6">
                            <select name="addressType" class="form-select form-select-sm">
                                <option value="HOME">Home</option>
                                <option value="OFFICE">Office</option>
                                <option value="HOSTEL">Hostel</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <button type="submit" class="btn btn-primary btn-sm w-100">Save Address</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

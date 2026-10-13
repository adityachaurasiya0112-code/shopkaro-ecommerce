<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="row g-4">
        <!-- Sidebar -->
        <div class="col-lg-3">
            <%@ include file="/fragments/sidebar.jsp" %>
        </div>

        <!-- Coupon Management -->
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold mb-0"><i class="bi bi-ticket-perforated text-primary me-2"></i>Coupon Management</h3>
                    <p class="text-muted small mb-0">Configure promotional discount codes</p>
                </div>
            </div>

            <!-- Add Coupon Form Card -->
            <div class="card border-0 shadow-sm p-4 mb-4">
                <h6 class="fw-bold mb-3">Create New Promotional Coupon</h6>
                <form method="post" action="${ctx}/admin/coupons" class="row g-2">
                    <div class="col-md-3">
                        <label class="form-label small fw-bold">Coupon Code</label>
                        <input type="text" name="code" class="form-control form-control-sm text-uppercase" placeholder="e.g. DIWALI50" required>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small fw-bold">Discount Type</label>
                        <select name="discountType" class="form-select form-select-sm">
                            <option value="PERCENT">Percentage (%)</option>
                            <option value="FLAT">Flat Amount (₹)</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small fw-bold">Discount Value</label>
                        <input type="number" step="0.01" name="discountValue" class="form-control form-control-sm" placeholder="e.g. 20" required>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small fw-bold">Min Order (₹)</label>
                        <input type="number" step="0.01" name="minimumOrder" class="form-control form-control-sm" placeholder="e.g. 500" value="0">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small fw-bold">Max Discount (₹)</label>
                        <input type="number" step="0.01" name="maximumDiscount" class="form-control form-control-sm" placeholder="e.g. 200" value="500">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-bold">Expiry Date</label>
                        <input type="date" name="expiryDate" class="form-control form-control-sm" value="2030-12-31">
                    </div>
                    <div class="col-md-3 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary btn-sm w-100">Create Coupon</button>
                    </div>
                </form>
            </div>

            <!-- Existing Coupons Table -->
            <div class="card border-0 shadow-sm overflow-hidden">
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>Code</th>
                                <th>Type</th>
                                <th>Discount</th>
                                <th>Min Order</th>
                                <th>Max Discount</th>
                                <th>Expiry Date</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cp" items="${coupons}">
                                <tr>
                                    <td><strong class="text-primary"><c:out value="${cp.code}"/></strong></td>
                                    <td><span class="badge bg-secondary"><c:out value="${cp.discountType}"/></span></td>
                                    <td class="fw-bold">${cp.discountType == 'PERCENT' ? cp.discountValue : '₹'} ${cp.discountType == 'PERCENT' ? '%' : cp.discountValue}</td>
                                    <td>₹${cp.minimumOrder}</td>
                                    <td>₹${cp.maximumDiscount}</td>
                                    <td class="small text-muted"><c:out value="${cp.expiryDate}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${cp.status == 1}">
                                                <span class="badge bg-success-subtle text-success">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Inactive</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

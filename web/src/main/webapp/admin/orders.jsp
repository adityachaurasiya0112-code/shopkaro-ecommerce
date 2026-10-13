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

        <!-- Orders Management -->
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold mb-0"><i class="bi bi-cart-check text-primary me-2"></i>Order Management</h3>
                    <p class="text-muted small mb-0">Track and update lifecycle status for customer orders</p>
                </div>
                <span class="badge bg-secondary">${orders.size()} Orders Total</span>
            </div>

            <div class="card border-0 shadow-sm overflow-hidden">
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Items</th>
                                <th>Total</th>
                                <th>Payment</th>
                                <th>Current Status</th>
                                <th>Update Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${orders}">
                                <tr>
                                    <td>
                                        <strong>#<c:out value="${o.id}"/></strong>
                                        <small class="text-muted d-block">${o.createdAt}</small>
                                    </td>
                                    <td>
                                        <div class="fw-bold small"><c:out value="${o.customerName}"/></div>
                                        <small class="text-muted"><c:out value="${o.customerEmail}"/></small>
                                    </td>
                                    <td>
                                        <span class="badge bg-light text-dark">${o.itemsCount} Items</span>
                                    </td>
                                    <td class="fw-semibold text-primary">₹${o.total}</td>
                                    <td>
                                        <small class="d-block"><c:out value="${o.paymentMethod}"/></small>
                                        <span class="badge bg-success-subtle text-success"><c:out value="${o.paymentStatus}"/></span>
                                    </td>
                                    <td>
                                        <span class="badge bg-primary"><c:out value="${o.status}"/></span>
                                    </td>
                                    <td>
                                        <form method="post" action="${ctx}/admin/orders" class="d-flex align-items-center gap-1">
                                            <input type="hidden" name="action" value="updateStatus">
                                            <input type="hidden" name="orderId" value="${o.id}">
                                            <select name="orderStatus" class="form-select form-select-sm" style="min-width: 120px;" onchange="this.form.submit()">
                                                <option value="PLACED" ${o.status == 'PLACED' ? 'selected' : ''}>PLACED</option>
                                                <option value="CONFIRMED" ${o.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                                <option value="SHIPPED" ${o.status == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                                <option value="DELIVERED" ${o.status == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                                <option value="CANCELLED" ${o.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                            </select>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty orders}">
                                <tr><td colspan="7" class="text-center text-muted py-4">No customer orders recorded.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

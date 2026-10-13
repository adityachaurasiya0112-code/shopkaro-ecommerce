<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <a href="${ctx}/orders" class="text-decoration-none small text-muted"><i class="bi bi-arrow-left me-1"></i>Back to My Orders</a>
            <h3 class="fw-bold mt-1 mb-0">Order Details <span class="text-primary">#<c:out value="${order.id}"/></span></h3>
        </div>
        <button class="btn btn-outline-secondary btn-sm" onclick="window.print()">
            <i class="bi bi-printer me-1"></i>Print Invoice
        </button>
    </div>

    <!-- Status Tracking Timeline Card -->
    <div class="card border-0 shadow-sm p-4 mb-4">
        <h6 class="fw-bold text-uppercase text-muted small mb-3">Order Status Progression</h6>
        <div class="row text-center position-relative">
            <div class="col-3">
                <div class="rounded-circle bg-success text-white mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 40px; height: 40px;">
                    <i class="bi bi-cart-check"></i>
                </div>
                <div class="small fw-bold">Placed</div>
                <small class="text-muted d-block">${order.createdAt}</small>
            </div>
            <div class="col-3">
                <div class="rounded-circle ${order.status == 'CONFIRMED' || order.status == 'SHIPPED' || order.status == 'DELIVERED' ? 'bg-success text-white' : 'bg-light text-muted'} mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 40px; height: 40px;">
                    <i class="bi bi-box-seam"></i>
                </div>
                <div class="small fw-bold">Confirmed</div>
            </div>
            <div class="col-3">
                <div class="rounded-circle ${order.status == 'SHIPPED' || order.status == 'DELIVERED' ? 'bg-success text-white' : 'bg-light text-muted'} mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 40px; height: 40px;">
                    <i class="bi bi-truck"></i>
                </div>
                <div class="small fw-bold">Shipped</div>
            </div>
            <div class="col-3">
                <div class="rounded-circle ${order.status == 'DELIVERED' ? 'bg-success text-white' : 'bg-light text-muted'} mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 40px; height: 40px;">
                    <i class="bi bi-house-check"></i>
                </div>
                <div class="small fw-bold">Delivered</div>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <!-- Items Table -->
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm overflow-hidden mb-4">
                <div class="card-header bg-white py-3">
                    <h6 class="fw-bold mb-0">Purchased Items (${order.items.size()})</h6>
                </div>
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>Item</th>
                                <th class="text-center">Price</th>
                                <th class="text-center">Quantity</th>
                                <th class="text-end">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <img src="${not empty item.imageUrl ? item.imageUrl : 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'}" 
                                                 class="rounded-2" style="width: 50px; height: 50px; object-fit: cover;" alt="img">
                                            <div>
                                                <h6 class="mb-0 fw-bold small"><c:out value="${item.productName}"/></h6>
                                                <small class="text-muted"><c:out value="${item.brand}"/></small>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="text-center">₹${item.price}</td>
                                    <td class="text-center">${item.quantity}</td>
                                    <td class="text-end fw-semibold">₹${item.subtotal}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Address & Payment Meta -->
        <div class="col-lg-4">
            <!-- Shipping Info -->
            <div class="card border-0 shadow-sm p-4 mb-4">
                <h6 class="fw-bold mb-3"><i class="bi bi-geo-alt text-danger me-2"></i>Shipping Address</h6>
                <c:choose>
                    <c:when test="${not empty order.addressText}">
                        <p class="text-secondary small mb-0"><c:out value="${order.addressText}"/></p>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted small mb-0">Standard Delivery (Default Address)</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Payment Summary -->
            <div class="card border-0 shadow-sm p-4">
                <h6 class="fw-bold mb-3"><i class="bi bi-credit-card text-primary me-2"></i>Payment Details</h6>
                <div class="d-flex justify-content-between mb-2 small">
                    <span class="text-muted">Payment Method:</span>
                    <span class="fw-bold"><c:out value="${order.paymentMethod}"/></span>
                </div>
                <div class="d-flex justify-content-between mb-2 small">
                    <span class="text-muted">Payment Status:</span>
                    <span class="badge bg-success-subtle text-success"><c:out value="${order.paymentStatus}"/></span>
                </div>
                <c:if test="${order.discount > 0}">
                    <div class="d-flex justify-content-between mb-2 small text-success">
                        <span>Coupon Discount:</span>
                        <span>-₹${order.discount}</span>
                    </div>
                </c:if>
                <hr class="my-2">
                <div class="d-flex justify-content-between fw-bold fs-5 text-dark">
                    <span>Total Paid:</span>
                    <span class="text-primary">₹${order.total}</span>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

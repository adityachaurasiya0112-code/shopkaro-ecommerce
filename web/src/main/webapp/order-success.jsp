<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5 text-center">
    <div class="card border-0 shadow-sm p-5 mx-auto" style="max-width: 600px;">
        <div class="rounded-circle bg-success bg-opacity-10 text-success mx-auto d-flex align-items-center justify-content-center mb-4" style="width: 80px; height: 80px;">
            <i class="bi bi-check-lg display-4"></i>
        </div>

        <h2 class="fw-bold text-dark mb-2">Order Confirmed!</h2>
        <p class="text-muted mb-4">
            Thank you for shopping with ShopSphere. Your payment has been processed and your order is placed.
        </p>

        <div class="bg-light p-3 rounded-3 mb-4 text-start">
            <div class="d-flex justify-content-between mb-2">
                <span class="text-secondary">Order Reference:</span>
                <span class="fw-bold text-primary">#<c:out value="${param.orderId}"/></span>
            </div>
            <div class="d-flex justify-content-between mb-2">
                <span class="text-secondary">Status:</span>
                <span class="badge bg-info text-dark">PLACED</span>
            </div>
            <div class="d-flex justify-content-between">
                <span class="text-secondary">Estimated Delivery:</span>
                <span class="fw-semibold text-dark">Within 3-5 Business Days</span>
            </div>
        </div>

        <div class="d-flex flex-wrap justify-content-center gap-3">
            <a href="${ctx}/order-details?id=${param.orderId}" class="btn btn-primary px-4">
                <i class="bi bi-eye me-1"></i> View Order Details
            </a>
            <a href="${ctx}/products" class="btn btn-outline-secondary px-4">
                <i class="bi bi-bag me-1"></i> Continue Shopping
            </a>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

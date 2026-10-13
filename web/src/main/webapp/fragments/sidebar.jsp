<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="card shadow-sm border-0 mb-4">
    <div class="card-body p-3">
        <h6 class="text-uppercase text-muted fw-bold small mb-3 px-2">Admin Navigation</h6>
        <div class="list-group list-group-flush rounded-3">
            <a href="${ctx}/admin/dashboard" class="list-group-item list-group-item-action border-0 rounded-2 mb-1 d-flex align-items-center gap-2 ${pageContext.request.servletPath == '/admin/dashboard.jsp' || pageContext.request.servletPath == '/admin/dashboard' ? 'active bg-primary' : ''}">
                <i class="bi bi-speedometer2"></i> Dashboard Overview
            </a>
            <a href="${ctx}/admin/products" class="list-group-item list-group-item-action border-0 rounded-2 mb-1 d-flex align-items-center gap-2 ${pageContext.request.servletPath == '/admin/products.jsp' ? 'active bg-primary' : ''}">
                <i class="bi bi-box-seam"></i> Manage Products
            </a>
            <a href="${ctx}/admin/categories" class="list-group-item list-group-item-action border-0 rounded-2 mb-1 d-flex align-items-center gap-2 ${pageContext.request.servletPath == '/admin/categories.jsp' ? 'active bg-primary' : ''}">
                <i class="bi bi-tags"></i> Manage Categories
            </a>
            <a href="${ctx}/admin/orders" class="list-group-item list-group-item-action border-0 rounded-2 mb-1 d-flex align-items-center gap-2 ${pageContext.request.servletPath == '/admin/orders.jsp' ? 'active bg-primary' : ''}">
                <i class="bi bi-cart-check"></i> Manage Orders
            </a>
            <a href="${ctx}/admin/users" class="list-group-item list-group-item-action border-0 rounded-2 mb-1 d-flex align-items-center gap-2 ${pageContext.request.servletPath == '/admin/users.jsp' ? 'active bg-primary' : ''}">
                <i class="bi bi-people"></i> Manage Customers
            </a>
            <a href="${ctx}/admin/coupons" class="list-group-item list-group-item-action border-0 rounded-2 mb-1 d-flex align-items-center gap-2 ${pageContext.request.servletPath == '/admin/coupons.jsp' ? 'active bg-primary' : ''}">
                <i class="bi bi-ticket-perforated"></i> Manage Coupons
            </a>
            <hr class="my-2 text-muted">
            <a href="${ctx}/products" class="list-group-item list-group-item-action border-0 rounded-2 text-secondary d-flex align-items-center gap-2">
                <i class="bi bi-arrow-left-circle"></i> Back to Storefront
            </a>
        </div>
    </div>
</div>

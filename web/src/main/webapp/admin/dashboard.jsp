<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="row g-4">
        <!-- Admin Sidebar -->
        <div class="col-lg-3">
            <%@ include file="/fragments/sidebar.jsp" %>
        </div>

        <!-- Main Dashboard Content -->
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold mb-1"><i class="bi bi-speedometer2 text-primary me-2"></i>Admin Dashboard</h3>
                    <p class="text-muted small mb-0">Overview of ShopSphere store metrics and inventory health</p>
                </div>
                <span class="badge bg-primary px-3 py-2">Tomcat 11 Container</span>
            </div>

            <!-- Stats Metric Cards -->
            <div class="row g-3 mb-4">
                <div class="col-sm-6 col-xl-3">
                    <div class="card border-0 shadow-sm p-3 h-100 bg-white">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="text-muted small text-uppercase fw-bold">Total Sales</span>
                            <div class="rounded-circle bg-success bg-opacity-10 text-success p-2">
                                <i class="bi bi-currency-rupee fs-5"></i>
                            </div>
                        </div>
                        <h4 class="fw-bold text-dark mb-0">₹${stats.totalRevenue}</h4>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="card border-0 shadow-sm p-3 h-100 bg-white">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="text-muted small text-uppercase fw-bold">Total Orders</span>
                            <div class="rounded-circle bg-primary bg-opacity-10 text-primary p-2">
                                <i class="bi bi-bag-check fs-5"></i>
                            </div>
                        </div>
                        <h4 class="fw-bold text-dark mb-0">${stats.totalOrders}</h4>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="card border-0 shadow-sm p-3 h-100 bg-white">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="text-muted small text-uppercase fw-bold">Customers</span>
                            <div class="rounded-circle bg-info bg-opacity-10 text-info p-2">
                                <i class="bi bi-people fs-5"></i>
                            </div>
                        </div>
                        <h4 class="fw-bold text-dark mb-0">${stats.totalCustomers}</h4>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="card border-0 shadow-sm p-3 h-100 bg-white">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="text-muted small text-uppercase fw-bold">Low Stock Items</span>
                            <div class="rounded-circle bg-danger bg-opacity-10 text-danger p-2">
                                <i class="bi bi-exclamation-triangle fs-5"></i>
                            </div>
                        </div>
                        <h4 class="fw-bold text-danger mb-0">${stats.lowStockCount}</h4>
                    </div>
                </div>
            </div>

            <!-- Recent Orders Section -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                    <h6 class="fw-bold mb-0">Recent Orders</h6>
                    <a href="${ctx}/admin/orders" class="small text-decoration-none">View All Orders</a>
                </div>
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${recentOrders}">
                                <tr>
                                    <td><strong>#<c:out value="${o.id}"/></strong></td>
                                    <td><c:out value="${o.customerName}"/></td>
                                    <td class="fw-semibold">₹${o.total}</td>
                                    <td><span class="badge bg-info-subtle text-info"><c:out value="${o.status}"/></span></td>
                                    <td class="small text-muted"><c:out value="${o.createdAt}"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty recentOrders}">
                                <tr><td colspan="5" class="text-center text-muted py-3">No orders placed yet.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Low Stock Inventory Warnings -->
            <div class="card border-0 shadow-sm">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                    <h6 class="fw-bold text-danger mb-0"><i class="bi bi-bell-fill me-1"></i>Low-Stock Inventory Warnings (Stock &le; 15)</h6>
                    <a href="${ctx}/admin/products" class="small text-decoration-none">Manage Inventory</a>
                </div>
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>ID</th>
                                <th>Product</th>
                                <th>Category</th>
                                <th>Stock Left</th>
                                <th>Quick Restock</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${lowStockProducts}">
                                <tr>
                                    <td>${p.id}</td>
                                    <td><strong><c:out value="${p.name}"/></strong></td>
                                    <td><c:out value="${p.categoryName}"/></td>
                                    <td><span class="badge bg-danger">${p.stock} units</span></td>
                                    <td>
                                        <form method="post" action="${ctx}/admin/products" class="d-flex align-items-center gap-1">
                                            <input type="hidden" name="action" value="setStock">
                                            <input type="hidden" name="id" value="${p.id}">
                                            <input type="number" name="stock" value="${p.stock + 20}" class="form-control form-control-sm" style="width: 75px;">
                                            <button type="submit" class="btn btn-sm btn-outline-success">Restock</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty lowStockProducts}">
                                <tr><td colspan="5" class="text-center text-success py-3"><i class="bi bi-check-circle me-1"></i>All inventory levels are healthy!</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

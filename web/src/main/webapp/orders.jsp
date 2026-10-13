<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="fw-bold mb-0"><i class="bi bi-box2-heart text-primary me-2"></i><fmt:message key="orders.title"/></h3>
        <a href="${ctx}/products" class="btn btn-outline-primary btn-sm">Explore More Products</a>
    </div>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="card border-0 shadow-sm text-center py-5 my-4">
                <i class="bi bi-bag-x text-muted display-2 mb-3"></i>
                <h4 class="fw-bold"><fmt:message key="orders.no_orders"/></h4>
                <p class="text-muted">Start filling your wardrobe and gadgets cart today.</p>
                <div>
                    <a href="${ctx}/products" class="btn btn-primary px-4 py-2"><fmt:message key="home.shop_now"/></a>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="d-flex flex-column gap-3">
                <c:forEach var="o" items="${orders}">
                    <div class="card border-0 shadow-sm p-4">
                        <div class="row align-items-center g-3">
                            <div class="col-md-3">
                                <span class="text-muted small d-block"><fmt:message key="orders.order_id"/></span>
                                <h5 class="fw-bold text-dark mb-0">#<c:out value="${o.id}"/></h5>
                                <small class="text-muted"><c:out value="${o.createdAt}"/></small>
                            </div>

                            <div class="col-md-3">
                                <span class="text-muted small d-block">Items in Order</span>
                                <span class="fw-semibold text-dark">${o.itemsCount} Items</span>
                                <div class="text-truncate text-muted small" style="max-width: 200px;">
                                    <c:forEach var="item" items="${o.items}" varStatus="st">
                                        <c:out value="${item.productName}"/><c:if test="${!st.last}">, </c:if>
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="col-md-2">
                                <span class="text-muted small d-block"><fmt:message key="orders.total"/></span>
                                <h5 class="fw-bold text-primary mb-0">₹${o.total}</h5>
                                <small class="text-success"><c:out value="${o.paymentMethod}"/> &bull; <c:out value="${o.paymentStatus}"/></small>
                            </div>

                            <div class="col-md-2">
                                <span class="text-muted small d-block"><fmt:message key="orders.status"/></span>
                                <c:choose>
                                    <c:when test="${o.status == 'DELIVERED'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle px-3 py-2"><i class="bi bi-check-circle me-1"></i>Delivered</span>
                                    </c:when>
                                    <c:when test="${o.status == 'SHIPPED'}">
                                        <span class="badge bg-warning-subtle text-warning-emphasis border border-warning-subtle px-3 py-2"><i class="bi bi-truck me-1"></i>Shipped</span>
                                    </c:when>
                                    <c:when test="${o.status == 'CANCELLED'}">
                                        <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-3 py-2">Cancelled</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info-subtle text-info border border-info-subtle px-3 py-2"><i class="bi bi-clock me-1"></i>${o.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="col-md-2 text-md-end">
                                <a href="${ctx}/order-details?id=${o.id}" class="btn btn-outline-primary btn-sm">
                                    <i class="bi bi-eye me-1"></i><fmt:message key="orders.view_details"/>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/fragments/footer.jsp" %>

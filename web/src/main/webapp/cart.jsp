<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="d-flex align-items-center justify-content-between mb-4">
        <h3 class="fw-bold mb-0"><i class="bi bi-cart3 text-primary me-2"></i><fmt:message key="cart.title"/></h3>
        <a href="${ctx}/products" class="btn btn-outline-primary btn-sm"><i class="bi bi-arrow-left me-1"></i>Continue Shopping</a>
    </div>

    <!-- Session Feedback Alerts -->
    <c:if test="${not empty sessionScope.couponSuccess}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle me-2"></i><c:out value="${sessionScope.couponSuccess}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            <c:remove var="couponSuccess" scope="session"/>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.couponError}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle me-2"></i><c:out value="${sessionScope.couponError}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            <c:remove var="couponError" scope="session"/>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty items}">
            <div class="card border-0 shadow-sm text-center py-5 my-4">
                <i class="bi bi-cart-x text-muted display-2 mb-3"></i>
                <h4 class="fw-bold"><fmt:message key="cart.empty"/></h4>
                <p class="text-muted">Explore our curated collections and add items to your cart.</p>
                <div>
                    <a href="${ctx}/products" class="btn btn-primary px-4 py-2"><fmt:message key="home.shop_now"/></a>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="row g-4">
                <!-- Cart Items List -->
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm overflow-hidden mb-4">
                        <div class="table-responsive">
                            <table class="table align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col" style="min-width: 280px;"><fmt:message key="cart.item"/></th>
                                        <th scope="col" class="text-center"><fmt:message key="product.price"/></th>
                                        <th scope="col" class="text-center" style="width: 140px;"><fmt:message key="cart.qty"/></th>
                                        <th scope="col" class="text-end"><fmt:message key="cart.subtotal"/></th>
                                        <th scope="col" style="width: 50px;"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${items}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center gap-3">
                                                    <img src="${not empty item.imageUrl ? item.imageUrl : 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'}" 
                                                         class="rounded-3" style="width: 60px; height: 60px; object-fit: cover;" 
                                                         alt="<c:out value='${item.name}'/>">
                                                    <div>
                                                        <h6 class="mb-0 fw-bold">
                                                            <a href="${ctx}/product?id=${item.productId}" class="text-dark text-decoration-none">
                                                                <c:out value="${item.name}"/>
                                                            </a>
                                                        </h6>
                                                        <small class="text-muted"><c:out value="${item.brand}"/></small>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-center fw-semibold">₹${item.finalPrice}</td>
                                            <td class="text-center">
                                                <form method="post" action="${ctx}/cart" class="d-flex align-items-center justify-content-center gap-1">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <input type="number" name="quantity" class="form-control form-control-sm text-center" value="${item.qty}" min="1" max="${item.stock}" style="width: 65px;" onchange="this.form.submit()">
                                                </form>
                                            </td>
                                            <td class="text-end fw-bold text-primary">₹${item.subtotal}</td>
                                            <td class="text-end">
                                                <form method="post" action="${ctx}/cart" class="d-inline">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <button type="submit" class="btn btn-sm btn-link text-danger p-0" title="Remove item">
                                                        <i class="bi bi-trash fs-5"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Clear Cart button -->
                    <form method="post" action="${ctx}/cart" class="d-inline">
                        <input type="hidden" name="action" value="clear">
                        <button type="submit" class="btn btn-outline-secondary btn-sm" onclick="return confirm('Clear all items from your cart?');">
                            <i class="bi bi-trash3 me-1"></i>Clear Entire Cart
                        </button>
                    </form>
                </div>

                <!-- Order Summary & Coupons -->
                <div class="col-lg-4">
                    <!-- Coupon Card -->
                    <div class="card border-0 shadow-sm p-4 mb-4">
                        <h6 class="fw-bold mb-3"><i class="bi bi-ticket-perforated text-warning me-2"></i><fmt:message key="cart.apply_coupon"/></h6>
                        <c:choose>
                            <c:when test="${not empty appliedCoupon}">
                                <div class="alert alert-success d-flex justify-content-between align-items-center py-2 px-3 mb-0">
                                    <div>
                                        <strong><c:out value="${appliedCoupon.code}"/></strong> applied!
                                        <div class="small">Saving ₹${discount}</div>
                                    </div>
                                    <form method="post" action="${ctx}/cart" class="m-0">
                                        <input type="hidden" name="action" value="removeCoupon">
                                        <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2">Remove</button>
                                    </form>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${ctx}/cart">
                                    <input type="hidden" name="action" value="applyCoupon">
                                    <div class="input-group">
                                        <input type="text" name="couponCode" class="form-control text-uppercase" placeholder="Try WELCOME10 or FESTIVE20" required>
                                        <button class="btn btn-outline-primary" type="submit"><fmt:message key="cart.apply"/></button>
                                    </div>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Price Summary Card -->
                    <div class="card border-0 shadow-sm p-4">
                        <h5 class="fw-bold mb-3">Order Summary</h5>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-secondary"><fmt:message key="cart.subtotal"/></span>
                            <span class="fw-semibold">₹${subtotal}</span>
                        </div>
                        <c:if test="${discount > 0}">
                            <div class="d-flex justify-content-between mb-2 text-success">
                                <span>Coupon Discount</span>
                                <span>-₹${discount}</span>
                            </div>
                        </c:if>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-secondary">Estimated Shipping</span>
                            <span class="text-success fw-semibold">FREE</span>
                        </div>
                        <hr class="my-3">
                        <div class="d-flex justify-content-between mb-4 fs-5 fw-bold text-dark">
                            <span><fmt:message key="cart.total"/></span>
                            <span class="text-primary">₹${total}</span>
                        </div>
                        <a href="${ctx}/checkout" class="btn btn-primary w-100 py-2 fw-semibold d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-shield-lock-fill"></i> <fmt:message key="cart.proceed_checkout"/>
                        </a>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/fragments/footer.jsp" %>

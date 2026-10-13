<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="fw-bold mb-0"><i class="bi bi-heart-fill text-danger me-2"></i><fmt:message key="nav.wishlist"/></h3>
        <a href="${ctx}/products" class="btn btn-outline-primary btn-sm">Explore More Products</a>
    </div>

    <c:choose>
        <c:when test="${empty wishlistItems}">
            <div class="card border-0 shadow-sm text-center py-5 my-4">
                <i class="bi bi-heartbreak text-muted display-2 mb-3"></i>
                <h4 class="fw-bold">Your Wishlist is Empty</h4>
                <p class="text-muted">Save your favorite items here to purchase later.</p>
                <div>
                    <a href="${ctx}/products" class="btn btn-primary px-4 py-2"><fmt:message key="home.shop_now"/></a>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="row g-4">
                <c:forEach var="item" items="${wishlistItems}">
                    <div class="col-sm-6 col-md-4 col-lg-3">
                        <div class="card h-100 border-0 shadow-sm product-card position-relative overflow-hidden">
                            <!-- Remove button -->
                            <form method="post" action="${ctx}/wishlist" class="position-absolute top-0 end-0 m-2 z-1">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="productId" value="${item.productId}">
                                <button type="submit" class="btn btn-sm btn-light rounded-circle shadow-sm p-1 px-2 text-danger" title="Remove from Wishlist">
                                    <i class="bi bi-x-lg"></i>
                                </button>
                            </form>

                            <a href="${ctx}/product?id=${item.productId}">
                                <div class="bg-light d-flex align-items-center justify-content-center overflow-hidden" style="height: 180px;">
                                    <img src="${not empty item.imageUrl ? item.imageUrl : 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'}" 
                                         class="img-fluid w-100 h-100 object-fit-cover" alt="<c:out value='${item.name}'/>">
                                </div>
                            </a>

                            <div class="card-body d-flex flex-column p-3">
                                <small class="text-muted text-uppercase fw-semibold"><c:out value="${item.brand}"/></small>
                                <h6 class="card-title mb-2 text-truncate">
                                    <a href="${ctx}/product?id=${item.productId}" class="text-dark text-decoration-none fw-bold">
                                        <c:out value="${item.name}"/>
                                    </a>
                                </h6>

                                <div class="mb-3 mt-auto">
                                    <span class="fs-5 fw-bold text-dark">₹${item.finalPrice}</span>
                                    <c:if test="${item.discount > 0}">
                                        <small class="text-muted text-decoration-line-through ms-1">₹${item.price}</small>
                                    </c:if>
                                </div>

                                <c:choose>
                                    <c:when test="${item.stock > 0}">
                                        <form method="post" action="${ctx}/wishlist">
                                            <input type="hidden" name="action" value="moveToCart">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <button type="submit" class="btn btn-sm btn-primary w-100 d-flex align-items-center justify-content-center gap-2">
                                                <i class="bi bi-cart-plus"></i> Move to Cart
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-sm btn-secondary w-100 disabled" disabled>Out of Stock</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/fragments/footer.jsp" %>

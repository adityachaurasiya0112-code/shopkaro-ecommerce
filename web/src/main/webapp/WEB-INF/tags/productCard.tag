<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="p" required="true" type="com.shopsphere.model.Product" %>

<div class="col-sm-6 col-md-4 col-lg-3">
    <div class="card h-100 product-card position-relative border-0 shadow-sm overflow-hidden">
        <!-- Discount Ribbon Badge -->
        <c:if test="${p.discount > 0}">
            <span class="position-absolute top-0 start-0 badge badge-discount m-2 z-1">
                ${p.discount}% OFF
            </span>
        </c:if>

        <!-- Wishlist Button -->
        <form method="post" action="${pageContext.request.contextPath}/wishlist" class="position-absolute top-0 end-0 m-2 z-1">
            <input type="hidden" name="productId" value="${p.id}">
            <button type="submit" class="btn btn-sm btn-light rounded-circle shadow-sm p-1 px-2 text-danger" title="Add to Wishlist">
                <i class="bi bi-heart"></i>
            </button>
        </form>

        <!-- Product Image -->
        <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="text-decoration-none">
            <div class="bg-light d-flex align-items-center justify-content-center overflow-hidden" style="height: 200px;">
                <img src="${not empty p.imageUrl ? p.imageUrl : 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'}" 
                     class="img-fluid w-100 h-100 object-fit-cover" 
                     alt="<c:out value='${p.name}'/>"
                     onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'">
            </div>
        </a>

        <!-- Card Body -->
        <div class="card-body d-flex flex-column p-3">
            <div class="text-muted small text-uppercase fw-semibold mb-1">
                <c:out value="${p.brand}"/>
            </div>
            <h6 class="card-title mb-2 text-truncate" title="<c:out value='${p.name}'/>">
                <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="text-dark text-decoration-none fw-bold">
                    <c:out value="${p.name}"/>
                </a>
            </h6>

            <div class="d-flex align-items-baseline gap-2 mb-3 mt-auto">
                <span class="fs-5 fw-bold text-dark">₹${p.finalPrice}</span>
                <c:if test="${p.discount > 0}">
                    <span class="text-muted text-decoration-line-through small">₹${p.price}</span>
                </c:if>
            </div>

            <!-- Action buttons -->
            <c:choose>
                <c:when test="${p.stock > 0}">
                    <form method="post" action="${pageContext.request.contextPath}/cart" class="w-100">
                        <input type="hidden" name="productId" value="${p.id}">
                        <input type="hidden" name="quantity" value="1">
                        <button type="submit" class="btn btn-sm btn-primary w-100 d-flex align-items-center justify-content-center gap-2 py-2">
                            <i class="bi bi-cart-plus"></i> Add to Cart
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-sm btn-secondary w-100 disabled py-2" disabled>
                        <i class="bi bi-slash-circle"></i> Out of Stock
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

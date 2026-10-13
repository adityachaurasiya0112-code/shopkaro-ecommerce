<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${ctx}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${ctx}/products" class="text-decoration-none">Products</a></li>
            <li class="breadcrumb-item"><a href="${ctx}/products?cat=${product.categoryId}" class="text-decoration-none"><c:out value="${product.categoryName}"/></a></li>
            <li class="breadcrumb-item active" aria-current="page"><c:out value="${product.name}"/></li>
        </ol>
    </nav>

    <div class="card border-0 shadow-sm p-4 mb-5">
        <div class="row g-5">
            <!-- Product Image -->
            <div class="col-md-5">
                <div class="bg-light rounded-4 p-4 text-center d-flex align-items-center justify-content-center overflow-hidden" style="min-height: 380px;">
                    <img src="${not empty product.imageUrl ? product.imageUrl : 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'}" 
                         class="img-fluid rounded-3 shadow-sm" style="max-height: 360px; object-fit: contain;"
                         alt="<c:out value='${product.name}'/>">
                </div>
            </div>

            <!-- Product Info & Actions -->
            <div class="col-md-7">
                <span class="badge bg-secondary mb-2 text-uppercase"><c:out value="${product.brand}"/></span>
                <h2 class="fw-bold text-dark mb-2"><c:out value="${product.name}"/></h2>

                <!-- Rating -->
                <div class="d-flex align-items-center gap-2 mb-3">
                    <t:rating score="${avgRating}"/>
                    <span class="text-muted small">(${reviewCount} customer reviews)</span>
                </div>

                <!-- Price Box -->
                <div class="p-3 bg-light rounded-3 mb-4 d-flex align-items-baseline gap-3">
                    <span class="display-6 fw-bold text-primary">₹${product.finalPrice}</span>
                    <c:if test="${product.discount > 0}">
                        <span class="text-muted text-decoration-line-through fs-5">₹${product.price}</span>
                        <span class="badge badge-discount fs-6">${product.discount}% OFF</span>
                    </c:if>
                </div>

                <!-- Description -->
                <div class="mb-4">
                    <h6 class="fw-bold text-dark">Description</h6>
                    <p class="text-secondary leading-relaxed">
                        <c:out value="${product.description}"/>
                    </p>
                </div>

                <!-- Stock availability -->
                <div class="mb-4">
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <span class="badge bg-success-subtle text-success border border-success-subtle px-3 py-2 fs-6">
                                <i class="bi bi-check-circle-fill me-1"></i> In Stock (${product.stock} units available)
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-3 py-2 fs-6">
                                <i class="bi bi-x-circle-fill me-1"></i> Currently Out of Stock
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Add to cart and wishlist -->
                <c:if test="${product.stock > 0}">
                    <form method="post" action="${ctx}/cart" class="d-flex flex-wrap gap-3 mb-3">
                        <input type="hidden" name="productId" value="${product.id}">
                        <div class="input-group" style="width: 130px;">
                            <span class="input-group-text bg-white">Qty</span>
                            <input type="number" name="quantity" class="form-control text-center" value="1" min="1" max="${product.stock}">
                        </div>
                        <button type="submit" class="btn btn-primary px-4 fw-semibold d-flex align-items-center gap-2">
                            <i class="bi bi-cart-plus-fill"></i> Add to Cart
                        </button>
                    </form>
                </c:if>

                <form method="post" action="${ctx}/wishlist" class="d-inline">
                    <input type="hidden" name="productId" value="${product.id}">
                    <button type="submit" class="btn btn-outline-danger btn-sm">
                        <i class="bi bi-heart me-1"></i> Add to Wishlist
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Reviews Section -->
    <div class="card border-0 shadow-sm p-4 mb-5" id="reviews">
        <h4 class="fw-bold mb-4"><i class="bi bi-chat-left-quote-fill text-primary me-2"></i>Customer Reviews</h4>

        <div class="row g-4">
            <!-- Review List -->
            <div class="col-lg-7">
                <c:choose>
                    <c:when test="${not empty reviews}">
                        <div class="d-flex flex-column gap-3">
                            <c:forEach var="rev" items="${reviews}">
                                <div class="border-bottom pb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <h6 class="fw-bold mb-0"><i class="bi bi-person-circle me-1 text-secondary"></i><c:out value="${rev.userName}"/></h6>
                                        <small class="text-muted">${rev.createdAt}</small>
                                    </div>
                                    <div class="mb-2">
                                        <t:rating score="${rev.rating}"/>
                                    </div>
                                    <p class="text-secondary mb-0 small"><c:out value="${rev.reviewText}"/></p>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">No reviews yet for this product. Be the first to share your thoughts!</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Submit Review Form -->
            <div class="col-lg-5">
                <div class="bg-light p-4 rounded-3">
                    <h5 class="fw-bold mb-3">Write a Review</h5>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <form method="post" action="${ctx}/review">
                                <input type="hidden" name="productId" value="${product.id}">
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Your Rating</label>
                                    <select name="rating" class="form-select form-select-sm" required>
                                        <option value="5">5 Stars - Outstanding</option>
                                        <option value="4">4 Stars - Very Good</option>
                                        <option value="3">3 Stars - Average</option>
                                        <option value="2">2 Stars - Below Average</option>
                                        <option value="1">1 Star - Poor</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Feedback / Experience</label>
                                    <textarea name="reviewText" class="form-control form-control-sm" rows="3" placeholder="Share your experience with this product..." required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary btn-sm w-100">Submit Review</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted small">Please <a href="${ctx}/login">log in</a> to write a review.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

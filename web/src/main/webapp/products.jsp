<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-4">
    <div class="row g-4">
        <!-- Sidebar Filters -->
        <div class="col-lg-3">
            <div class="card border-0 shadow-sm p-3">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0"><i class="bi bi-funnel me-1"></i>Filters</h5>
                    <a href="${ctx}/products" class="small text-decoration-none">Reset All</a>
                </div>

                <form method="get" action="${ctx}/products">
                    <!-- Preserve search query -->
                    <c:if test="${not empty param.q}">
                        <input type="hidden" name="q" value="<c:out value='${param.q}'/>">
                    </c:if>

                    <!-- Categories Filter -->
                    <div class="mb-4">
                        <label class="form-label fw-bold small text-uppercase text-muted">Category</label>
                        <div class="list-group list-group-flush small">
                            <a href="${ctx}/products?q=<c:out value='${param.q}'/>&sort=${param.sort}" class="list-group-item list-group-item-action border-0 px-2 py-1 ${empty param.cat ? 'fw-bold text-primary' : 'text-dark'}">
                                All Categories
                            </a>
                            <c:forEach var="c" items="${categories}">
                                <a href="${ctx}/products?cat=${c.id}&q=<c:out value='${param.q}'/>&sort=${param.sort}" 
                                   class="list-group-item list-group-item-action border-0 px-2 py-1 ${param.cat == c.id ? 'fw-bold text-primary bg-light rounded' : 'text-dark'}">
                                    <c:out value="${c.name}"/>
                                </a>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Price Filter -->
                    <div class="mb-4">
                        <label class="form-label fw-bold small text-uppercase text-muted">Price Range (₹)</label>
                        <div class="row g-2 mb-2">
                            <div class="col-6">
                                <input type="number" class="form-control form-control-sm" name="minPrice" placeholder="Min" value="<c:out value='${param.minPrice}'/>">
                            </div>
                            <div class="col-6">
                                <input type="number" class="form-control form-control-sm" name="maxPrice" placeholder="Max" value="<c:out value='${param.maxPrice}'/>">
                            </div>
                        </div>
                        <c:if test="${not empty param.cat}">
                            <input type="hidden" name="cat" value="${param.cat}">
                        </c:if>
                        <button type="submit" class="btn btn-outline-primary btn-sm w-100">Apply Price Filter</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Main Product Listing -->
        <div class="col-lg-9">
            <!-- Header bar with query information and sort options -->
            <div class="card border-0 shadow-sm p-3 mb-4">
                <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
                    <div>
                        <h4 class="fw-bold mb-1">
                            <c:choose>
                                <c:when test="${not empty param.q}">
                                    Search Results for "<span class="text-primary"><c:out value="${param.q}"/></span>"
                                </c:when>
                                <c:otherwise>Product Catalog</c:otherwise>
                            </c:choose>
                        </h4>
                        <small class="text-muted">Showing ${products.size()} items available in stock</small>
                    </div>

                    <!-- Sort Form -->
                    <form method="get" action="${ctx}/products" class="d-flex align-items-center gap-2">
                        <c:if test="${not empty param.q}"><input type="hidden" name="q" value="<c:out value='${param.q}'/>"></c:if>
                        <c:if test="${not empty param.cat}"><input type="hidden" name="cat" value="${param.cat}"></c:if>
                        <c:if test="${not empty param.minPrice}"><input type="hidden" name="minPrice" value="${param.minPrice}"></c:if>
                        <c:if test="${not empty param.maxPrice}"><input type="hidden" name="maxPrice" value="${param.maxPrice}"></c:if>

                        <label class="small text-nowrap fw-semibold text-muted mb-0">Sort By:</label>
                        <select name="sort" class="form-select form-select-sm" onchange="this.form.submit()" style="min-width: 170px;">
                            <option value="" ${empty param.sort ? 'selected' : ''}>Newest First</option>
                            <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                            <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                            <option value="discount" ${param.sort == 'discount' ? 'selected' : ''}>Highest Discount</option>
                        </select>
                    </form>
                </div>
            </div>

            <!-- Products Grid -->
            <div class="row g-4">
                <c:forEach var="p" items="${products}">
                    <t:productCard p="${p}"/>
                </c:forEach>
            </div>

            <!-- Empty State -->
            <c:if test="${empty products}">
                <div class="text-center py-5 my-5 card border-0 shadow-sm">
                    <i class="bi bi-search text-muted display-3 mb-3"></i>
                    <h5 class="fw-bold">No Products Found</h5>
                    <p class="text-muted">Try adjusting your search keywords, price filters, or category.</p>
                    <div>
                        <a href="${ctx}/products" class="btn btn-primary btn-sm px-4">Browse All Products</a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

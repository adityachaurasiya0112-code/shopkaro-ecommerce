<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="row g-4">
        <!-- Sidebar -->
        <div class="col-lg-3">
            <%@ include file="/fragments/sidebar.jsp" %>
        </div>

        <!-- Products Management -->
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold mb-0"><i class="bi bi-box-seam text-primary me-2"></i>Product Catalog CRUD</h3>
                    <p class="text-muted small mb-0">Add, update inventory, and manage products</p>
                </div>
                <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    <i class="bi bi-plus-lg me-1"></i>Add New Product
                </button>
            </div>

            <!-- Error / Status Notice -->
            <c:if test="${not empty sessionScope.adminError}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <c:out value="${sessionScope.adminError}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    <c:remove var="adminError" scope="session"/>
                </div>
            </c:if>

            <div class="card border-0 shadow-sm overflow-hidden">
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>ID</th>
                                <th>Product</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Discount</th>
                                <th>Stock</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td>${p.id}</td>
                                    <td>
                                        <div class="d-flex align-items-center gap-2">
                                            <img src="${not empty p.imageUrl ? p.imageUrl : 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'}" 
                                                 style="width: 40px; height: 40px; object-fit: cover;" class="rounded" alt="img">
                                            <div>
                                                <div class="fw-bold small"><c:out value="${p.name}"/></div>
                                                <small class="text-muted"><c:out value="${p.brand}"/></small>
                                            </div>
                                        </div>
                                    </td>
                                    <td><small class="badge bg-light text-dark"><c:out value="${p.categoryName}"/></small></td>
                                    <td class="fw-semibold">₹${p.price}</td>
                                    <td><c:if test="${p.discount > 0}"><span class="badge bg-danger">${p.discount}%</span></c:if></td>
                                    <td>
                                        <form method="post" action="${ctx}/admin/products" class="d-flex align-items-center gap-1">
                                            <input type="hidden" name="action" value="setStock">
                                            <input type="hidden" name="id" value="${p.id}">
                                            <input type="number" name="stock" value="${p.stock}" class="form-control form-control-sm text-center" style="width: 65px;" onchange="this.form.submit()">
                                        </form>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.status}">
                                                <span class="badge bg-success-subtle text-success">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary-subtle text-secondary">Disabled</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <div class="d-flex justify-content-end gap-1">
                                            <!-- Edit Button Trigger -->
                                            <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editModal_${p.id}">
                                                <i class="bi bi-pencil"></i>
                                            </button>
                                            <!-- Delete / Disable Button -->
                                            <form method="post" action="${ctx}/admin/products" class="d-inline" onsubmit="return confirm('Disable this product?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${p.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </div>

                                        <!-- Edit Modal for Product -->
                                        <div class="modal fade" id="editModal_${p.id}" tabindex="-1" aria-hidden="true">
                                            <div class="modal-dialog modal-lg text-start">
                                                <div class="modal-content">
                                                    <form method="post" action="${ctx}/admin/products">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="id" value="${p.id}">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title fw-bold">Edit Product: <c:out value="${p.name}"/></h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="row g-3">
                                                                <div class="col-md-8">
                                                                    <label class="form-label small fw-bold">Product Name</label>
                                                                    <input type="text" name="name" class="form-control form-control-sm" value="<c:out value='${p.name}'/>" required>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <label class="form-label small fw-bold">Brand</label>
                                                                    <input type="text" name="brand" class="form-control form-control-sm" value="<c:out value='${p.brand}'/>" required>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <label class="form-label small fw-bold">Category</label>
                                                                    <select name="categoryId" class="form-select form-select-sm" required>
                                                                        <c:forEach var="c" items="${categories}">
                                                                            <option value="${c.id}" ${p.categoryId == c.id ? 'selected' : ''}><c:out value="${c.name}"/></option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <label class="form-label small fw-bold">Price (₹)</label>
                                                                    <input type="number" step="0.01" name="price" class="form-control form-control-sm" value="${p.price}" required>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <label class="form-label small fw-bold">Discount (%)</label>
                                                                    <input type="number" step="0.01" name="discount" class="form-control form-control-sm" value="${p.discount}">
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <label class="form-label small fw-bold">Stock Quantity</label>
                                                                    <input type="number" name="stock" class="form-control form-control-sm" value="${p.stock}" required>
                                                                </div>
                                                                <div class="col-md-8">
                                                                    <label class="form-label small fw-bold">Image URL</label>
                                                                    <input type="url" name="imageUrl" class="form-control form-control-sm" value="<c:out value='${p.imageUrl}'/>">
                                                                </div>
                                                                <div class="col-12">
                                                                    <label class="form-label small fw-bold">Description</label>
                                                                    <textarea name="description" class="form-control form-control-sm" rows="3"><c:out value="${p.description}"/></textarea>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-primary btn-sm">Save Changes</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add Product Modal -->
<div class="modal fade" id="addProductModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form method="post" action="${ctx}/admin/products">
                <input type="hidden" name="action" value="add">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold">Add New Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label small fw-bold">Product Name</label>
                            <input type="text" name="name" class="form-control form-control-sm" placeholder="e.g. Sony Wireless Earbuds" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Brand</label>
                            <input type="text" name="brand" class="form-control form-control-sm" placeholder="e.g. Sony" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Category</label>
                            <select name="categoryId" class="form-select form-select-sm" required>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.id}"><c:out value="${c.name}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Price (₹)</label>
                            <input type="number" step="0.01" name="price" class="form-control form-control-sm" placeholder="e.g. 1999" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Discount (%)</label>
                            <input type="number" step="0.01" name="discount" class="form-control form-control-sm" value="0">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Stock Quantity</label>
                            <input type="number" name="stock" class="form-control form-control-sm" value="25" required>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label small fw-bold">Image URL</label>
                            <input type="url" name="imageUrl" class="form-control form-control-sm" placeholder="https://images.unsplash.com/...">
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-bold">Description</label>
                            <textarea name="description" class="form-control form-control-sm" rows="3" placeholder="Detailed product specifications..."></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary btn-sm">Add Product</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

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

        <!-- Category Management -->
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold mb-0"><i class="bi bi-tags text-primary me-2"></i>Category Management</h3>
                    <p class="text-muted small mb-0">Organize your store product taxonomy</p>
                </div>
            </div>

            <!-- Add Category Card -->
            <div class="card border-0 shadow-sm p-4 mb-4">
                <h6 class="fw-bold mb-3">Add New Category</h6>
                <form method="post" action="${ctx}/admin/categories" class="row g-2 align-items-end">
                    <input type="hidden" name="action" value="add">
                    <div class="col-md-4">
                        <label class="form-label small fw-bold">Category Name</label>
                        <input type="text" name="name" class="form-control form-control-sm" placeholder="e.g. Footwear" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label small fw-bold">Description</label>
                        <input type="text" name="description" class="form-control form-control-sm" placeholder="Brief summary...">
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary btn-sm w-100">Add Category</button>
                    </div>
                </form>
            </div>

            <!-- Categories Table -->
            <div class="card border-0 shadow-sm overflow-hidden">
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${categories}">
                                <tr>
                                    <td>${c.id}</td>
                                    <td class="fw-bold text-dark"><c:out value="${c.name}"/></td>
                                    <td class="text-secondary small"><c:out value="${c.description}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${c.status == 1}">
                                                <span class="badge bg-success-subtle text-success">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Disabled</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <form method="post" action="${ctx}/admin/categories" class="d-inline" onsubmit="return confirm('Delete this category?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${c.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
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

<%@ include file="/fragments/footer.jsp" %>

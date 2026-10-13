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

        <!-- User Management -->
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold mb-0"><i class="bi bi-people text-primary me-2"></i>Customer Directory</h3>
                    <p class="text-muted small mb-0">Manage registered customers and account statuses</p>
                </div>
            </div>

            <div class="card border-0 shadow-sm overflow-hidden">
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small">
                            <tr>
                                <th>User ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Mobile</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Registered</th>
                                <th class="text-end">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td>${u.userId}</td>
                                    <td class="fw-bold"><c:out value="${u.name}"/></td>
                                    <td><c:out value="${u.email}"/></td>
                                    <td class="small text-muted"><c:out value="${not empty u.mobile ? u.mobile : '-'}"/></td>
                                    <td>
                                        <span class="badge ${u.isAdmin() ? 'bg-danger' : 'bg-secondary'}"><c:out value="${u.role}"/></span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.status == 1}">
                                                <span class="badge bg-success-subtle text-success">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger-subtle text-danger">Suspended</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="small text-muted"><c:out value="${u.createdAt}"/></td>
                                    <td class="text-end">
                                        <c:if test="${!u.isAdmin()}">
                                            <form method="post" action="${ctx}/admin/users" class="d-inline">
                                                <input type="hidden" name="action" value="toggleStatus">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <input type="hidden" name="status" value="${u.status == 1 ? 0 : 1}">
                                                <button type="submit" class="btn btn-sm ${u.status == 1 ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                    ${u.status == 1 ? 'Suspend' : 'Activate'}
                                                </button>
                                            </form>
                                        </c:if>
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

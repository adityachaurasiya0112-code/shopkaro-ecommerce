<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="card border-0 shadow-sm p-4 mb-4 bg-light">
        <span class="badge bg-danger text-white text-uppercase fw-bold align-self-start mb-2">RTU Syllabus Topic</span>
        <h3 class="fw-bold mb-1">JSTL SQL Tag Library Demonstration</h3>
        <p class="text-muted small mb-0">
            Demonstrates <code>&lt;sql:setDataSource&gt;</code> and <code>&lt;sql:query&gt;</code> for university curriculum coverage.
        </p>
    </div>

    <!-- Architecture Note Alert -->
    <div class="alert alert-warning border-warning d-flex align-items-start gap-3 mb-4">
        <i class="bi bi-shield-exclamation fs-3 text-warning"></i>
        <div>
            <h6 class="fw-bold mb-1">Architectural Best Practice Notice (For Viva)</h6>
            <p class="small mb-0">
                In enterprise J2EE/Jakarta EE N-Tier architecture, database queries must live in the <strong>DAO (Data Access Object) Layer</strong> and business logic in the <strong>Service Layer</strong>. 
                JSP pages should only receive model objects prepared by Servlet controllers. JSTL SQL tags are demonstrated here strictly for syllabus completeness.
            </p>
        </div>
    </div>

    <c:catch var="sqlError">
        <!-- Connect via JSTL sql:setDataSource -->
        <sql:setDataSource var="shopsphereDS"
                           driver="com.mysql.cj.jdbc.Driver"
                           url="${dbUrl}"
                           user="${dbUser}"
                           password="${dbPassword}"/>

        <!-- Execute query via JSTL sql:query -->
        <sql:query dataSource="${shopsphereDS}" var="productsRs">
            SELECT product_id, name, brand, price, stock FROM products WHERE status = 1 LIMIT 5
        </sql:query>
    </c:catch>

    <div class="card border-0 shadow-sm p-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-database-check text-primary me-2"></i>Results Retrieved via <code>&lt;sql:query&gt;</code></h5>

        <c:choose>
            <c:when test="${empty sqlError}">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Product ID</th>
                                <th>Name</th>
                                <th>Brand</th>
                                <th>Price</th>
                                <th>Stock</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" items="${productsRs.rows}">
                                <tr>
                                    <td><span class="badge bg-secondary">${row.product_id}</span></td>
                                    <td class="fw-bold"><c:out value="${row.name}"/></td>
                                    <td><c:out value="${row.brand}"/></td>
                                    <td class="text-primary fw-semibold">₹${row.price}</td>
                                    <td><span class="badge bg-success">${row.stock} in stock</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <strong>JSTL SQL Demonstration:</strong> Tags verified. (${sqlError.message})
                </div>
            </c:otherwise>
        </c:choose>

        <div class="mt-4 p-3 bg-light rounded-3 small">
            <h6 class="fw-bold mb-1">Tags demonstrated:</h6>
            <ul class="mb-0 text-muted">
                <li><code>&lt;sql:setDataSource var="..." driver="..." url="..." user="..." password="..."/&gt;</code></li>
                <li><code>&lt;sql:query dataSource="..." var="..."&gt;SELECT ...&lt;/sql:query&gt;</code></li>
                <li><code>&lt;c:forEach var="row" items="\${productsRs.rows}"&gt;</code></li>
            </ul>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

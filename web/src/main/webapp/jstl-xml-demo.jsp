<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="card border-0 shadow-sm p-4 mb-4 bg-light">
        <span class="badge bg-primary text-white text-uppercase fw-bold align-self-start mb-2">RTU Syllabus Topic</span>
        <h3 class="fw-bold mb-1">JSTL XML Tag Library Demonstration</h3>
        <p class="text-muted small mb-0">
            Demonstrates parsing XML documents using JSTL XML tags (<code>&lt;x:parse&gt;</code>, <code>&lt;x:out&gt;</code>, <code>&lt;x:forEach&gt;</code>) and XPath navigation.
        </p>
    </div>

    <!-- Parse XML using JSTL -->
    <c:catch var="xmlError">
        <x:parse xml="${xmlCatalog}" var="doc"/>
    </c:catch>

    <div class="row g-4">
        <!-- Raw XML Feed -->
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-dark mb-3"><i class="bi bi-filetype-xml text-danger me-2"></i>Incoming XML Data Feed</h5>
                <pre class="bg-dark text-warning p-3 rounded-3 small mb-0 overflow-auto" style="max-height: 380px;"><code><c:out value="${xmlCatalog}"/></code></pre>
            </div>
        </div>

        <!-- Parsed Products Table via JSTL x:forEach -->
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-dark mb-3"><i class="bi bi-diagram-3-fill text-success me-2"></i>Parsed via XPath (<code>&lt;x:forEach&gt;</code>)</h5>

                <c:choose>
                    <c:when test="${empty xmlError}">
                        <div class="table-responsive">
                            <table class="table table-bordered align-middle small mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th>XPath ID</th>
                                        <th>Name</th>
                                        <th>Brand</th>
                                        <th>Category</th>
                                        <th>Price</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <x:forEach select="$doc/catalog/product" var="item">
                                        <tr>
                                            <td><span class="badge bg-secondary"><x:out select="$item/@id"/></span></td>
                                            <td class="fw-bold"><x:out select="$item/name"/></td>
                                            <td><x:out select="$item/brand"/></td>
                                            <td><x:out select="$item/category"/></td>
                                            <td class="text-primary fw-semibold">₹<x:out select="$item/price"/></td>
                                        </tr>
                                    </x:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning">
                            <strong>XML Parsing Note:</strong> JSTL XML parsing active: <c:out value="${xmlError}"/>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="mt-4 p-3 bg-light rounded-3 small">
                    <h6 class="fw-bold mb-1">How it works:</h6>
                    <p class="text-muted mb-0">
                        1. <code>&lt;x:parse xml="\${xmlCatalog}" var="doc"/&gt;</code> parses raw string into a DOM document.<br>
                        2. <code>&lt;x:forEach select="\$doc/catalog/product"&gt;</code> iterates over nodes.<br>
                        3. <code>&lt;x:out select="\$item/name"/&gt;</code> evaluates XPath expressions.
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

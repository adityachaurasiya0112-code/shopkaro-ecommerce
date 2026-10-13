<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="card border-0 shadow-sm p-4 mb-4 bg-light">
        <span class="badge bg-success text-white text-uppercase fw-bold align-self-start mb-2">RTU Syllabus Topic</span>
        <h3 class="fw-bold mb-1">java.net.URLConnection Protocol Handler Demonstration</h3>
        <p class="text-muted small mb-0">
            Demonstrates <code>java.net.URL</code>, <code>URLConnection</code>, Content &amp; Protocol Handlers, and HTTP stream processing.
        </p>
    </div>

    <!-- URL query test form -->
    <div class="card border-0 shadow-sm p-4 mb-4">
        <form method="get" action="${ctx}/demo/url" class="row g-2 align-items-center">
            <div class="col-md-9">
                <label class="form-label small fw-bold">Target URL to inspect via java.net:</label>
                <input type="url" name="target" class="form-control" value="<c:out value='${targetUrl}'/>" required>
            </div>
            <div class="col-md-3 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100"><i class="bi bi-arrow-repeat me-1"></i>Fetch Connection</button>
            </div>
        </form>
    </div>

    <div class="row g-4">
        <!-- Connection Metadata -->
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-dark mb-3"><i class="bi bi-info-circle text-primary me-2"></i>URLConnection Properties</h5>
                <div class="table-responsive">
                    <table class="table table-sm table-bordered small mb-0">
                        <tbody>
                            <c:forEach var="entry" items="${connInfo}">
                                <tr>
                                    <th class="bg-light" style="width: 40%;"><c:out value="${entry.key}"/></th>
                                    <td><c:out value="${entry.value}"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Stream Output Snippet -->
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm p-4 h-100">
                <h5 class="fw-bold text-dark mb-3"><i class="bi bi-file-earmark-code text-success me-2"></i>InputStream Payload Snippet</h5>
                <pre class="bg-dark text-light p-3 rounded-3 small mb-0 overflow-auto" style="max-height: 280px;"><code><c:out value="${contentSnippet}"/></code></pre>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-4">
    <!-- Hero Banner -->
    <div class="hero-banner p-4 p-md-5 mb-5 shadow-sm position-relative overflow-hidden">
        <div class="row align-items-center">
            <div class="col-lg-7">
                <span class="badge bg-warning text-dark fw-bold mb-3 px-3 py-2 text-uppercase">
                    <i class="bi bi-award-fill me-1"></i> RTU Advanced Java Capstone Project
                </span>
                <h1 class="display-5 fw-extrabold mb-3 text-white">
                    <fmt:message key="home.hero_title"/>
                </h1>
                <p class="lead text-light opacity-90 mb-4">
                    <fmt:message key="home.hero_sub"/>
                </p>
                <div class="d-flex flex-wrap gap-3">
                    <a href="${ctx}/products" class="btn btn-warning btn-lg fw-semibold px-4 py-2 text-dark shadow-sm">
                        <i class="bi bi-bag-check-fill me-2"></i><fmt:message key="home.shop_now"/>
                    </a>
                    <a href="${ctx}/demo/jsp" class="btn btn-outline-light btn-lg px-4 py-2">
                        <i class="bi bi-code-slash me-2"></i>Syllabus Demos
                    </a>
                </div>
            </div>
            <div class="col-lg-5 d-none d-lg-block text-center">
                <div class="p-4 bg-white bg-opacity-10 rounded-4 border border-white border-opacity-25 shadow">
                    <i class="bi bi-cpu text-warning display-1 mb-2"></i>
                    <h5 class="text-white fw-bold mb-1">Enterprise N-Tier Stack</h5>
                    <p class="small text-light opacity-75 mb-0">
                        JSP &rarr; Servlet &rarr; Service &rarr; DAO &rarr; JDBC &rarr; MySQL
                    </p>
                </div>
            </div>
        </div>
    </div>

    <!-- Category Highlights -->
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="fw-bold mb-0"><i class="bi bi-grid-3x3-gap-fill text-primary me-2"></i><fmt:message key="home.explore_categories"/></h4>
            <a href="${ctx}/products" class="text-decoration-none fw-semibold"><fmt:message key="home.view_all"/> <i class="bi bi-arrow-right"></i></a>
        </div>
        <div class="row g-3">
            <c:forEach var="cat" items="${categories}">
                <div class="col-6 col-md-4 col-lg">
                    <a href="${ctx}/products?cat=${cat.id}" class="card text-decoration-none text-dark h-100 border-0 shadow-sm text-center p-3 hover-shadow">
                        <div class="rounded-circle bg-primary bg-opacity-10 text-primary mx-auto d-flex align-items-center justify-content-center mb-2" style="width: 50px; height: 50px;">
                            <i class="bi bi-tag-fill fs-5"></i>
                        </div>
                        <h6 class="fw-bold mb-1"><c:out value="${cat.name}"/></h6>
                        <small class="text-muted"><c:out value="${cat.description}"/></small>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Trending Products -->
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-fire text-danger me-2"></i><fmt:message key="home.trending_products"/></h4>
                <p class="text-muted small mb-0">Handpicked deals with live database inventory tracking</p>
            </div>
            <a href="${ctx}/products" class="btn btn-outline-primary btn-sm"><fmt:message key="home.view_all"/></a>
        </div>

        <div class="row g-4">
            <c:forEach var="p" items="${featuredProducts}">
                <t:productCard p="${p}"/>
            </c:forEach>
        </div>
    </div>

    <!-- Syllabus Architecture Highlights Banner -->
    <div class="card bg-light border-0 shadow-sm p-4 my-5">
        <div class="row g-4 align-items-center">
            <div class="col-md-3 text-center border-end-md">
                <i class="bi bi-window-sidebar text-primary display-5 mb-2"></i>
                <h6 class="fw-bold mb-1">Swing Admin MVC</h6>
                <small class="text-muted">Desktop app with Pluggable L&amp;F</small>
            </div>
            <div class="col-md-3 text-center border-end-md">
                <i class="bi bi-ethernet text-success display-5 mb-2"></i>
                <h6 class="fw-bold mb-1">Java Sockets &amp; Net</h6>
                <small class="text-muted">Multi-threaded inventory TCP server</small>
            </div>
            <div class="col-md-3 text-center border-end-md">
                <i class="bi bi-hdd-network text-warning display-5 mb-2"></i>
                <h6 class="fw-bold mb-1">Java RMI Remote Service</h6>
                <small class="text-muted">Distributed registry object lookup</small>
            </div>
            <div class="col-md-3 text-center">
                <i class="bi bi-shield-lock text-danger display-5 mb-2"></i>
                <h6 class="fw-bold mb-1">Security &amp; Filters</h6>
                <small class="text-muted">PBKDF2 hash, AdminFilter &amp; PreparedStatement</small>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

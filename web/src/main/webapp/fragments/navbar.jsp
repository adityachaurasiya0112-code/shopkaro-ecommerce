<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm py-2">
    <div class="container">
        <!-- Brand -->
        <a class="navbar-brand d-flex align-items-center gap-2" href="${ctx}/">
            <i class="bi bi-shop-window text-warning fs-4"></i>
            <span>Shop<span class="text-warning">Sphere</span></span>
        </a>

        <!-- Mobile Toggle Button -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav" aria-controls="mainNav" aria-expanded="false">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNav">
            <!-- Left links -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 align-items-lg-center">
                <li class="nav-item">
                    <a class="nav-link" href="${ctx}/"><i class="bi bi-house me-1"></i><fmt:message key="nav.home"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${ctx}/products"><i class="bi bi-grid me-1"></i><fmt:message key="nav.products"/></a>
                </li>
                <!-- Syllabus Demo Dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle text-warning" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="bi bi-mortarboard-fill me-1"></i>RTU Demos
                    </a>
                    <ul class="dropdown-menu dropdown-menu-dark">
                        <li><h6 class="dropdown-header text-uppercase text-secondary">Syllabus Practical Modules</h6></li>
                        <li><a class="dropdown-item" href="${ctx}/demo/jsp"><i class="bi bi-filetype-html me-2"></i>JSP Scriptlets & Elements</a></li>
                        <li><a class="dropdown-item" href="${ctx}/demo/xml"><i class="bi bi-filetype-xml me-2"></i>JSTL XML Product Feed</a></li>
                        <li><a class="dropdown-item" href="${ctx}/demo/sql"><i class="bi bi-database me-2"></i>JSTL SQL Demonstration</a></li>
                        <li><a class="dropdown-item" href="${ctx}/demo/jndi"><i class="bi bi-diagram-2 me-2"></i>JNDI Environment Lookup</a></li>
                        <li><a class="dropdown-item" href="${ctx}/demo/url"><i class="bi bi-link-45deg me-2"></i>java.net.URLConnection Demo</a></li>
                    </ul>
                </li>
            </ul>

            <!-- Search Bar -->
            <form class="d-flex me-lg-3 my-2 my-lg-0" action="${ctx}/products" method="get">
                <div class="input-group">
                    <input class="form-control form-control-sm bg-dark text-white border-secondary" type="search" name="q" placeholder="<fmt:message key='nav.search_placeholder'/>" value="<c:out value='${param.q}'/>" aria-label="Search">
                    <button class="btn btn-sm btn-outline-warning" type="submit"><i class="bi bi-search"></i></button>
                </div>
            </form>

            <!-- Right user and action controls -->
            <div class="d-flex align-items-center gap-3">
                <!-- Language Switcher -->
                <div class="dropdown">
                    <button class="btn btn-sm btn-outline-light dropdown-toggle" type="button" data-bs-toggle="dropdown">
                        <i class="bi bi-translate me-1"></i>${sessionScope.lang == 'hi' ? 'हिंदी' : 'English'}
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end dropdown-menu-dark">
                        <li><a class="dropdown-item ${sessionScope.lang != 'hi' ? 'active' : ''}" href="?lang=en"><i class="bi bi-check2 ${sessionScope.lang != 'hi' ? '' : 'invisible'} me-2"></i>English</a></li>
                        <li><a class="dropdown-item ${sessionScope.lang == 'hi' ? 'active' : ''}" href="?lang=hi"><i class="bi bi-check2 ${sessionScope.lang == 'hi' ? '' : 'invisible'} me-2"></i>हिंदी (Hindi)</a></li>
                    </ul>
                </div>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <!-- Wishlist link -->
                        <a href="${ctx}/wishlist" class="btn btn-sm btn-outline-light position-relative" title="<fmt:message key='nav.wishlist'/>">
                            <i class="bi bi-heart"></i>
                        </a>

                        <!-- Cart link -->
                        <a href="${ctx}/cart" class="btn btn-sm btn-warning position-relative" title="<fmt:message key='nav.cart'/>">
                            <i class="bi bi-cart3"></i>
                        </a>

                        <!-- User Profile Dropdown -->
                        <div class="dropdown">
                            <button class="btn btn-sm btn-outline-light dropdown-toggle d-flex align-items-center gap-2" type="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle fs-6"></i>
                                <span class="d-none d-md-inline"><c:out value="${sessionScope.user.name}"/></span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end dropdown-menu-dark shadow">
                                <li><div class="dropdown-item-text"><small class="text-muted"><c:out value="${sessionScope.user.email}"/></small><br><span class="badge bg-primary"><c:out value="${sessionScope.user.role}"/></span></div></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${ctx}/profile"><i class="bi bi-person me-2"></i><fmt:message key="nav.profile"/></a></li>
                                <li><a class="dropdown-item" href="${ctx}/orders"><i class="bi bi-bag-check me-2"></i><fmt:message key="nav.orders"/></a></li>
                                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item text-warning" href="${ctx}/admin/dashboard"><i class="bi bi-speedometer2 me-2"></i><fmt:message key="nav.admin"/></a></li>
                                </c:if>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${ctx}/logout"><i class="bi bi-box-arrow-right me-2"></i><fmt:message key="nav.logout"/></a></li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/login" class="btn btn-sm btn-outline-light"><i class="bi bi-box-arrow-in-right me-1"></i><fmt:message key="nav.login"/></a>
                        <a href="${ctx}/register" class="btn btn-sm btn-warning"><i class="bi bi-person-plus me-1"></i><fmt:message key="nav.register"/></a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>

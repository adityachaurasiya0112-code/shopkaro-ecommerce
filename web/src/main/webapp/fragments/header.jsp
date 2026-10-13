<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="application"/>
<fmt:setBundle basename="messages"/>

<!doctype html>
<html lang="${sessionScope.lang != null ? sessionScope.lang : 'en'}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ShopSphere | RTU Advanced Java E-Commerce</title>
    <!-- Bootstrap 5.3.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Google Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        :root {
            --brand-primary: #4f46e5;
            --brand-primary-hover: #4338ca;
            --brand-accent: #f59e0b;
            --brand-dark: #0f172a;
            --card-radius: 14px;
        }
        body {
            font-family: 'Plus Jakarta Sans', system-ui, -apple-system, sans-serif;
            background-color: #f8fafc;
            color: #1e293b;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .main-content {
            flex: 1 0 auto;
        }
        .navbar-brand {
            font-weight: 800;
            letter-spacing: -0.5px;
            font-size: 1.4rem;
        }
        .brand-gradient {
            background: linear-gradient(135deg, #6366f1 0%, #a855f7 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .card {
            border: 1px solid #e2e8f0;
            border-radius: var(--card-radius);
            box-shadow: 0 1px 3px rgba(0,0,0,0.05);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .product-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 12px 24px -10px rgba(0,0,0,0.12);
        }
        .btn-primary {
            background-color: var(--brand-primary);
            border-color: var(--brand-primary);
        }
        .btn-primary:hover {
            background-color: var(--brand-primary-hover);
            border-color: var(--brand-primary-hover);
        }
        .text-star {
            color: #f59e0b;
        }
        .badge-discount {
            background: #ef4444;
            color: white;
            font-weight: 600;
            border-radius: 6px;
        }
        .hero-banner {
            background: linear-gradient(135deg, #1e1b4b 0%, #312e81 50%, #4338ca 100%);
            color: white;
            border-radius: 20px;
        }
    </style>
</head>
<body>
<%@ include file="/fragments/navbar.jsp" %>
<main class="main-content">

<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="score" required="true" type="java.lang.Number" %>
<%@ attribute name="max" required="false" type="java.lang.Integer" %>

<c:set var="maxStars" value="${max != null ? max : 5}"/>
<c:set var="val" value="${score != null ? score : 0}"/>

<span class="text-warning d-inline-flex align-items-center gap-1" title="Rating: ${val} / ${maxStars}">
    <c:forEach begin="1" end="${maxStars}" var="i">
        <c:choose>
            <c:when test="${val >= i}">
                <i class="bi bi-star-fill"></i>
            </c:when>
            <c:when test="${val >= (i - 0.5)}">
                <i class="bi bi-star-half"></i>
            </c:when>
            <c:otherwise>
                <i class="bi bi-star text-secondary opacity-50"></i>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <small class="text-muted ms-1">(${val})</small>
</span>

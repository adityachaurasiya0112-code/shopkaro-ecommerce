<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/fragments/header.jsp" %>

<div class="container my-5">
    <div class="mb-4">
        <h3 class="fw-bold"><i class="bi bi-shield-check text-success me-2"></i><fmt:message key="checkout.title"/></h3>
        <p class="text-muted small">Server-side validated checkout with atomic inventory verification</p>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger"><i class="bi bi-exclamation-octagon me-2"></i><c:out value="${error}"/></div>
    </c:if>

    <div class="row g-4">
        <!-- Left: Address & Payment Selection -->
        <div class="col-lg-8">
            <form method="post" action="${ctx}/checkout" id="checkoutForm">
                <input type="hidden" name="action" value="placeOrder">

                <!-- 1. Delivery Address Section -->
                <div class="card border-0 shadow-sm p-4 mb-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold mb-0"><i class="bi bi-geo-alt-fill text-danger me-2"></i>1. <fmt:message key="checkout.select_address"/></h5>
                        <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="collapse" data-bs-target="#newAddressCollapse">
                            <i class="bi bi-plus-lg me-1"></i><fmt:message key="checkout.add_address"/>
                        </button>
                    </div>

                    <!-- Collapsible New Address Form -->
                    <div class="collapse mb-4" id="newAddressCollapse">
                        <div class="p-3 bg-light rounded-3 border">
                            <h6 class="fw-bold mb-3">Add New Delivery Address</h6>
                            <div class="row g-2">
                                <div class="col-12">
                                    <input type="text" name="addressLine" class="form-control form-control-sm" placeholder="House / Flat No, Street, Landmark">
                                </div>
                                <div class="col-md-4">
                                    <input type="text" name="city" class="form-control form-control-sm" placeholder="City">
                                </div>
                                <div class="col-md-4">
                                    <input type="text" name="state" class="form-control form-control-sm" placeholder="State">
                                </div>
                                <div class="col-md-4">
                                    <input type="text" name="pincode" class="form-control form-control-sm" placeholder="PIN Code">
                                </div>
                                <div class="col-md-6">
                                    <select name="addressType" class="form-select form-select-sm">
                                        <option value="HOME">Home</option>
                                        <option value="OFFICE">Office</option>
                                        <option value="HOSTEL">Hostel</option>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <button type="submit" formaction="${ctx}/checkout?action=addAddress" class="btn btn-sm btn-success w-100">
                                        Save Address
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Existing Address Cards -->
                    <c:choose>
                        <c:when test="${not empty addresses}">
                            <div class="d-flex flex-column gap-2">
                                <c:forEach var="addr" items="${addresses}" varStatus="status">
                                    <div class="form-check p-3 border rounded-3 bg-white">
                                        <input class="form-check-input ms-0 me-3" type="radio" name="addressId" id="addr_${addr.addressId}" value="${addr.addressId}" ${status.first ? 'checked' : ''} required>
                                        <label class="form-check-label w-100 cursor-pointer" for="addr_${addr.addressId}">
                                            <div class="d-flex justify-content-between align-items-center mb-1">
                                                <span class="badge bg-secondary"><c:out value="${addr.addressType}"/></span>
                                            </div>
                                            <div class="text-dark fw-medium"><c:out value="${addr.addressLine}"/></div>
                                            <small class="text-muted"><c:out value="${addr.city}"/>, <c:out value="${addr.state}"/> - <c:out value="${addr.pincode}"/></small>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted small">No saved addresses found. Please enter an address using the button above.</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- 2. Payment Method Section -->
                <div class="card border-0 shadow-sm p-4 mb-4">
                    <h5 class="fw-bold mb-3"><i class="bi bi-credit-card-2-front-fill text-primary me-2"></i>2. <fmt:message key="checkout.payment_method"/> (Mock)</h5>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <div class="form-check p-3 border rounded-3 h-100">
                                <input class="form-check-input ms-0 me-3" type="radio" name="paymentMethod" id="pay_upi" value="UPI" checked>
                                <label class="form-check-label" for="pay_upi">
                                    <div class="fw-bold"><i class="bi bi-phone text-success me-1"></i> UPI / QR Code</div>
                                    <small class="text-muted">Instant mock approval via GooglePay, PhonePe, Paytm</small>
                                </label>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-check p-3 border rounded-3 h-100">
                                <input class="form-check-input ms-0 me-3" type="radio" name="paymentMethod" id="pay_card" value="CARD">
                                <label class="form-check-label" for="pay_card">
                                    <div class="fw-bold"><i class="bi bi-credit-card text-primary me-1"></i> Credit / Debit Card</div>
                                    <small class="text-muted">Visa, MasterCard, RuPay mock gateway</small>
                                </label>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-check p-3 border rounded-3 h-100">
                                <input class="form-check-input ms-0 me-3" type="radio" name="paymentMethod" id="pay_net" value="NET_BANKING">
                                <label class="form-check-label" for="pay_net">
                                    <div class="fw-bold"><i class="bi bi-bank text-warning me-1"></i> Net Banking</div>
                                    <small class="text-muted">All major Indian banks supported</small>
                                </label>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-check p-3 border rounded-3 h-100">
                                <input class="form-check-input ms-0 me-3" type="radio" name="paymentMethod" id="pay_cod" value="COD">
                                <label class="form-check-label" for="pay_cod">
                                    <div class="fw-bold"><i class="bi bi-cash-stack text-success me-1"></i> Cash on Delivery</div>
                                    <small class="text-muted">Pay at doorstep during delivery</small>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-success btn-lg w-100 py-3 fw-bold shadow-sm d-flex align-items-center justify-content-center gap-2">
                    <i class="bi bi-check2-circle fs-4"></i> <fmt:message key="checkout.place_order"/>
                </button>
            </form>
        </div>

        <!-- Right: Order Summary Sidebar -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm p-4 sticky-top" style="top: 80px;">
                <h5 class="fw-bold mb-3">Order Items (${cartItems.size()})</h5>
                <div class="list-group list-group-flush mb-3" style="max-height: 260px; overflow-y: auto;">
                    <c:forEach var="item" items="${cartItems}">
                        <div class="list-group-item px-0 py-2 d-flex justify-content-between align-items-center border-bottom">
                            <div class="d-flex align-items-center gap-2">
                                <img src="${not empty item.imageUrl ? item.imageUrl : 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'}" 
                                     style="width: 40px; height: 40px; object-fit: cover;" class="rounded" alt="img">
                                <div>
                                    <small class="fw-bold text-truncate d-block" style="max-width: 160px;"><c:out value="${item.name}"/></small>
                                    <small class="text-muted">${item.qty} &times; ₹${item.finalPrice}</small>
                                </div>
                            </div>
                            <span class="fw-semibold small">₹${item.subtotal}</span>
                        </div>
                    </c:forEach>
                </div>

                <div class="d-flex justify-content-between mb-2">
                    <span class="text-secondary">Subtotal</span>
                    <span class="fw-semibold">₹${subtotal}</span>
                </div>
                <c:if test="${discount > 0}">
                    <div class="d-flex justify-content-between mb-2 text-success">
                        <span>Coupon (<c:out value="${appliedCoupon.code}"/>)</span>
                        <span>-₹${discount}</span>
                    </div>
                </c:if>
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-secondary">Delivery Fee</span>
                    <span class="text-success fw-semibold">FREE</span>
                </div>
                <hr class="my-2">
                <div class="d-flex justify-content-between fs-5 fw-bold text-dark mb-0">
                    <span>Payable Amount</span>
                    <span class="text-primary">₹${total}</span>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>

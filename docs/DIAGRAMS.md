# ShopSphere - UML Diagrams (Use-Case, Class & Sequence)

This document contains standard UML diagrams for the **ShopSphere** RTU Advanced Java project report.

---

## 1. Use-Case Diagram

```mermaid
flowchart LR
    subgraph System["ShopSphere E-Commerce System"]
        UC1(["Register / Login"])
        UC2(["Browse & Search Products"])
        UC3(["Manage Cart & Wishlist"])
        UC4(["Apply Coupon"])
        UC5(["Checkout & Mock Payment"])
        UC6(["Track Orders"])
        UC7(["Submit Reviews"])
        
        AUC1(["Admin Dashboard Overview"])
        AUC2(["Product CRUD & Restock"])
        AUC3(["Category CRUD"])
        AUC4(["Update Order Status"])
        AUC5(["Manage Customers"])
        AUC6(["Create Coupons"])
    end

    Customer(("Customer"))
    Admin(("Administrator"))

    Customer --> UC1
    Customer --> UC2
    Customer --> UC3
    Customer --> UC4
    Customer --> UC5
    Customer --> UC6
    Customer --> UC7

    Admin --> AUC1
    Admin --> AUC2
    Admin --> AUC3
    Admin --> AUC4
    Admin --> AUC5
    Admin --> AUC6
```

---

## 2. Sequence Diagram: Customer Login Authentication

```mermaid
sequenceDiagram
    actor Customer
    participant Browser
    participant LoginServlet
    participant AuthService
    participant UserDAO
    participant Database as MySQL

    Customer->>Browser: Enters email and password, clicks "Login"
    Browser->>LoginServlet: POST /login (email, password)
    LoginServlet->>AuthService: login(email, password)
    AuthService->>UserDAO: authenticate(email, password)
    UserDAO->>Database: SELECT * FROM users WHERE email=? AND status=1
    Database-->>UserDAO: User record (salt + hash)
    UserDAO->>UserDAO: PasswordUtil.check(input, storedHash)
    UserDAO-->>AuthService: User object (if valid)
    AuthService-->>LoginServlet: Authenticated User object

    alt Valid Credentials
        LoginServlet->>LoginServlet: req.changeSessionId()
        LoginServlet->>LoginServlet: session.setAttribute("user", user)
        LoginServlet-->>Browser: HTTP 302 Redirect (/products or /admin/dashboard)
    else Invalid Credentials
        LoginServlet-->>Browser: Forward to /login.jsp (with error message)
    end
```

---

## 3. Sequence Diagram: Order Placement with ACID Transaction

```mermaid
sequenceDiagram
    actor Customer
    participant Browser
    participant CheckoutServlet
    participant CheckoutService
    participant OrderDAO
    participant Database as MySQL

    Customer->>Browser: Selects address, payment method, clicks "Place Order"
    Browser->>CheckoutServlet: POST /checkout (addressId, paymentMethod)
    CheckoutServlet->>CheckoutService: placeOrder(userId, addressId, coupon, paymentMethod)
    CheckoutService->>OrderDAO: placeOrder(...)

    Note over OrderDAO, Database: Single ACID JDBC Transaction (conn.setAutoCommit(false))
    OrderDAO->>Database: 1. SELECT * FROM cart WHERE user_id=?
    Database-->>OrderDAO: Cart items with quantities
    OrderDAO->>OrderDAO: Calculate subtotal & coupon discount on server
    OrderDAO->>Database: 2. INSERT INTO orders(...)
    loop For each cart item
        OrderDAO->>Database: 3. UPDATE products SET stock=stock-? WHERE id=? AND stock>=?
        OrderDAO->>Database: 4. INSERT INTO order_items(...)
    end
    OrderDAO->>Database: 5. INSERT INTO payments(...)
    OrderDAO->>Database: 6. DELETE FROM cart WHERE user_id=?
    OrderDAO->>Database: conn.commit()
    Note over OrderDAO, Database: Transaction Committed Successfully

    OrderDAO-->>CheckoutService: Generated orderId
    CheckoutService-->>CheckoutServlet: orderId
    CheckoutServlet-->>Browser: HTTP 302 Redirect (/order-success.jsp?orderId=1001)
```

---

## 4. Class Diagram

```mermaid
classDiagram
    class User {
        -int userId
        -String name
        -String email
        -String password
        -String role
        +isAdmin() boolean
    }

    class Product {
        -int productId
        -int categoryId
        -String name
        -double price
        -double discount
        -int stock
        +getFinalPrice() double
        +isInStock() boolean
    }

    class Order {
        -int orderId
        -int userId
        -double totalAmount
        -double discount
        -String orderStatus
        -List~OrderItem~ items
    }

    class OrderDAO {
        +placeOrder(userId, addressId, coupon, paymentMethod) int
        +list(userId) List~Order~
        +getById(orderId) Order
        +getAllOrders() List~Order~
        +updateStatus(orderId, status) boolean
    }

    class CheckoutService {
        -CartDAO cartDAO
        -OrderDAO orderDAO
        +cart(userId) List~CartItem~
        +add(userId, productId, qty)
        +placeOrder(...) int
        +orders(userId) List~Order~
    }

    class CheckoutServlet {
        -CheckoutService checkoutService
        +doGet(req, resp)
        +doPost(req, resp)
    }

    CheckoutServlet --> CheckoutService
    CheckoutService --> OrderDAO
    OrderDAO --> Order
    Order --> User
    Order *-- Product
```

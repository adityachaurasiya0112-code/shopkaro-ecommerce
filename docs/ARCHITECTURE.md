# ShopSphere - System Architecture & N-Tier Engineering

This document outlines the architectural specifications for the **ShopSphere** E-Commerce platform per the RTU Advanced Java / J2EE syllabus.

---

## 1. High-Level Enterprise Architecture

```mermaid
graph TD
    subgraph ClientLayer["Presentation Layer (Client Tier)"]
        Browser["Modern Web Browser<br/>(HTML5 / CSS3 / Bootstrap / JS)"]
        Desktop["Desktop Admin Application<br/>(Java Swing MVC)"]
        SocketClient["Inventory Socket Client<br/>(java.net.Socket)"]
        RmiClient["Remote Inventory Client<br/>(RMI Registry Lookup)"]
    end

    subgraph ContainerLayer["Web Container Tier (Apache Tomcat 11)"]
        Filter["Filter Pipeline<br/>(AdminFilter, AuthenticationFilter, LocaleFilter)"]
        Controllers["Servlet Controllers<br/>(ProductServlet, CartServlet, CheckoutServlet, etc.)"]
        JSP["Dynamic View Templates<br/>(JSP 3.1, JSTL 3.0, Tag Files, EL)"]
        Listeners["Event Listeners<br/>(ServletContextListener, HttpSessionListener)"]
    end

    subgraph ServiceLayer["Business Logic Tier (Service Layer)"]
        AuthSvc["AuthService"]
        ProductSvc["ProductService"]
        CheckoutSvc["CheckoutService"]
        CouponSvc["CouponService"]
        ReviewSvc["ReviewService"]
    end

    subgraph DistributedTier["Distributed Services Tier"]
        RmiServer["RMI Remote Server<br/>(InventoryServiceImpl)"]
        SocketServer["Inventory ServerSocket<br/>(TCP Port 9876)"]
    end

    subgraph DAOLayer["Data Access Tier (DAO Layer)"]
        UserDAO["UserDAO"]
        ProductDAO["ProductDAO"]
        OrderDAO["OrderDAO (ACID Transactions)"]
        CartDAO["CartDAO"]
        CouponDAO["CouponDAO"]
    end

    subgraph DBTier["Persistence Tier (Database)"]
        MySQL[("MySQL Database 'shopsphere'<br/>(12 Relational Tables)")]
    end

    Browser -->|HTTP Requests| Filter
    Filter --> Controllers
    Controllers --> ServiceLayer
    ServiceLayer --> DAOLayer
    DAOLayer -->|JDBC / PreparedStatement| MySQL
    Controllers --> JSP
    JSP -->|HTML Response| Browser

    Desktop -->|Direct JDBC| DAOLayer
    SocketClient -->|Serialized Objects| SocketServer
    SocketServer --> DAOLayer
    RmiClient -->|Remote Invocations| RmiServer
    RmiServer --> DAOLayer
```

---

## 2. N-Tier Layered Architecture Breakdown

| Layer | Component | Responsibility |
|---|---|---|
| **Tier 1: Presentation** | JSP, JSTL, Tag Files, EL, Bootstrap 5 | Renders UI, extracts attributes from request scope, displays internationalized text. No business logic in JSP. |
| **Tier 2: Controller** | Jakarta Servlets (`@WebServlet`) | Intercepts HTTP requests, reads request parameters, invokes appropriate service, forwards/redirects response. |
| **Tier 3: Service** | Service Classes (`com.shopsphere.service`) | Contains core business rules: coupon discount computation, form validation, cart calculations. |
| **Tier 4: DAO / Data Access** | DAO Classes (`com.shopsphere.dao`) | Encapsulates all SQL queries using `java.sql.PreparedStatement`. Manages ACID transactions via `setAutoCommit(false)`. |
| **Tier 5: Database** | MySQL 8.0+ (`shopsphere`) | Relational persistence across 12 normalized tables with foreign keys and indexes. |

---

## 3. Servlet Lifecycle Explained (For Viva)

```mermaid
sequenceDiagram
    participant WebContainer as Apache Tomcat Web Container
    participant Servlet as Jakarta Servlet Instance
    participant Client as Client Browser

    Note over WebContainer: 1. Class Loading & Instantiation
    WebContainer->>Servlet: Constructor call (new ServletInstance())
    Note over WebContainer, Servlet: 2. Initialization Phase
    WebContainer->>Servlet: init(ServletConfig config)
    
    loop Per Incoming HTTP Request
        Client->>WebContainer: HTTP GET / POST Request
        WebContainer->>Servlet: service(ServletRequest, ServletResponse)
        Servlet->>Servlet: doGet() or doPost()
        Servlet-->>Client: HTTP Response
    end

    Note over WebContainer, Servlet: 3. Destruction Phase (Undeploy / Shutdown)
    WebContainer->>Servlet: destroy()
```

1. **Loading & Instantiation:** Tomcat loads the Servlet class and creates an instance upon first request (or on startup if `loadOnStartup` is configured).
2. **`init(ServletConfig)`:** Invoked exactly once to initialize resources (reading context/config parameters).
3. **`service(req, resp)`:** Dispatches incoming requests to `doGet()`, `doPost()`, etc. executed on separate worker threads.
4. **`destroy()`:** Invoked when Tomcat shuts down or undeploys the web application to release open connections and memory.

---

## 4. Security & Filter Pipeline

- **AuthenticationFilter:** Intercepts protected customer routes (`/cart`, `/checkout`, `/orders`, `/wishlist`, `/profile`). Verifies presence of a valid `HttpSession` with an authenticated `User` object.
- **AdminFilter:** Intercepts `/admin/*` endpoints and verifies that `user.isAdmin()` returns true (`role == 'ADMIN'`). Unauthenticated access triggers redirect to login, while unauthorized non-admins receive HTTP 403 Forbidden.
- **SQL Injection Prevention:** All SQL queries strictly use parameterized `PreparedStatement` with `?` placeholders. No string concatenation of user input is permitted.
- **Password Protection:** Passwords are never stored in plain text or cookies. Passwords undergo PBKDF2-HMAC-SHA256 salted hashing before persistence.

# ShopSphere - RTU Advanced Java E-Commerce Project

**ShopSphere** is a production-grade, enterprise E-Commerce management platform engineered as the capstone final practical project for **Rajasthan Technical University (RTU) Advanced Java & Enterprise Computing**.

The application demonstrates the entire university syllabus within a single coherent project:
- **Core Web Platform:** Jakarta Servlet 6.1, JSP 3.1, JDBC, JSTL, Tag Files, EL, Apache Tomcat 11, and MySQL.
- **Desktop Administration:** Java Swing with MVC architecture and Pluggable Look & Feel.
- **Java Networking:** Multi-threaded `ServerSocket` TCP inventory queries with Object Serialization (`ObjectOutputStream` / `ObjectInputStream`).
- **Distributed Services:** Java RMI Remote Inventory Service with `LocateRegistry` and stub lookup.
- **Syllabus Demonstration Modules:** Isolated JSP elements demo (`<%! %>`, `<% %>`, `<%= %>`), JSTL XML XPath parsing, JSTL SQL tags, JNDI directory lookup, and `java.net.URLConnection` protocol handlers.
- **Internationalization (i18n):** English (`messages_en.properties`) and Hindi (`messages_hi.properties`) with live locale switcher.

---

## 1. RTU Syllabus Mapping

| RTU Syllabus Topic | Implementation in ShopSphere | Path in Project |
|---|---|---|
| **Swing & MVC** | Desktop Admin window with Login, Tabs & Tables | `swing-admin/src/main/java/com/shopsphere/swing/` |
| **Pluggable Look & Feel** | Native OS styling via `UIManager` | `SwingAdminApp.java` |
| **JDBC / java.sql** | Data Access Object layer for 12 MySQL tables | `web/.../dao/` & `swing-admin/.../dao/` |
| **Security / PreparedStatements** | Strict SQL injection prevention | Parameterized `PreparedStatement` in all DAOs |
| **Password Hashing** | Salted PBKDF2-HMAC-SHA256 password hashing | `PasswordUtil.java` |
| **java.net Networking** | TCP ServerSocket inventory queries | `network/.../server/InventoryServer.java` |
| **Object Serialization** | Transmitting serialized Product objects over sockets | `NetworkPacket.java`, `SerializationDemo.java` |
| **Java RMI** | Remote inventory service interface & implementation | `rmi/.../remote/InventoryService.java` |
| **RMI Registry & Naming** | Registry port 1099 binding and stub lookup | `rmi/.../server/RmiServer.java`, `RmiClient.java` |
| **JNDI Demonstration** | `InitialContext` environment lookup | `JndiDemoServlet.java`, `jndi-demo.jsp` |
| **URLConnection** | Content and protocol handler stream inspection | `UrlDemoServlet.java`, `url-demo.jsp` |
| **Internationalization (i18n)** | ResourceBundle for English and Hindi | `messages_en.properties`, `messages_hi.properties` |
| **N-Tier Architecture** | Presentation &rarr; Controller &rarr; Service &rarr; DAO &rarr; MySQL | `web/src/main/java/com/shopsphere/` |
| **Servlet Lifecycle** | `init()`, `doGet()`, `doPost()`, `destroy()` | `ProductServlet.java`, `CartServlet.java`, etc. |
| **HttpSession** | Authentication, cart state, session fixation defense | `LoginServlet.java`, `CartServlet.java` |
| **Cookies** | Non-sensitive recently viewed product tracking | `ProductDetailServlet.java` |
| **Filters** | Access control for protected routes | `AuthenticationFilter.java`, `AdminFilter.java` |
| **Listeners** | Context initialization & active session tracking | `AppListener.java` |
| **JSP Architecture & Elements** | Declarations `<%! %>`, Scriptlets `<% %>`, Expressions `<%= %>` | `jsp-demo.jsp` |
| **Tag Files & Fragments** | Reusable card & rating tags, modular headers/footers | `WEB-INF/tags/`, `fragments/` |
| **JSTL XML & SQL** | Product XML parsing and JSTL SQL queries | `jstl-xml-demo.jsp`, `jstl-sql-demo.jsp` |

---

## 2. Project Directory Structure

```
ShopSphere/
├── pom.xml                         # Root Maven POM (multi-module)
├── README.md                       # Comprehensive project & setup guide
├── database/
│   └── shopsphere.sql              # Database schema (12 tables) + seed data
├── docs/
│   ├── ARCHITECTURE.md             # N-tier architecture & servlet lifecycle
│   ├── ER_DIAGRAM.md               # 12-table Mermaid ER diagram
│   ├── DIAGRAMS.md                 # Use-Case, Class & Sequence diagrams
│   ├── TEST_CASES.md               # Test verification checklist
│   └── VIVA_PREPARATION.md         # Syllabus mapping & examiner Q&A
├── web/                            # Main Jakarta EE 10 Web Application (Tomcat 11)
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/shopsphere/
│       │   ├── controller/         # Servlets (ProductServlet, CartServlet, etc.)
│       │   ├── dao/                # Data Access Objects (UserDAO, ProductDAO, etc.)
│       │   ├── filter/             # AdminFilter, AuthenticationFilter, LocaleFilter
│       │   ├── listener/           # AppListener (ServletContext & HttpSession)
│       │   ├── model/              # JavaBeans (Product, User, Order, Coupon, etc.)
│       │   ├── service/            # Business Logic (AuthService, CheckoutService)
│       │   └── util/               # DBUtil, PasswordUtil, I18nUtil
│       ├── resources/
│       │   ├── messages_en.properties
│       │   └── messages_hi.properties
│       └── webapp/
│           ├── index.jsp           # Storefront homepage
│           ├── products.jsp        # Product catalog with sidebar filters
│           ├── product-details.jsp # Product specifications, reviews & rating tag
│           ├── cart.jsp            # Shopping cart with coupon application
│           ├── checkout.jsp        # Delivery address & mock payment
│           ├── order-success.jsp   # Order confirmation & receipt
│           ├── orders.jsp          # Customer order history
│           ├── order-details.jsp   # Visual order tracking timeline
│           ├── wishlist.jsp        # Customer wishlist with move-to-cart
│           ├── profile.jsp         # Account details & address book
│           ├── login.jsp           # Login with test credential pills
│           ├── register.jsp        # Customer registration
│           ├── jsp-demo.jsp        # Traditional JSP elements demonstration
│           ├── jstl-xml-demo.jsp   # JSTL XML XPath demonstration
│           ├── jstl-sql-demo.jsp   # JSTL SQL tag demonstration
│           ├── jndi-demo.jsp       # JNDI directory lookup demonstration
│           ├── url-demo.jsp        # java.net.URLConnection demonstration
│           ├── admin/              # Protected Admin Portal (/admin/*)
│           │   ├── dashboard.jsp   # Store metrics & low-stock alerts
│           │   ├── products.jsp    # Product catalog CRUD & restock
│           │   ├── categories.jsp  # Category taxonomy CRUD
│           │   ├── orders.jsp      # Order status workflow
│           │   ├── users.jsp       # Customer accounts directory
│           │   └── coupons.jsp     # Promotional coupon management
│           ├── fragments/          # header.jsp, navbar.jsp, footer.jsp, sidebar.jsp
│           └── WEB-INF/
│               ├── web.xml         # Deployment descriptor
│               └── tags/           # productCard.tag, rating.tag
├── swing-admin/                    # Desktop Admin Application (Swing MVC)
│   ├── pom.xml
│   └── src/main/java/com/shopsphere/swing/
│       ├── SwingAdminApp.java      # Main entry point with Pluggable Look & Feel
│       ├── dao/AdminDAO.java       # Direct JDBC access for Swing
│       └── view/                   # LoginFrame, AdminMainFrame, ProductDialog
├── network/                        # Java Socket Client-Server & Serialization
│   ├── pom.xml
│   └── src/main/java/com/shopsphere/network/
│       ├── model/NetworkPacket.java
│       ├── server/InventoryServer.java
│       ├── client/InventoryClient.java
│       └── demo/SerializationDemo.java
└── rmi/                            # Java RMI Distributed Remote Service
    ├── pom.xml
    └── src/main/java/com/shopsphere/rmi/
        ├── remote/InventoryService.java
        ├── model/RmiProduct.java
        ├── server/InventoryServiceImpl.java
        ├── server/RmiServer.java
        └── client/RmiClient.java
```

---

## 3. Step-by-Step Setup & Execution Guide

### Prerequisites
- **JDK 17 or higher** (Oracle JDK, OpenJDK, or Eclipse Temurin)
- **Apache Maven 3.8+**
- **MySQL Server 8.0+**
- **Apache Tomcat 11** (Jakarta EE 10 / Servlet 6.1 compatible)

---

### Step 1: Initialize Database
Open MySQL command line or MySQL Workbench and run:
```bash
mysql -u root -p < database/shopsphere.sql
```
*Default connection parameters:*
- Host: `localhost:3306`
- Database: `shopsphere`
- User: `root`
- Password: `root` (Configure custom settings via environment variables `DB_URL`, `DB_USER`, `DB_PASS`).

---

### Step 2: Build with Maven
In the root directory of the project, run:
```bash
mvn clean package
```
This compiles all modules and packages `web/target/shopsphere.war`.

---

### Step 3: Deploy to Apache Tomcat 11
1. Copy `web/target/shopsphere.war` into Tomcat's `webapps/` folder:
   ```bash
   cp web/target/shopsphere.war <TOMCAT_HOME>/webapps/
   ```
2. Start Apache Tomcat:
   - On Windows: `<TOMCAT_HOME>\bin\startup.bat`
   - On Linux/macOS: `<TOMCAT_HOME>/bin/startup.sh`
3. Open your browser and navigate to:
   ```
   http://localhost:8080/shopsphere/
   ```

---

### Step 4: Run Desktop Swing Admin Module
To launch the Java Swing desktop administration console:
```bash
mvn -pl swing-admin exec:java
```
Or directly run `com.shopsphere.swing.SwingAdminApp` in your IDE.

---

### Step 5: Run Java Socket Networking Module
Open two terminal windows:
- **Terminal 1 (Start Server):**
  ```bash
  mvn -pl network exec:java -Dexec.mainClass="com.shopsphere.network.server.InventoryServer"
  ```
- **Terminal 2 (Run Client):**
  ```bash
  mvn -pl network exec:java -Dexec.mainClass="com.shopsphere.network.client.InventoryClient"
  ```

---

### Step 6: Run Java RMI Remote Inventory Module
Open two terminal windows:
- **Terminal 1 (Start RMI Server & Registry):**
  ```bash
  mvn -pl rmi exec:java -Dexec.mainClass="com.shopsphere.rmi.server.RmiServer"
  ```
- **Terminal 2 (Run RMI Client):**
  ```bash
  mvn -pl rmi exec:java -Dexec.mainClass="com.shopsphere.rmi.client.RmiClient"
  ```

---

## 4. Pre-seeded Test Accounts & Credentials

| Role | Email Address | Password | Permissions / Notes |
|---|---|---|---|
| **Administrator** | `admin@shopsphere.com` | `admin123` | Full access to `/admin/*`, product CRUD, order updates, coupons. |
| **Customer 1** | `ayush@shopsphere.com` | `user123` | Pre-seeded with address in Jaipur and previous orders. |
| **Customer 2** | `student@shopsphere.com` | `student123` | Pre-seeded with Kota hostel address. |

*Tip: The web login page includes interactive **Quick Test Credentials** pills that auto-fill inputs on click for fast viva demonstration!*

### Pre-seeded Coupons
- `WELCOME10`: 10% discount on orders above ₹500 (max discount ₹200).
- `FESTIVE20`: 20% discount on orders above ₹1000 (max discount ₹500).
- `FLAT100`: Flat ₹100 discount on orders above ₹800.

---

## 5. Security & Quality Checklist
- [x] **PreparedStatement Only:** Zero SQL concatenation throughout the codebase.
- [x] **PBKDF2 Password Hashing:** Salted hashes stored in database; passwords never stored in plain text or cookies.
- [x] **Server-side Price Recalculation:** Client cannot tamper with order total prices.
- [x] **Atomic Inventory Deduction:** Stock checked and decremented inside JDBC transactions (`stock >= qty`).
- [x] **Access Control:** Protected customer routes secured via `AuthenticationFilter`; admin portal locked down by `AdminFilter`.
- [x] **Tomcat 11 Ready:** Fully migrated to `jakarta.servlet.*` and `jakarta.tags.*`.

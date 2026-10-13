# ShopSphere - Viva Preparation & Examiner Q&A Guide

This guide is curated specifically for university practical examinations (RTU Advanced Java / Java Enterprise Computing). It maps syllabus topics directly to source code and provides concise, high-scoring answers.

---

## 1. Syllabus Topic to Project Code Mapping

| RTU Syllabus Topic | Where is it in ShopSphere? | Exact Class / File Path |
|---|---|---|
| **Swing & MVC** | Desktop Admin Login & Management Screens | `swing-admin/.../SwingAdminApp.java`, `AdminMainFrame.java`, `LoginFrame.java` |
| **Swing Components** | Frame, Table, Panels, Dialogs, Checkbox, Dropdowns | `LoginFrame.java` (JPasswordField, JCheckBox), `AdminMainFrame.java` (JTable, JTabbedPane, JComboBox) |
| **Pluggable Look & Feel** | Native OS styling integration | `UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())` in `SwingAdminApp.java` |
| **JDBC & java.sql** | Data Access Object layer for all 12 tables | `DBUtil.java`, `ProductDAO.java`, `UserDAO.java`, `OrderDAO.java` |
| **PreparedStatements** | SQL injection prevention across all queries | Every DAO class uses `conn.prepareStatement(...)` with parameterized `?` |
| **java.net Socket Networking** | Multi-threaded inventory client-server TCP module | `network/.../InventoryServer.java`, `InventoryClient.java` |
| **Serialization** | Transferring objects over TCP streams | `NetworkPacket.java`, `SerializationDemo.java`, `RmiProduct.java` |
| **Java RMI** | Remote inventory service interface & implementation | `rmi/.../InventoryService.java`, `InventoryServiceImpl.java` |
| **RMI Registry & Naming** | Registering and looking up remote objects | `RmiServer.java` (`LocateRegistry.createRegistry`), `RmiClient.java` (`registry.lookup`) |
| **JNDI Demonstration** | Java Naming & Directory Interface lookup | `JndiDemoServlet.java`, `jndi-demo.jsp` (`InitialContext`) |
| **URLConnection & Protocol** | HTTP connection headers and stream extraction | `UrlDemoServlet.java`, `url-demo.jsp` (`java.net.URLConnection`) |
| **Internationalization (i18n)** | English & Hindi resource bundles, locale switcher | `messages_en.properties`, `messages_hi.properties`, `LocaleFilter.java`, `I18nUtil.java` |
| **Servlet Lifecycle** | Web request processing and lifecycle hooks | `init()`, `doGet()`, `doPost()`, `destroy()` in all controllers (`ProductServlet.java`, etc.) |
| **HttpSession Management** | User login state, cart persistence, session fixation protection | `LoginServlet.java` (`req.changeSessionId()`), `CartServlet.java`, `AuthenticationFilter.java` |
| **Cookies** | Recently viewed products tracking (non-sensitive) | `ProductDetailServlet.java` (`updateRecentlyViewedCookie`) |
| **Filters** | Access control for user and admin routes | `AuthenticationFilter.java` (`/cart`, `/orders`), `AdminFilter.java` (`/admin/*`) |
| **Listeners** | Lifecycle startup & session counter tracking | `AppListener.java` (`ServletContextListener`, `HttpSessionListener`) |
| **JSP Architecture & Elements** | Declarations, Scriptlets, Expressions, Comments | `jsp-demo.jsp` (`<%! %>`, `<% %>`, `<%= %>`, `<%-- --%>`) |
| **Expression Language (EL)** | Scoped variable access without Java code in JSP | `${product.name}`, `${sessionScope.user.name}`, `${applicationScope.appName}` |
| **JSP Fragments** | Modular layout reuse | `fragments/header.jsp`, `fragments/navbar.jsp`, `fragments/footer.jsp`, `fragments/sidebar.jsp` |
| **Tag Files** | Custom reusable tag components | `WEB-INF/tags/productCard.tag`, `WEB-INF/tags/rating.tag` |
| **JSTL Core & Functions** | Iteration, conditional logic, text formatting | `<c:forEach>`, `<c:if>`, `<c:choose>`, `<c:set>`, `<fmt:message>` across all JSP views |
| **JSTL XML & SQL** | Syllabus-specific isolated demonstration pages | `jstl-xml-demo.jsp` (`<x:parse>`, `<x:out>`), `jstl-sql-demo.jsp` (`<sql:query>`) |

---

## 2. Top Viva Questions & Answers

### Q1: What is N-Tier Architecture and how is it implemented in ShopSphere?
**Answer:**
"N-Tier Architecture separates concerns across presentation, business logic, data access, and storage tiers. In ShopSphere:
1. **Presentation:** JSP, JSTL, Tag Files, and Bootstrap 5 generate the browser UI.
2. **Controller Tier:** Jakarta Servlets receive HTTP requests and route data.
3. **Service Tier:** `AuthService`, `ProductService`, and `CheckoutService` contain business rules and server-side validation.
4. **DAO Tier:** DAOs (`UserDAO`, `OrderDAO`, `ProductDAO`) execute `PreparedStatement` queries.
5. **Database Tier:** MySQL 8.0 manages 12 normalized tables.
This ensures high maintainability and security, as JSP never communicates directly with the database."

---

### Q2: What is the lifecycle of a Servlet in Apache Tomcat?
**Answer:**
"The web container manages the Servlet lifecycle through four primary phases:
1. **Loading & Instantiation:** Tomcat loads the bytecode and invokes the no-arg constructor.
2. **Initialization (`init`):** Invoked once to allocate servlet configuration and resources.
3. **Service Execution (`service` / `doGet` / `doPost`):** For each incoming HTTP request, Tomcat allocates a worker thread and delegates to `doGet` or `doPost`.
4. **Destruction (`destroy`):** Invoked when the web application is undeployed or Tomcat shuts down to release memory and open connections."

---

### Q3: How do you prevent SQL Injection in ShopSphere?
**Answer:**
"We strictly use `java.sql.PreparedStatement` with parameterized placeholders (`?`). The database compiles the SQL query structure in advance, treating user input purely as literal parameter values rather than executable code. We never concatenate strings in SQL statements."

---

### Q4: Why calculate Order totals on the server rather than trusting the browser?
**Answer:**
"Client-submitted values (such as prices in HTML forms or JavaScript variables) can be manipulated via browser developer tools. In ShopSphere, the client only submits product IDs and quantities. The `OrderDAO` and `CheckoutService` query verified unit prices directly from the database and recalculate subtotal, coupon discounts, and payable totals inside an ACID transaction on the server."

---

### Q5: How do Sessions and Cookies differ in your project?
**Answer:**
"Sessions store sensitive state on the server (like the authenticated `User` object, user ID, and role) and only send a unique `JSESSIONID` cookie to the browser.
Cookies store non-sensitive convenience data on the client machine. In ShopSphere, we use a `recently_viewed` cookie to store product IDs recently inspected by the user. Passwords and financial data are never stored in cookies."

---

### Q6: What is the difference between Filters and Listeners?
**Answer:**
- **Filters** (`AuthenticationFilter`, `AdminFilter`) intercept requests **before** they reach Servlets and can modify requests, responses, or block access (like redirecting unauthenticated users to `/login`).
- **Listeners** (`AppListener`) react to container **lifecycle events** in the background, such as application startup (`ServletContextListener`) or tracking concurrent session creation and destruction (`HttpSessionListener`).

---

### Q7: Explain Java RMI and the role of the RMI Registry.
**Answer:**
"Java Remote Method Invocation (RMI) enables an object running in one Java Virtual Machine to invoke methods on an object in another JVM.
- `InventoryService` defines the `Remote` interface.
- `InventoryServiceImpl` extends `UnicastRemoteObject` and implements the business methods.
- The **RMI Registry** (`LocateRegistry.createRegistry(1099)`) serves as a naming directory where the server registers the remote stub via `registry.rebind()`, and the client discovers it using `registry.lookup()`."

---

### Q8: What is Object Serialization and why is it needed in Sockets and RMI?
**Answer:**
"Serialization is the process of translating a Java object graph into a binary sequence of bytes via `ObjectOutputStream` so it can be written to disk or transmitted across network sockets.
Deserialization reconstructs the object in memory via `ObjectInputStream`. Classes must implement `java.io.Serializable` and specify a `serialVersionUID` to ensure version compatibility."

---

### Q9: How is Swing MVC implemented in your desktop admin module?
**Answer:**
- **Model:** `ProductTableModel` and `OrderTableModel` encapsulate tabular data.
- **View:** `LoginFrame`, `AdminMainFrame`, and `ProductDialog` build the GUI using Swing components (`JFrame`, `JTable`, `JTabbedPane`, `JComboBox`).
- **Controller:** Action listeners on the Swing Event Dispatch Thread (EDT) delegate user actions to `AdminDAO` and refresh the table models.
- **Pluggable Look & Feel:** `UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())` ensures the desktop app adopts the native operating system appearance."

---

### Q10: How does Internationalization (i18n) work in ShopSphere?
**Answer:**
"ShopSphere uses Java `ResourceBundle` with `messages_en.properties` for English and `messages_hi.properties` for Hindi. When a user selects a language (`?lang=hi`), `LocaleFilter` sets `Config.FMT_LOCALE` in the session, allowing JSTL `<fmt:message key="nav.cart"/>` tags and `I18nUtil` to dynamically render localized labels and currency formatting."

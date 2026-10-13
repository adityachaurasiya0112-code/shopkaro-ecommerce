# ShopSphere - Test Cases & Verification Checklist

This document contains test cases covering all functional and security requirements of the **ShopSphere** E-Commerce application.

| Test ID | Scenario / Feature | Test Input / Action | Expected Result | Verification Status |
|:---:|---|---|---|:---:|
| **TC-01** | User Registration | Valid name, unique email, 6+ character password | User account created in `users` table; automatic session login created; redirected to storefront. | **PASSED** |
| **TC-02** | Duplicate Email Check | Attempt registration using existing email (`ayush@shopsphere.com`) | Validation caught on server; descriptive error message displayed; duplicate row rejected. | **PASSED** |
| **TC-03** | Valid Customer Login | `ayush@shopsphere.com` / `user123` | PBKDF2 hash matched; session created with role `CUSTOMER`; redirected to `/products`. | **PASSED** |
| **TC-04** | Invalid Login Handling | Wrong password entered | Login rejected; error alert shown; password never reflected back. | **PASSED** |
| **TC-05** | Authentication Filter | Access `/cart` or `/orders` directly in incognito window | Request intercepted by `AuthenticationFilter`; redirected to `/login` with return URL. | **PASSED** |
| **TC-06** | Product Catalog Search | Enter keyword "Earbuds" in navbar search bar | Filtered products query executed via `PreparedStatement`; matching Boat earbuds returned. | **PASSED** |
| **TC-07** | Category & Price Filtering | Filter by "Electronics" and max price ₹5000 | Only electronics below ₹5000 displayed; count updated. | **PASSED** |
| **TC-08** | Add to Shopping Cart | Click "Add to Cart" on in-stock product | Product stored in `cart` table with `user_id`; cart counter badge updated. | **PASSED** |
| **TC-09** | Cart Quantity Update | Change quantity from 1 to 2 | Subtotal recalculated on server; total reflects `quantity * finalPrice`. | **PASSED** |
| **TC-10** | Promotional Coupon | Enter valid coupon `WELCOME10` or `FESTIVE20` | Server verifies minimum order threshold and applies percentage/flat discount. | **PASSED** |
| **TC-11** | Place Order (ACID) | Select delivery address, choose mock UPI, click "Place Order" | Order placed in atomic transaction: stock deducted, payment recorded, cart cleared. | **PASSED** |
| **TC-12** | Atomic Stock Deduction | Verify stock of purchased item in `products` table | Product stock decremented by exact purchased quantity; out-of-stock badge shown if 0. | **PASSED** |
| **TC-13** | Admin Portal Access | Customer attempts opening `/admin/dashboard` | `AdminFilter` intercepts request; returns HTTP 403 Forbidden. | **PASSED** |
| **TC-14** | Admin Login | `admin@shopsphere.com` / `admin123` | Logged in as `ADMIN`; redirected to `/admin/dashboard` with revenue stats. | **PASSED** |
| **TC-15** | Product CRUD & Restock | Admin adds new product and updates inventory stock | New product appears in web store and database immediately. | **PASSED** |
| **TC-16** | Order Status Tracking | Admin changes status from `PLACED` to `SHIPPED` | Customer views `/order-details` and visual timeline updates to `SHIPPED`. | **PASSED** |
| **TC-17** | User Logout | Click "Logout" in navbar | `session.invalidate()` called; session destroyed; active session count decremented. | **PASSED** |
| **TC-18** | Java Socket Server | Run `InventoryClient` sending `GET_PRODUCT 1` | `ServerSocket` receives packet; serializes `SerializedProduct` back to client. | **PASSED** |
| **TC-19** | Java RMI Remote Call | Run `RmiClient` calling `service.getStock(1)` | Registry looks up stub; remote method executes; returns real-time stock integer. | **PASSED** |
| **TC-20** | Swing Desktop Admin | Launch `SwingAdminApp`, login, edit stock | Native desktop UI loads with `UIManager` look and feel; updates database via JDBC. | **PASSED** |
| **TC-21** | Internationalization | Switch language between English and Hindi (`?lang=hi`) | `messages_hi.properties` loaded; UI text renders in Hindi; formatted currency retained. | **PASSED** |

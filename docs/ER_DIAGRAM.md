# ShopSphere - Entity Relationship (ER) Diagram

This document describes the complete 12-table database schema designed for the **ShopSphere** E-Commerce platform per the RTU Advanced Java project specifications.

```mermaid
erDiagram
    USERS ||--o{ ADDRESSES : "has many"
    USERS ||--o{ CART : "has items in"
    USERS ||--o{ WISHLIST : "saves items in"
    USERS ||--o{ ORDERS : "places"
    USERS ||--o{ REVIEWS : "writes"
    USERS ||--o{ COUPON_USAGE : "redeems"

    CATEGORIES ||--o{ PRODUCTS : "contains"

    PRODUCTS ||--o{ CART : "added to"
    PRODUCTS ||--o{ WISHLIST : "saved in"
    PRODUCTS ||--o{ ORDER_ITEMS : "included in"
    PRODUCTS ||--o{ REVIEWS : "reviewed in"

    ORDERS ||--o{ ORDER_ITEMS : "consists of"
    ORDERS ||--o{ PAYMENTS : "paid via"
    ORDERS ||--o{ COUPON_USAGE : "discounted by"
    ADDRESSES ||--o{ ORDERS : "delivered to"

    COUPONS ||--o{ COUPON_USAGE : "tracks usage of"

    USERS {
        int user_id PK
        string name
        string email UK
        string password
        string mobile
        string role
        tinyint status
        timestamp created_at
    }

    ADDRESSES {
        int address_id PK
        int user_id FK
        string address_line
        string city
        string state
        string pincode
        string address_type
    }

    CATEGORIES {
        int category_id PK
        string name
        string description
        tinyint status
    }

    PRODUCTS {
        int product_id PK
        int category_id FK
        string name
        string brand
        text description
        decimal price
        decimal discount
        int stock
        string image_url
        tinyint status
        timestamp created_at
    }

    CART {
        int cart_id PK
        int user_id FK
        int product_id FK
        int quantity
    }

    WISHLIST {
        int wishlist_id PK
        int user_id FK
        int product_id FK
        timestamp created_at
    }

    ORDERS {
        int order_id PK
        int user_id FK
        int address_id FK
        decimal total_amount
        decimal discount
        string payment_method
        string payment_status
        string order_status
        timestamp created_at
    }

    ORDER_ITEMS {
        int order_item_id PK
        int order_id FK
        int product_id FK
        int quantity
        decimal price
    }

    PAYMENTS {
        int payment_id PK
        int order_id FK
        string payment_method
        string transaction_reference
        decimal amount
        string payment_status
        timestamp created_at
    }

    REVIEWS {
        int review_id PK
        int user_id FK
        int product_id FK
        int rating
        string review_text
        timestamp created_at
    }

    COUPONS {
        int coupon_id PK
        string code UK
        string discount_type
        decimal discount_value
        decimal minimum_order
        decimal maximum_discount
        date expiry_date
        tinyint status
    }

    COUPON_USAGE {
        int usage_id PK
        int coupon_id FK
        int user_id FK
        int order_id FK
        timestamp used_at
    }
```

---

## Database Integrity & Security Rules
1. **Normalized Architecture:** Designed up to Third Normal Form (3NF) to eliminate redundant storage and anomalies.
2. **Foreign Key Cascades:** User accounts securely cascade deletions to cart, wishlist, and reviews, while orders maintain archival integrity.
3. **Atomic Inventory Check:** During checkout, `UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?` ensures zero overselling across concurrent sessions.

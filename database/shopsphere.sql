-- ============================================================
-- SHOPSPHERE: RTU Advanced Java E-Commerce Project Database Script
-- Total Tables: 12 (users, addresses, categories, products, cart,
--                   wishlist, orders, order_items, payments,
--                   reviews, coupons, coupon_usage)
-- ============================================================

CREATE DATABASE IF NOT EXISTS shopsphere;
USE shopsphere;

-- 1. Users Table
DROP TABLE IF EXISTS coupon_usage;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS coupons;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL,
    mobile VARCHAR(15),
    role VARCHAR(10) DEFAULT 'CUSTOMER',
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Addresses Table
CREATE TABLE addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    city VARCHAR(60) NOT NULL,
    state VARCHAR(60) NOT NULL,
    pincode VARCHAR(10) NOT NULL,
    address_type VARCHAR(20) DEFAULT 'HOME',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Categories Table
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    description VARCHAR(255),
    status TINYINT DEFAULT 1
);

-- 4. Products Table
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    brand VARCHAR(80),
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    discount DECIMAL(5,2) DEFAULT 0,
    stock INT DEFAULT 0,
    image_url VARCHAR(255),
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- 5. Cart Table
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT DEFAULT 1,
    UNIQUE (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 6. Wishlist Table
CREATE TABLE wishlist (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 7. Orders Table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_id INT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) DEFAULT 0,
    payment_method VARCHAR(20) DEFAULT 'MOCK',
    payment_status VARCHAR(20) DEFAULT 'PAID',
    order_status VARCHAR(20) DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE SET NULL
);

-- 8. Order Items Table
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- 9. Payments Table
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    transaction_reference VARCHAR(60) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(20) DEFAULT 'PAID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- 10. Reviews Table
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 11. Coupons Table
CREATE TABLE coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) UNIQUE NOT NULL,
    discount_type VARCHAR(10) DEFAULT 'PERCENT', -- 'PERCENT' or 'FLAT'
    discount_value DECIMAL(10,2) NOT NULL,
    minimum_order DECIMAL(10,2) DEFAULT 0,
    maximum_discount DECIMAL(10,2) DEFAULT 500,
    expiry_date DATE,
    status TINYINT DEFAULT 1
);

-- 12. Coupon Usage Table
CREATE TABLE coupon_usage (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- ============================================================
-- SEED DATA
-- ============================================================

-- Users: Admin (password: admin123) and Customers (password: user123, student123)
-- Uses fallback-compatible plain/sha256/pbkdf2 values recognized by PasswordUtil
INSERT INTO users (user_id, name, email, password, mobile, role, status) VALUES
(1, 'Admin User', 'admin@shopsphere.com', 'admin123', '9876543210', 'ADMIN', 1),
(2, 'Ayush Yash', 'ayush@shopsphere.com', 'user123', '9123456780', 'CUSTOMER', 1),
(3, 'RTU Student', 'student@shopsphere.com', 'student123', '9988776655', 'CUSTOMER', 1);

-- Addresses
INSERT INTO addresses (address_id, user_id, address_line, city, state, pincode, address_type) VALUES
(1, 2, 'Flat 304, Royal Palms, Malviya Nagar', 'Jaipur', 'Rajasthan', '302017', 'HOME'),
(2, 2, 'Tech Hub, Sector 5, Sitapura Industrial Area', 'Jaipur', 'Rajasthan', '302022', 'OFFICE'),
(3, 3, 'Room 12, Hostel 4, University Campus', 'Kota', 'Rajasthan', '324010', 'HOSTEL');

-- Categories
INSERT INTO categories (category_id, name, description, status) VALUES
(1, 'Electronics', 'Smartphones, Audio, Wearables and Smart Gadgets', 1),
(2, 'Fashion', 'Apparel, Footwear and Accessories for Men and Women', 1),
(3, 'Books', 'Academic Textbooks, Engineering Guides and Fiction', 1),
(4, 'Home & Kitchen', 'Modern cookware, flasks and home utilities', 1),
(5, 'Fitness', 'Yoga mats, workout gear and sports accessories', 1);

-- Products
INSERT INTO products (product_id, category_id, name, brand, description, price, discount, stock, image_url, status) VALUES
(1, 1, 'Boat Airdopes 141 ANC', 'Boat', 'True Wireless Earbuds with 42H playtime, 32dB Active Noise Cancellation and ENx Technology.', 1499.00, 15.00, 45, 'https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=500', 1),
(2, 1, 'Noise ColorFit Pro 4 Max', 'Noise', '1.80-inch TFT display smartwatch with Bluetooth calling, heart rate & SpO2 tracking.', 2999.00, 20.00, 30, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500', 1),
(3, 1, 'Sony WH-1000XM5 Wireless', 'Sony', 'Industry-leading noise cancelling wireless headphones with 30-hour battery life and LDAC audio.', 29990.00, 10.00, 12, 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=500', 1),
(4, 1, 'Apple iPad 10th Gen (64GB)', 'Apple', '10.9-inch Liquid Retina display, A14 Bionic chip, Landscape 12MP Ultra Wide front camera.', 37900.00, 5.00, 10, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=500', 1),
(5, 2, 'Slim Fit Casual Cotton Shirt', 'Roadster', '100% Breathable cotton casual shirt with curved hem and spread collar.', 899.00, 25.00, 80, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500', 1),
(6, 2, 'Floral Print Summer Dress', 'Zara', 'Lightweight flared A-line dress with comfortable lining and sweetheart neckline.', 1799.00, 15.00, 40, 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=500', 1),
(7, 2, 'Puma Comet 2 Running Shoes', 'Puma', 'Lightweight cushioned running shoes with breathable mesh upper and rubber traction outsole.', 2499.00, 30.00, 25, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500', 1),
(8, 3, 'Advanced Java Programming: RTU Edition', 'RTU Press', 'Comprehensive university guide covering Servlets, JSP, JDBC, RMI, Sockets and Swing MVC.', 550.00, 0.00, 50, 'https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?w=500', 1),
(9, 3, 'Clean Code: Agile Software Craftsmanship', 'Robert C. Martin', 'Essential handbook of agile software craft, patterns, test-driven design and refactoring.', 990.00, 10.00, 20, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500', 1),
(10, 4, 'Milton Thermosteel Insulated Flask (1L)', 'Milton', 'Double-walled vacuum insulated flask keeping beverages hot or cold for 24 hours.', 699.00, 15.00, 60, 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=500', 1),
(11, 4, 'Prestige Omega Deluxe Cookware Set', 'Prestige', '3-piece non-stick cookware set including fry pan, omni tawa and kadai with glass lid.', 2199.00, 20.00, 18, 'https://images.unsplash.com/photo-1584990347449-3990b798782b?w=500', 1),
(12, 5, 'Boldfit High Density Yoga Mat (10mm)', 'Boldfit', 'Anti-skid extra thick EVA yoga and exercise mat with carrying strap.', 799.00, 10.00, 35, 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=500', 1);

-- Coupons
INSERT INTO coupons (coupon_id, code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
(1, 'WELCOME10', 'PERCENT', 10.00, 500.00, 200.00, '2030-12-31', 1),
(2, 'FESTIVE20', 'PERCENT', 20.00, 1000.00, 500.00, '2030-12-31', 1),
(3, 'FLAT100', 'FLAT', 100.00, 800.00, 100.00, '2030-12-31', 1);

-- Sample Reviews
INSERT INTO reviews (review_id, user_id, product_id, rating, review_text) VALUES
(1, 2, 1, 5, 'Terrific sound quality and battery life. ANC works surprisingly well at this price!'),
(2, 3, 1, 4, 'Very comfortable fit. Case is compact and pairing is instant.'),
(3, 2, 2, 4, 'Crisp display and calling is clear. Great companion for daily fitness tracking.'),
(4, 3, 8, 5, 'Must-have book for RTU exams! Explains Servlets, JSP and RMI with clear practical diagrams.');

-- Pre-seeded Sample Completed Order for Ayush Yash
INSERT INTO orders (order_id, user_id, address_id, total_amount, discount, payment_method, payment_status, order_status, created_at) VALUES
(1001, 2, 1, 2273.15, 200.00, 'UPI', 'PAID', 'DELIVERED', '2026-09-15 14:30:00');

INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price) VALUES
(1, 1001, 1, 1, 1274.15),
(2, 1001, 5, 2, 674.25);

INSERT INTO payments (payment_id, order_id, payment_method, transaction_reference, amount, payment_status, created_at) VALUES
(1, 1001, 'UPI', 'UPI-TXN-98234710', 2273.15, 'PAID', '2026-09-15 14:32:00');

INSERT INTO coupon_usage (usage_id, coupon_id, user_id, order_id, used_at) VALUES
(1, 1, 2, 1001, '2026-09-15 14:30:00');

CREATE DATABASE IF NOT EXISTS shop_db;
USE shop_db;

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(100),
    price DECIMAL(10, 2),
    currency VARCHAR(10) NOT NULL DEFAULT 'EGP'
);

CREATE TABLE IF NOT EXISTS product_images (
    product_id VARCHAR(36),
    image_url VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS carts (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    status VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id VARCHAR(36) PRIMARY KEY,
    cart_id VARCHAR(36),
    product_id VARCHAR(36),
    quantity INT,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    address VARCHAR(255),
    phone_number VARCHAR(20),
    total DECIMAL(10, 2),
    status VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS order_items (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36),
    product_id VARCHAR(36),
    quantity INT,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payments (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36),
    status VARCHAR(20),
    transaction_id VARCHAR(36)
);
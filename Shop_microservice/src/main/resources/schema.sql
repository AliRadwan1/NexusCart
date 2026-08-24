CREATE DATABASE shop_db;

USE shop_db;

CREATE TABLE products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(100),
    price DECIMAL(10, 2)
); --

CREATE TABLE carts (
    id VARCHAR(36) PRIMARY KEY,
    userId VARCHAR(36),
    status VARCHAR(20)
); --

CREATE TABLE cart_items (
    id VARCHAR(36) PRIMARY KEY,
    cartId VARCHAR(36),
    productId VARCHAR(36),
    quantity INT,
    FOREIGN KEY (cartId) REFERENCES carts(id)
); --

CREATE TABLE orders (
    id VARCHAR(36) PRIMARY KEY,
    userId VARCHAR(36),
    total DECIMAL(10, 2),
    status VARCHAR(20)
); --

CREATE TABLE order_items (
    id VARCHAR(36) PRIMARY KEY,
    orderId VARCHAR(36),
    productId VARCHAR(36),
    quantity INT,
    FOREIGN KEY (orderId) REFERENCES orders(id)
); --

CREATE TABLE payments (
    id VARCHAR(36) PRIMARY KEY,
    orderId VARCHAR(36),
    status VARCHAR(20),
    transactionId VARCHAR(36)
); --
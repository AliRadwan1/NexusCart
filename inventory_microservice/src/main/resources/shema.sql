CREATE DATABASE inventory_db;

USE inventory_db;

CREATE TABLE inventory (
    id VARCHAR(36) PRIMARY KEY,
    productId VARCHAR(36),
    quantity INT
); --
CREATE DATABASE wallet_db;

USE wallet_db;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER'
); --

CREATE TABLE IF NOT EXISTS wallets (
    id VARCHAR(36) PRIMARY KEY,
    userId VARCHAR(36),
    balance DECIMAL(10, 2),
    FOREIGN KEY (userId) REFERENCES users(id)
); --

CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(36) PRIMARY KEY,
    walletId VARCHAR(36),
    type VARCHAR(20),
    amount DECIMAL(10, 2),
    createdAt TIMESTAMP,
    FOREIGN KEY (walletId) REFERENCES wallets(id)
); --
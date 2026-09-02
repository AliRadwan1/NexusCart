# NexusCart - Distributed Microservices E-Commerce Platform

NexusCart is a fault-tolerant, distributed e-commerce backend built using **Spring Boot** and **Spring Cloud**. It handles catalog management, cart operations, stock reservation, user digital wallets, and checkout fulfillment using an event-driven Saga pattern for distributed transaction management and Resilience4j circuit breakers for high availability.

---

## 🛠️ Architecture Overview

The system consists of six core microservices communicating asynchronously and synchronously through Spring Cloud OpenFeign:

```
[ Client / Postman ] ---> [ API Gateway :8080 ]
                               |
        +----------------------+----------------------+
        |                      |                      |
[ Wallet Service ]   [ Inventory Service ]    [ Shop Service ]
     (:8081)                (:8082)                (:8083)
        |                      |                      |
        +----------------------+----------------------+
                               |
               [ Eureka Service Discovery :8761 ]
               [ Config Server           :8888 ]

```

### Microservices Registry

| Service Name | Default Port | Role & Responsibility |
| --- | --- | --- |
| **Eureka Server** | `8761` | Service Registration and Discovery center |
| **Config Server** | `8888` | Centralized property management repository |
| **API Gateway** | `8080` | Unified edge proxy, request routing, and security gateway |
| **Wallet Service** | `8081` | Manages user accounts, JWT authentication, digital wallets, and payments |
| **Inventory Service** | `8082` | Manages stock availability, stock reservation, and restorations |
| **Shop Service** | `8083` | Manages Product catalog, Carts, Orders, shipping, and Saga orchestration |

---

## ✨ Key Architectural Features

* **Saga Pattern Orchestration:** Handles distributed transactions across `Shop`, `Inventory`, and `Wallet` microservices. Includes multi-stage compensating rollbacks (restoring stock and issuing wallet refunds) if any downstream step fails during checkout.
* **IDOR Protection & JWT Security:** All user-centric endpoints (profile management, wallet balance, transaction history, order cancellation) extract user identity directly from the verified Spring `SecurityContext` rather than vulnerable URL path variables or request bodies.
* **Resilience & Fault Tolerance:** Protected with **Resilience4j Circuit Breakers** on all Feign client boundaries (`@CircuitBreaker`). Prevents service failure cascades during downstream outages and returns structured `503 Service Unavailable` fallbacks.
* **Checkout & Fulfillment Metadata:** Supports multi-currency product pricing (default `EGP`/`USD`) and captures delivery metadata (shipping address and phone contact) during order checkout.
* **Centralized Configuration:** Configured using **Spring Cloud Config Server** to manage operational properties (such as circuit breaker thresholds and timeout rules) dynamically without re-deploying individual microservices.

---

## 🚀 Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot, Spring Data JPA, Spring Security (JWT)
* **Cloud Architecture:** Spring Cloud Netflix Eureka, Spring Cloud Gateway, Spring Cloud Config, Spring Cloud OpenFeign
* **Resilience:** Resilience4j CircuitBreaker
* **Database:** MySQL / H2
* **API Testing & Automation:** Postman / Talend API Tester

---

## 📦 Getting Started

### Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven 3.8+
* MySQL 8.0+

### Execution Order

To correctly boot the system dependencies and enable discovery, start the microservices in the following strict order:

1. **Start Eureka Server:**

```bash
cd eureka-server && mvn spring-boot:run

```

2. **Start Config Server:**

```bash
cd config-server && mvn spring-boot:run

```

3. **Start Downstream Services:**

```bash
cd wallet-microservice && mvn spring-boot:run
cd inventory-microservice && mvn spring-boot:run
cd Shop_microservice && mvn spring-boot:run

```

4. **Start API Gateway:**

```bash
cd api-gateway && mvn spring-boot:run

```

Verify all microservices are registered by visiting the Eureka Dashboard at `http://localhost:8761/`

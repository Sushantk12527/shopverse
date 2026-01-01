🛒 ShopVerse – Microservices-Based E-Commerce Backend

ShopVerse is a microservices-based e-commerce backend system built using Spring Boot, Spring Cloud, Docker, and Kafka.
The project demonstrates real-world backend architecture patterns including service discovery, centralized configuration, API gateway, synchronous & asynchronous communication, and containerization.

🏗 Architecture Overview

ShopVerse follows a distributed microservices architecture with:

Spring Cloud Config Server – centralized configuration (global + service-specific)

Eureka Discovery Server – service registration & discovery

Spring Cloud Gateway – single entry point for all clients

Independent domain services (Product, Order, Inventory)

Event-driven communication using Kafka

Dockerized services running in a shared network

Client
  |
  v
API Gateway
  |
  +--> Product Service
  |
  +--> Order Service ----> Kafka ----> Inventory Service
  |
Config Server <---- all services
  |
Eureka Server <---- all services

🧱 Microservices Overview

1️⃣ Config Server

Centralized configuration management

Loads configs from Git/local repo

Provides:

Global configuration

Service-specific configuration

Eliminates hardcoded configs inside services

Port: 8888

2️⃣ Discovery Server (Eureka)

Service registry for all microservices

Enables service-to-service communication using service names

No hardcoded host/port dependencies

Port: 8761

3️⃣ API Gateway

Single entry point for all client requests

Routes requests dynamically using Eureka

Enables:

Centralized routing

Future support for security, rate limiting, logging

Port: 8080

4️⃣ Product Service

Manages product catalog

CRUD operations on products

Registers with Eureka

Fetches configuration from Config Server

Port: 8080 (internal)

5️⃣ Order Service

Handles order creation

Communicates with:

Product Service (synchronous)

Inventory Service (asynchronous via Kafka)

Publishes OrderPlacedEvent to Kafka

Port: 8081 (internal)

6️⃣ Inventory Service

Manages product stock

Listens to Kafka events from Order Service

Updates inventory asynchronously

Ensures loose coupling with Order Service

Port: 8082 (internal)

🔄 Communication Patterns Used
✅ Synchronous Communication

REST APIs using Spring Web

Service name resolution via Eureka

Example:

Order Service → Product Service

✅ Asynchronous Communication (Event-Driven)

Kafka used as message broker

Order Service publishes events

Inventory Service consumes events

Why Kafka?

Loose coupling

Better scalability

Failure isolation

Real-world enterprise pattern

🐳 Docker & Containerization

All services are Dockerized using:

eclipse-temurin:17-jdk-alpine base image

Lightweight & production-ready JVM image

Docker Network

All containers run inside a custom bridge network:

shopverse-net


This allows containers to communicate using service names instead of IPs.

▶️ Application Startup Order

⚠️ Important – Services must be started in the correct order:

Config Server

Eureka Server

API Gateway

Kafka & Zookeeper

Product Service

Inventory Service

Order Service

🧪 Running Services Using Docker
Build Image
docker build -t shopverse/product-service:1.0 .

Run Container
docker run -d \
  --name product-service \
  --network shopverse-net \
  -p 8080:8080 \
  shopverse/product-service:1.0


Internal container ports can be the same because each container has its own network namespace.

⚙️ Global Configuration (Config Server)

A global configuration file is used for:

Logging levels

Common JPA properties

Eureka configuration

Shared timeout values

This avoids duplication and keeps configs clean and centralized.

📦 Technologies Used

Java 17

Spring Boot

Spring Cloud (Config, Eureka, Gateway)

Apache Kafka

Docker & Docker Desktop

Maven

H2 / PostgreSQL (as applicable)

🚀 What This Project Demonstrates

✔ Real-world microservices design
✔ Service discovery & centralized config
✔ API Gateway pattern
✔ Event-driven architecture
✔ Docker-based deployment
✔ Clean separation of concerns

🔮 Future Enhancements (Not Implemented Yet)

Circuit Breaker (Resilience4j)

Retry & Timeout mechanisms

Centralized security (JWT + OAuth2)

Distributed tracing (Zipkin)

Monitoring (Prometheus + Grafana)

These were intentionally left out to keep the learning focused and progressive.

🎯 Key Learnings

Difference between image vs container

Why ports conflict on host but not inside Docker network

Service name–based communication

Kafka-based async processing

Debugging container logs effectively

Production-grade backend thinking

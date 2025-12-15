🛒 ShopVerse – Microservices-Based E-Commerce Backend

A production-style microservices backend system built using Spring Boot, Spring Cloud, Kafka, and API Gateway, following industry best practices for configuration management, service discovery, and event-driven architecture.

🚀 Project Overview

ShopVerse is a scalable backend architecture designed to simulate a real-world e-commerce platform. The system is built using loosely coupled microservices, centralized configuration, service discovery, and asynchronous communication via Kafka.

This project demonstrates backend engineering depth, not just CRUD operations.

🧩 Architecture Components
Core Services
Service	Description
Config Server	Centralized configuration management using Git-backed configs
Discovery Server (Eureka)	Service registration and discovery
API Gateway	Single entry point for all client requests
Product Service	Manages product data and publishes product events
(Future-ready)	Order, Inventory, Notification services
🔧 Tech Stack

Java 17

Spring Boot 3.x

Spring Cloud 2023.x

Spring Cloud Config Server

Netflix Eureka

Spring Cloud Gateway

Apache Kafka

Spring Data JPA + Hibernate

H2 Database (File-based)

Docker-ready (optional)

📁 Repository Structure
shopverse/
├── config-server/
├── discovery-server/
├── api-gateway/
├── product-service/
├── config-repo/   # Git-backed configuration files
└── README.md
⚙️ Configuration Management

Centralized configuration stored in Git Config Repository

Services fetch configs using Spring Cloud Config

Environment-specific configs supported

Example config file:

product-service.yml

Key configurations managed globally:

Datasource

Kafka

Logging levels

Eureka client

Feature flags

🔍 Service Discovery

Eureka Server runs on port 8761

All services auto-register

API Gateway uses discovery-based routing

Eureka Dashboard:

http://localhost:8761
🌐 API Gateway

Central entry point for all APIs

Dynamic routing via service IDs

Load-balanced calls using Eureka

Example route:

http://localhost:8080/api/products/**
📦 Product Service
Features

Product CRUD APIs

JPA + Hibernate

File-based H2 DB persistence

Kafka producer for product events

Database

H2 Console enabled

Persistent file-based DB

http://localhost:8080/h2-console
📡 Kafka Integration

Kafka used for event-driven communication

Product Service publishes JSON events

Ready for consumer services (Order, Inventory, etc.)

▶️ How to Run the Project
Start Order

Config Server

Discovery Server

Kafka + Zookeeper

API Gateway

Product Service

🧪 Testing

REST APIs testable via Postman

H2 console available for DB inspection

Kafka topics observable via CLI or UI

📌 Key Learnings Demonstrated

Microservices communication

Centralized config with fail-fast

Service discovery

Event-driven architecture

Production-grade logging

Debugging real-world config issues

🛣️ Roadmap

Order Service

Inventory Service

Kafka Consumers

Circuit Breakers

Distributed Tracing

Security (OAuth2 / JWT)

Docker & Kubernetes

👨‍💻 Author

Built with dedication and persistence by Sushant Kharat.

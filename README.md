# Swiggy Microservices Backend

A production-style food delivery backend built using **Spring Boot Microservices Architecture**.

## Architecture

Services included:

- API Gateway
- Eureka Service Discovery
- Auth Service (JWT Authentication)
- User Service
- Restaurant Service
- Menu Service
- Order Service
- Payment Service (Razorpay Integration)
- Delivery Service
- Notification Service (Email + WhatsApp)

## Technologies

- Java
- Spring Boot
- Spring Cloud
- Kafka
- PostgreSQL
- Redis
- Docker
- Razorpay API
- Twilio API
- REST APIs

## Features

- Microservices architecture
- Event-driven communication using Kafka
- Service discovery with Eureka
- API Gateway routing
- JWT authentication
- Razorpay payment integration
- Email & WhatsApp notifications
- Order lifecycle management

## Services Diagram

User → API Gateway → Services  
Order Service → Kafka → Notification Service

## Author

Vishal Jadhav  
Java Backend Developer

## Architecture Overview

The system follows a **microservices architecture** with Spring Boot services communicating through REST APIs and Kafka event streaming.

Main components:
- API Gateway for routing
- Eureka for service discovery
- Kafka for asynchronous messaging
- Razorpay for payments
- Twilio for notifications

## System Architecture

```
                Client
                  |
              API Gateway
                  |
------------------------------------------------
|  Auth  |  User  |  Restaurant  |  Menu  |
|           Order Service        | Payment |
------------------------------------------------
                      |
                    Kafka
                      |
            Notification Service
               |             |
             Email        WhatsApp
```
    

# Quiz Microservices Platform

A distributed and scalable **Quiz Application** built using **Spring Boot** and **Spring Cloud** with **PostgreSQL** as the database. The platform is built with a microservices architecture, leveraging **Eureka** for service discovery, **Feign** for inter-service communication, and an **API Gateway** to route all incoming requests.

## 🚀 Features

- **Microservices Architecture**: Each service runs independently and can be scaled individually.
- **PostgreSQL Database**: Reliable data storage for all quiz questions and results.
- **Service Discovery with Eureka**: Dynamic registration and discovery of services in a distributed environment.
- **API Gateway**: Centralized routing of requests to various microservices.
- **Feign Client**: Simplified HTTP communication between microservices.

## 🛠️ Tech Stack

- **Java 17**: Latest version of Java for modern development.
- **Spring Boot 3**: Framework for building production-ready applications.
- **Spring Cloud**: Including **Eureka**, **Feign**, and **API Gateway**.
- **PostgreSQL**: Relational database for storing quiz-related data.
- **Docker** (optional): Containerization of microservices for easy deployment.

## 🌐 Microservices Overview

### 1. `service-registry` (Eureka Server)

Handles the registration and discovery of services in the system. Services communicate with each other dynamically without needing hard-coded URLs.

- **Eureka Server** for service registration and discovery.
  
### 2. `question-service`

Manages all quiz-related questions and answers. It exposes CRUD endpoints to create, update, retrieve, and delete quiz questions.

- **PostgreSQL** database for storing questions and answers.
  
### 3. `quiz-service`

Responsible for generating quizzes and evaluating answers. It consumes data from the `question-service` and calculates the results for the quizzes.

- **Feign Client** to call `question-service` for retrieving questions.

### 4. `api-gateway`

Acts as the entry point for all client requests. It routes the requests to the appropriate microservice (either `question-service` or `quiz-service`).

- **Spring Cloud Gateway** for routing requests.

## 🏁 Getting Started

### Prerequisites

Make sure you have the following installed on your machine:

- Java 17 or higher
- PostgreSQL (or use Docker to run PostgreSQL)
- Maven or Gradle (to build the project)


📊 Project Structure
quiz-microservices-platform/
│
├── api-gateway/          # API Gateway that routes requests
├── question-service/     # Service responsible for managing questions
├── quiz-service/         # Service responsible for quiz generation and evaluation
├── service-registry/     # Eureka Service Registry
└── README.md             # Project documentation

📅 Future Improvements
- Implement user authentication (JWT).
- Add unit and integration tests.
- Containerize using Docker and deploy to a cloud platform (e.g., AWS or Azure).
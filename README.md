# URL Shortener Backend Application

A production-ready URL Shortener built with **Spring Boot**, featuring **JWT authentication**, **role-based authorization**, **caching**, **pagination**, and **real performance metrics**.  
Includes a lightweight frontend built using **HTML, CSS, and JavaScript**, served directly from Spring Boot as a single deployable application.

---

## Features

- JWT-based authentication with USER / ADMIN roles
- Create, resolve, list, and delete short URLs
- Public short URL redirection using HTTP 302
- Soft delete implementation using active flags
- Pagination for scalable data retrieval
- Caching for read-heavy endpoints
- API documentation using Swagger/OpenAPI
- Application metrics using Spring Boot Actuator
- Simple frontend dashboard (HTML, CSS, JavaScript)
- Request logging and global exception handling

---

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- H2 / MySQL
- Spring Cache
- Spring Boot Actuator
- Swagger / OpenAPI
- HTML, CSS, JavaScript
- Maven, Git

---

## Architecture

- Layered architecture (Controller → Service → Repository)
- Stateless REST APIs using JWT authentication
- Frontend served from Spring Boot static resources

---

## API Endpoints

**Authentication**
- `POST /auth/login`

**URL Management**
- `POST /shorten` (ADMIN)
- `GET /shorten/{code}`
- `GET /shorten?page=&size=`
- `DELETE /shorten/{code}` (ADMIN)

**Redirect**
- `GET /r/{code}` → Redirects to original URL

---

## Performance Metrics

Measured using Spring Boot Actuator:

- Handled **1,000+ authenticated requests**
- **~3.8 ms average latency**
- **<80 ms worst-case latency**
- **~200+ requests/sec locally**
- Zero request failures during testing

Metrics endpoint:

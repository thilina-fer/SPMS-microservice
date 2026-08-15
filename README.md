# Smart Parking Management System (SPMS)

**Microservice-Based Application** — ITS 1018: Software Architectures & Design Patterns II, Final Examination Assignment
Graduate Diploma in Software Engineering — IJSE

A cloud-native, microservice-based platform for real-time parking space management, allowing drivers to search, reserve, and pay for parking while giving parking owners tools to manage their spaces dynamically.

---

## Architecture Overview

SPMS is built as **7 independent Spring Boot microservices**, coordinated through a service registry, centralized configuration, and a single-entry API Gateway.

```
                         ┌─────────────────┐
                         │  Eureka Server   │  (Service Registry - 8761)
                         └────────▲─────────┘
                                  │ registers
              ┌───────────────────┼───────────────────┐
              │                   │                   │
      ┌───────▼──────┐    ┌───────▼───────┐   ┌───────▼───────┐
      │ Config Server │    │  API Gateway   │   │  (all business │
      │    (8888)     │    │    (8080)      │   │   services)    │
      └───────────────┘    └───────▲────────┘   └────────────────┘
                                    │ routes
              ┌─────────────────────┼─────────────────────┬──────────────────┐
              │                     │                     │                  │
      ┌───────▼──────┐    ┌─────────▼────────┐   ┌────────▼────────┐ ┌───────▼───────┐
      │ User Service │    │ Vehicle Service   │   │ Parking Space   │ │ Payment       │
      │    (8081)    │    │     (8082)        │   │ Service (8083)  │ │ Service (8084)│
      └──────┬───────┘    └────────┬──────────┘   └────────┬────────┘ └───────┬───────┘
             │                     │                        │                  │
        ┌────▼────┐          ┌─────▼─────┐            ┌─────▼──────┐    ┌──────▼──────┐
        │ userdb  │          │ vehicledb │            │ parkingdb  │    │ paymentdb   │
        └─────────┘          └───────────┘            └────────────┘    └─────────────┘
              Neon PostgreSQL (cloud-hosted, database-per-service pattern)
```

**Design pattern:** Database-per-service — each business microservice owns an isolated PostgreSQL database on Neon, with no shared schema across services.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Core framework | Spring Boot 4.1.0 |
| Service discovery | Spring Cloud Netflix Eureka |
| Centralized config | Spring Cloud Config Server (native filesystem backend) |
| API Gateway | Spring Cloud Gateway (WebFlux) |
| Data access | Spring Data JPA + Hibernate |
| Database | Neon (serverless cloud PostgreSQL) — database-per-service |
| Security | Spring Security (BCrypt password hashing) |
| Build tool | Maven |
| Language | Java 17 |
| API testing | Postman |

---

## Modules & Ports

| Module | Port | Responsibility |
|---|---|---|
| `eureka-server` | 8761 | Service registry & discovery |
| `config-server` | 8888 | Centralized YAML configuration |
| `api-gateway` | 8080 | Single entry point, routes to all business services |
| `user-service` | 8081 | User/owner registration, authentication, profile |
| `vehicle-service` | 8082 | Vehicle CRUD, entry/exit simulation |
| `parking-space-service` | 8083 | Parking space management, reservation, availability |
| `payment-service` | 8084 | Mock payment processing, digital receipts |

---

## API Endpoints

All endpoints are accessed through the **API Gateway** at `http://localhost:8080`.

### User Service — `/api/v1/users`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/users/register` | Register a new user (DRIVER / OWNER / ADMIN) |
| POST | `/api/v1/users/login` | Authenticate a user |
| GET | `/api/v1/users/{id}` | Get user by ID |
| PUT | `/api/v1/users/{id}` | Update user profile |

### Vehicle Service — `/api/v1/vehicles`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/vehicles` | Register a vehicle, linked to a user |
| GET | `/api/v1/vehicles/{id}` | Get vehicle by ID |
| PUT | `/api/v1/vehicles/{id}` | Update vehicle details |
| GET | `/api/v1/vehicles/user/{userId}` | Get all vehicles for a user |
| POST | `/api/v1/vehicles/{id}/entry` | Simulate vehicle entry (records `entryTime`) |
| POST | `/api/v1/vehicles/{id}/exit` | Simulate vehicle exit (records `exitTime`) |

### Parking Space Service — `/api/v1/parking`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/parking` | Create a parking space (owner) |
| GET | `/api/v1/parking?city=&zone=&available=true` | Search/filter available spaces |
| POST | `/api/v1/parking/{id}/reserve` | Reserve a space (prevents double-booking) |
| POST | `/api/v1/parking/{id}/release` | Release a reserved/occupied space |
| PUT | `/api/v1/parking/{id}/status` | Manually update status (simulated IoT) |

### Payment Service — `/api/v1/payments`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/payments/charge` | Process a mock payment (Luhn-validated card) |
| GET | `/api/v1/payments/{id}/receipt` | Retrieve a digital receipt |
| GET | `/api/v1/payments/user/{userId}` | Get all transactions for a user |

---

## Database

All persistent data is hosted on **Neon** (serverless cloud PostgreSQL) under a single project, with one isolated database per business service:

| Service | Database |
|---|---|
| user-service | `userdb` |
| vehicle-service | `vehicledb` |
| parking-space-service | `parkingdb` |
| payment-service | `paymentdb` |

Schema is auto-managed via Hibernate (`spring.jpa.hibernate.ddl-auto: update`) — no manual migration scripts required.

---

## Running the Project

Services must be started **in this order** so that dependent services can discover each other via Eureka:

1. `eureka-server` (port 8761)
2. `config-server` (port 8888)
3. `api-gateway` (port 8080)
4. `user-service`, `vehicle-service`, `parking-space-service`, `payment-service` (any order, ports 8081–8084)

```bash
# from each module's directory
./mvnw spring-boot:run
```

Once all services are up, confirm registration at the Eureka dashboard:
```
http://localhost:8761
```

---

## Testing

All endpoints were tested end-to-end using **Postman**, including:
- Success and validation-error paths for every endpoint (duplicate email, invalid login, not-found lookups, invalid card numbers)
- **Double-booking prevention** verified on the Parking Space Service (`reserve` → `reserve again` correctly rejected until `release`)
- A full **cross-service integration flow**: register user → register vehicle → create & reserve parking space → charge payment → retrieve receipt → vehicle entry/exit → release space — confirming all four business services interoperate correctly through the API Gateway

---

## Author

**Thilina Dilshan Fernando**
Software Engineering Undergraduate — Institute of Software Engineering (IJSE)
GitHub: [github.com/thilina-fer](https://github.com/thilina-fer)

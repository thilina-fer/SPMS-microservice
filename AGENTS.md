# AGENTS.md — Smart Parking Management System (SPMS)

## Project Context

Multi-module Spring Boot microservices project for **ITS 1018 - Software Architectures & Design Patterns II** final exam. Backend-only (no UI). All endpoints tested via Postman. This project is developed incrementally, service by service, using this file as the persistent context/instruction set for the coding agent.

**Root folder:** `smart-parking-management-system/`
**Status:** All 6 modules already created manually by the developer (`eureka-server`, `config-server`, `api-gateway`, `user-service`, `vehicle-service`, `parking-space-service`, `payment-service`). DO NOT recreate any module or its `pom.xml`. The agent only writes code inside already-existing module folders, one module at a time, strictly following the phase order below. Do not touch a module until its phase is explicitly started by the developer.

---

## Non-Negotiable Conventions

1. **Config format: YAML only.** Every module uses `application.yml` (or `bootstrap.yml` where relevant for config-server clients). Never generate `.properties` files.
2. **Group ID:** `lk.ijse.spms` for every module.
3. **Package structure per service** (standard layered architecture):
   ```
   lk.ijse.spms.<service_name>
   ├── controller
   ├── service
   │   └── impl
   ├── repository
   ├── entity
   ├── dto
   ├── exception
   └── config
   ```
4. **Response format:** every controller returns a consistent wrapper DTO, e.g.:
   ```java
   record ResponseDTO(int code, String message, Object data) {}
   ```
5. **Exception handling:** every service has a `@RestControllerAdvice` global exception handler. No raw stack traces returned to client.
6. **Lombok** used for entities/DTOs (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`).
7. **Database:** H2 in-memory database for all services (separate DB name per service — database-per-service pattern, e.g. `jdbc:h2:mem:userdb`, `jdbc:h2:mem:vehicledb`). Use `spring.jpa.hibernate.ddl-auto: update` during dev. Enable H2 console per service (`spring.h2.console.enabled: true`, path `/h2-console`) for quick data inspection during development.
8. **Every business service must:**
   - Register with Eureka (`eureka.client.service-url.defaultZone`)
   - Pull shared config from Config Server where applicable
   - Be routable through API Gateway
9. **After each service is completed**, the agent must generate/update a Postman collection section (or note endpoints clearly) so they can be added to `postman_collection.json` at the end.
10. **No service should be built until the developer confirms the previous phase's endpoints work in Postman.** Do not jump ahead to the next phase automatically.

---

## Port Allocation Plan

| Service | Port |
|---|---|
| eureka-server | 8761 |
| config-server | 8888 |
| api-gateway | 8080 |
| user-service | 8081 |
| vehicle-service | 8082 |
| parking-space-service | 8083 |
| payment-service | 8084 |

---

## Development Phases

Work through phases **strictly in order**. Each phase = one module. Do not start a phase until told to.

### Phase 0 — Eureka Server (already done, reference only)
- Port `8761`, standalone registry, no changes needed unless debugging.

### Phase 1 — Config Server
**Goal:** Centralized YAML config for all services.
- Dependency already chosen: `spring-cloud-config-server`
- Enable with `@EnableConfigServer`
- `application.yml`:
  - `server.port: 8888`
  - `spring.cloud.config.server.native.search-locations` pointing to a local `config-repo/` folder inside this module (use **native filesystem backend**, not a remote git repo, to keep things simple for exam scope)
- Create one YAML file per service inside `config-repo/` (e.g. `user-service.yml`, `vehicle-service.yml`) holding shared props (datasource placeholders, eureka url, logging level).
- Register config-server itself with Eureka too.

### Phase 2 — API Gateway
**Goal:** Single entry point routing to all business services by service ID via Eureka.
- Port `8080`
- `spring.cloud.gateway.discovery.locator.enabled: true` OR explicit route definitions per service (prefer **explicit routes** for clarity in an exam context):
  ```yaml
  spring:
    cloud:
      gateway:
        routes:
          - id: user-service
            uri: lb://USER-SERVICE
            predicates:
              - Path=/api/users/**
          - id: vehicle-service
            uri: lb://VEHICLE-SERVICE
            predicates:
              - Path=/api/vehicles/**
          - id: parking-service
            uri: lb://PARKING-SPACE-SERVICE
            predicates:
              - Path=/api/parking/**
          - id: payment-service
            uri: lb://PAYMENT-SERVICE
            predicates:
              - Path=/api/payments/**
  ```
- Register with Eureka.

### Phase 3 — User Service
**Goal:** User/owner registration, auth, profile, booking history.
- Port `8081`
- Entities: `User` (id, name, email, password, role: DRIVER/OWNER/ADMIN, createdAt)
- Endpoints:
  - `POST /api/users/register`
  - `POST /api/users/login`
  - `GET /api/users/{id}`
  - `PUT /api/users/{id}`
  - `GET /api/users/{id}/history` (stub/in-memory ok initially — can integrate later with bookings from parking-space-service via Feign/RestTemplate)
- Password hashing with BCrypt at minimum (JWT auth optional/bonus — confirm with developer before implementing full JWT).

### Phase 4 — Vehicle Service
**Goal:** Vehicle CRUD + link to user + entry/exit simulation.
- Port `8082`
- Entity: `Vehicle` (id, plateNumber, type, userId, status: IN/OUT)
- Endpoints:
  - `POST /api/vehicles`
  - `GET /api/vehicles/{id}`
  - `PUT /api/vehicles/{id}`
  - `GET /api/vehicles/user/{userId}`
  - `POST /api/vehicles/{id}/entry`
  - `POST /api/vehicles/{id}/exit`

### Phase 5 — Parking Space Service (core business logic)
**Goal:** Manage spaces, reservation, availability.
- Port `8083`
- Entity: `ParkingSpace` (id, location/zone, city, status: AVAILABLE/OCCUPIED/RESERVED, ownerId, pricePerHour)
- Endpoints:
  - `POST /api/parking` (create space — owner)
  - `GET /api/parking?city=&zone=&available=true` (filter/search)
  - `POST /api/parking/{id}/reserve`
  - `POST /api/parking/{id}/release`
  - `PUT /api/parking/{id}/status` (manual/simulated IoT status update)
- This is the most logic-heavy service — reservation must prevent double-booking (check current status before allowing reserve).

### Phase 6 — Payment Service
**Goal:** Mock payment + digital receipt.
- Port `8084`
- Entity: `Transaction` (id, userId, vehicleId, parkingSpaceId, amount, status: SUCCESS/FAILED, receiptCode, timestamp)
- Endpoints:
  - `POST /api/payments/charge` (mock card validation — simple Luhn check or hardcoded mock validation is enough)
  - `GET /api/payments/{id}/receipt`
  - `GET /api/payments/user/{userId}`

### Phase 7 — Integration Pass
- Verify all services reachable through API Gateway.
- Verify Eureka dashboard shows all 6 registered services (eureka-server, config-server, api-gateway, user-service, vehicle-service, parking-space-service, payment-service).
- Cross-service calls (e.g. payment-service confirming vehicle/space exists) via `RestTemplate`/`OpenFeign` — only if time permits, not mandatory per spec.

### Phase 8 — Postman & Submission
- Consolidate all endpoints into one Postman collection, export as `postman_collection.json`, place in project root.
- Screenshot Eureka dashboard → `docs/screenshots/eureka_dashboard.png`.
- Write root `README.md` per the exam's required format (links to Postman collection + Eureka screenshot).

---

## Agent Working Rules

- Before writing code for a phase, confirm the module folder + chosen dependencies already exist (developer creates modules manually).
- Generate code **only for the current phase's service** unless explicitly told to work across services.
- Always output complete files (entity, repo, service, serviceImpl, controller, exception handler, `application.yml`) for the phase — not fragments.
- Flag any assumption made (e.g. DB name, default port conflicts) clearly instead of silently guessing.
- Do not implement JWT/security beyond basic password hashing unless explicitly requested.
- Keep each service self-contained — no shared JAR/common module across services (exam scope prefers simplicity over DRY here).

# Task Management App

Decision:
Did not create UI for register a user this can be done by posting to the endpoint
http://localhost:8080/api/auth/register

body:
{
  "username": "alice",
  "password": "password123"
}

There is no strict validation user creation

then user can login after successfully registering the user first:

endpoint
http://localhost:8080/api/auth/login 

body:
{
  "username": "alice",
  "password": "password123"
}

When the user logins in after registering JWT token is issued that lasts 1 hour, should be 5 minutes or so then refresh, does not have refresh token.
Token is validated then the subject is used to read the userId then added into the context which is used to read the task,

there is no logout functionalities, not enough time

There is database table for app_user, task
Task are linked to ManyToOne user 

Didn't have time to create most of UI, just enough to login and Dispaly the tasks
I put that there just for testing purposes nothing more.


Error handling, It does uses the correct error code but the error handling need to be improved need to consistent across.


This is a **full-stack monorepo** for a Task Management application. It includes a **Spring Boot 3.2.1** backend and an **Angular 13.3** frontend. The app uses **JWT-based authentication** and serves both the REST API and the Angular SPA as a single deployable artifact.

---

## Features

- **Backend**: Spring Boot REST API with H2 in-memory database
- **Frontend**: Angular SPA for task management
- **Authentication**: JWT-based authentication with a custom filter chain
- **Database**: H2 in-memory database (resets on restart)
- **Single Deployable Artifact**: Backend serves both API and frontend static assets
- **Development Proxy**: Angular dev server proxies API requests to the backend

---

## Architecture Overview

### Backend
- **Framework**: Spring Boot 3.2.1
- **Port**: 8080
- **Authentication**: JWT-based (custom filter, no Spring Security authentication manager)
- **Database**: H2 in-memory database
- **Static Asset Serving**: Serves Angular app from `src/main/resources/static/`

### Frontend
- **Framework**: Angular 13.3
- **Port**: 4200 (in development)
- **Routing**: Client-side routing with fallback to `index.html`
- **Proxy**: Development proxy forwards `/api` requests to the backend

---

## Getting Started

### Prerequisites
- **Java 21**
- **Node.js 16+**
- **Maven 3.8+**

---

### Running the Application

#### Backend Only
1. Navigate to the project root.
2. Run the backend:
   ```bash
   ./mvnw spring-boot:run
   ```

#### Frontend Only
1. Navigate to the `frontend` directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the frontend:
   ```bash
   ng serve
   ```

#### Full Application (Backend + Frontend)
1. Open two terminal windows.
2. In the first window, navigate to the project root and run the backend:
   ```bash
   ./mvnw spring-boot:run
   ```
3. In the second window, navigate to the `frontend` directory, install dependencies, and run the frontend:
   ```bash
   npm install
   ng serve
   ```

---

## Architectural and Design Decisions

This document outlines the key architectural and design decisions made during the development of the Task Management App.

---

## 1. Full-Stack Monorepo
### Decision:
The application is structured as a **full-stack monorepo**, combining the backend (Spring Boot) and frontend (Angular) into a single repository.

### Rationale:
- Simplifies development and deployment workflows.
- Ensures tight integration between the backend and frontend.
- Allows for a single deployable artifact in production.

---

## 2. JWT-Based Authentication
### Decision:
The application uses **JWT-based authentication** with a custom `JwtAuthFilter` to validate tokens and inject the authenticated user into the request.

### Rationale:
- Stateless authentication is ideal for modern SPAs.
- JWTs eliminate the need for server-side session storage.
- Custom filter allows flexibility without relying on Spring Security's `AuthenticationManager`.

### Implementation Details:
- Tokens are validated in `JwtAuthFilter`.
- Authenticated `User` is injected into the request as an attribute (`@RequestAttribute("user")`).
- No `SecurityContext` or `UserDetailsService` is used.

---

## 3. Angular SPA Integration
### Decision:
The Angular app is served as static assets by the Spring Boot backend in production.

### Rationale:
- Simplifies deployment by bundling the frontend and backend into a single artifact.
- Reduces the need for separate hosting for the frontend.

### Implementation Details:
- Static assets are placed in `src/main/resources/static/`.
- `ForwardController` forwards non-file paths to `index.html` for client-side routing.
- Static resources (e.g., `.js`, `.css`) are explicitly whitelisted in `SecurityConfig`.

---

## 4. H2 In-Memory Database
### Decision:
The application uses an **H2 in-memory database** for development and testing.

### Rationale:
- Simplifies setup for development and testing.
- No external database dependency is required.
- Data resets on every restart, ensuring a clean slate for testing.

### Implementation Details:
- H2 console is enabled at `/h2-console`.
- JDBC URL: `jdbc:h2:mem:testdb`, username: `sa`, password: (empty).
- `data.sql` can be used to seed initial data.

---

## 5. Development Proxy
### Decision:
The Angular dev server proxies API requests to the Spring Boot backend during development.

### Rationale:
- Allows the frontend and backend to run on separate ports during development.
- Avoids CORS issues by forwarding `/api` requests to the backend.

### Implementation Details:
- Proxy configuration is defined in `frontend/proxy.conf.json`.
- Proxy forwards `/api` requests to `http://localhost:8080`.

---

## 6. Custom Security Configuration
### Decision:
The `SecurityConfig` is customized to:
- Disable CSRF (common for JWT-based APIs).
- Disable frame options for the H2 console.
- Whitelist static resources and public endpoints.

### Rationale:
- JWT-based APIs do not require CSRF protection.
- H2 console requires frame options to be disabled.
- Static resources and public endpoints must be accessible without authentication.

### Implementation Details:
- `requestMatchers()` is used to define public and protected routes.
- `JwtAuthFilter` is registered with `addFilterBefore()`.

---

## 7. Controller Design
### Decision:
Controllers use `@RequestAttribute("user")` to access the authenticated user.

### Rationale:
- Simplifies controller logic by directly injecting the `User` object.
- Avoids reliance on `SecurityContextHolder`.

### Implementation Details:
- `JwtAuthFilter` sets the `User` object as a request attribute.
- Controllers retrieve the user with `@RequestAttribute("user")`.

---

## 8. Manual Build Integration
### Decision:
Frontend and backend builds are manually integrated for production.

### Rationale:
- Keeps the build process simple and decoupled.
- Allows independent development of the frontend and backend.

### Implementation Details:
- Frontend build output (`dist/frontend/`) is manually copied to `src/main/resources/static/`.
- Maven builds the backend and packages the frontend assets into the JAR.

---

## 9. Angular Guards
### Decision:
Angular guards (`AuthGuard` and `LoginGuard`) are used to protect routes.

### Rationale:
- Ensures only authenticated users can access protected routes.
- Redirects authenticated users away from the login page.

### Implementation Details:
- `AuthGuard` checks for a valid JWT token in `localStorage`.
- `LoginGuard` redirects authenticated users to `/tasks`.

---

## 10. Minimal Test Coverage
### Decision:
The application includes minimal test coverage for both the backend and frontend.

### Rationale:
- Focused on delivering a functional prototype.
- Tests can be added in future iterations.

### Implementation Details:
- Backend tests use JUnit and Mockito.
- Frontend tests use Jasmine and Karma.

---

## 11. Static Resource Whitelisting
### Decision:
Static resources (e.g., `.js`, `.css`, `/assets/**`) are explicitly whitelisted in `SecurityConfig`.

### Rationale:
- Ensures the Angular app can load all required assets in production.
- Prevents accidental blocking of static resources.

---

## 12. No Bidirectional Relationships in Entities
### Decision:
The `User` entity does not have a bidirectional relationship with `Task`.

### Rationale:
- Simplifies the data model and avoids potential circular references.
- Reduces the risk of performance issues with lazy loading.

### Implementation Details:
- `Task` has a `@ManyToOne` relationship with `User`.
- `User` does not have a `@OneToMany` relationship with `Task`.

---

## 13. Error Handling
### Decision:
Error responses use simple status codes and string messages.

### Rationale:
- Keeps the API lightweight and easy to debug.
- Avoisd the complexity of wrapping errors in custom response objects.

### Implementation Details:
- Example: `return ResponseEntity.badRequest().body("Username already exists")`.

---

## 14. No Integration Tests
### Decision:
The application does not include integration tests.

### Rationale:
- Focused on delivering a functional prototype.
- Integration tests can be added in future iterations.

This document will be updated as new decisions are made during the development process.

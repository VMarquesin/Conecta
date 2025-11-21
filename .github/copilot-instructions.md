# Copilot Instructions for Conecta

## Project Overview

Conecta is a local services marketplace built with Angular 17+ (frontend) and Spring Boot 3 (backend). It connects clients with service providers, featuring authentication, role-based access, and dynamic UI based on user type.

## Architecture & Key Components

-  **Frontend**: Angular Standalone Components, CSS Grid/Flexbox, Reactive Forms. Key directories:
   -  `frontend/src/app/pages/` — Main pages (home, cadastro-cliente, cadastro-prestador, login, meu-perfil)
   -  `frontend/src/app/components/` — Reusable UI components
   -  `frontend/src/app/services/` — API communication, business logic
   -  `frontend/src/app/guards/` — Route protection
   -  `frontend/src/app/interceptors/` — HTTP interceptors (e.g., JWT)
-  **Backend**: Spring Boot 3, Spring Security 6 (JWT), Spring Data JPA, H2 (dev DB). Main entry: `backend/src/main/java/br/com/conecta/DemoApplication.java`

## Developer Workflows

-  **Frontend**:
   -  Install dependencies: `npm install` (in `frontend/`)
   -  Start dev server: `npx ng serve` (http://localhost:4200)
   -  Build: `ng build`
   -  Unit tests: `ng test`
   -  E2E tests: `ng e2e` (if configured)
-  **Backend**:
   -  Build/run: Use VS Code Java extension or `./mvnw spring-boot:run` in `backend/`
   -  Main class: `DemoApplication.java`
   -  API runs at http://localhost:8080

## Conventions & Patterns

-  **Angular**:
   -  Use Standalone Components (no NgModules)
   -  CSS variables for colors: `--cor-amarela-principal`, `--cor-texto-principal`, `--cor-branca`, `--cor-borda`
   -  Use Angular Reactive Forms for all user input
   -  Route guards and interceptors for auth
   -  UI adapts to user role (Cliente/Prestador)
   -  Do not alter TypeScript logic or Angular directives when editing HTML/CSS
-  **Spring Boot**:
   -  Role-based endpoints: Only Prestadores can create publications, only Clientes can create reviews
   -  JWT authentication for all API calls
   -  Data validation on both frontend and backend

## Integration Points

-  **API**: Frontend communicates with backend via REST endpoints (see `services/*.ts`)
-  **Authentication**: JWT tokens managed by `auth.service.ts` and `auth-interceptor.ts`
-  **Categories**: Initial categories seeded via Postman or API calls

## Examples

-  To add a new service category, update backend and frontend (see `flashPost/flashpost-collection-categorias.json` and category grid in `home.html`)
-  For new UI components, use Angular CLI: `ng generate component <name>`

## References

-  See `README.md` in root and `frontend/` for setup and workflow details
-  Key files: `frontend/src/app/pages/home/home.html`, `frontend/src/app/services/auth.ts`, `backend/pom.xml`, `backend/src/main/java/br/com/conecta/DemoApplication.java`

---

**Feedback:** If any section is unclear or missing, please specify what needs improvement or additional detail.

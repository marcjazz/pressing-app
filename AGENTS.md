# AGENTS.md

This file provides guidance to agents when working with code in this repository.

## Project Setup

*   **Database:** A MySQL database named `pressing` must be created manually. The application will not create it automatically. Default credentials are `root`/`mysql`.
*   **Database Migrations:** The Flyway migration scripts in [`src/main/resources/db/migration`](src/main/resources/db/migration) are not executed automatically. They must be run manually to initialize the database schema.

## Build & Run

*   **Java Version:** This project is built with Java 8 and Spring Boot 1.4.3. The Gradle wrapper may fail with newer Java versions.

## Architecture

*   **Stateless Sessions:** The application is configured for stateless session management. Do not rely on session state.
*   **CORS:** All API endpoints are open to cross-origin requests via a global `@CrossOrigin` annotation on each controller.

## Code Style & Patterns

*   **Indentation:** The codebase uses tabs for indentation.
*   **Error Handling:** Methods commonly return `null` instead of throwing exceptions when an object is not found.
*   **Service Naming:** Service implementations are named with an `Impl` suffix (e.g., `CustomerServiceImpl`).

## Testing

*   **No Tests:** The project is set up for JUnit testing, but no unit tests are implemented. The [`README.md`](README.md) explicitly states that this is a missing feature.
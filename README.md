# Pressing Management
Generic Pressing Management system for small and medium size businesses

## Overview
This project is a multi-module Spring Boot application secured as an OAuth2 Resource Server (JWT). It exposes REST APIs for managing merchants, agencies, categories, customers, items, etc. The application is intended to run against PostgreSQL and validate JWTs issued by Keycloak.

Security and route protection are enforced in [pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java](pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java) via [java.SecurityFilterChain filterChain()](pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java:17).

- Versioned routes protection examples defined in [java.SecurityFilterChain filterChain()](pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java:22):
  - `/api/v1/plans/**` and `/api/v1/merchants/**` require `SCOPE_ADMIN`
  - `/api/v1/agencies/**` and `/api/v1/counters/**` require `SCOPE_ADMIN` or `SCOPE_MERCHANT`
  - `.anyRequest().authenticated()` applies to the rest

Non-versioned routes such as [java.MerchantController.findAll()](pressing-web/src/main/java/com/pressing/controller/MerchantController.java:33) and [java.AgencyController.findAll()](pressing-web/src/main/java/com/pressing/controller/AgencyController.java:51) require an authenticated JWT but no specific scope.

## Prerequisites
- Docker and Docker Compose
- Java 21 (or compatible JDK for Spring Boot 3.1.x)
- Internet access to pull Docker images and Gradle dependencies

## Modules
- [pressing-data](pressing-data): JPA entities and repositories
- [pressing-service](pressing-service): Service contracts
- [pressing-service-impl](pressing-service-impl): Service implementations
- [pressing-dto](pressing-dto): DTOs
- [pressing-web](pressing-web): Spring Boot web app (main module)

## Quick Start (Local, with Docker Postgres + Keycloak)

1) Start infrastructure services (Postgres, Keycloak)

From the repository root, run:

```bash
docker-compose up -d postgres keycloak-db keycloak
```

This launches:
- Postgres at `localhost:5432` with DB `pressing` (user/pass: `postgres`/`postgres`)
- Keycloak at `http://localhost:8180` (admin/admin)

2) Application configuration (already in repo)

- Active profile is set in [pressing-web/src/main/resources/application.yml](pressing-web/src/main/resources/application.yml) to `postgres`.
- Postgres + JWT issuer configured in [pressing-web/src/main/resources/application-postgres.yml](pressing-web/src/main/resources/application-postgres.yml):
  - `spring.datasource.*` targeting `localhost:5432/pressing`
  - `spring.security.oauth2.resourceserver.jwt.issuer-uri: http://localhost:8180/realms/master`
  - `spring.jpa.hibernate.ddl-auto: update` (Hibernate will create/update schema automatically)

3) Start the Spring Boot app locally

Use a port not already in use:

```bash
./gradlew :pressing-web:bootRun --args="--server.port=8082" -x test
```

The app starts on `http://localhost:8082` and enforces JWT authentication per [java.SecurityFilterChain filterChain()](pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java:17).

4) Seed Keycloak (client + test user)

Use Keycloak admin CLI inside the container to create a public client and a test user:

```bash
KC=pressing-app-keycloak-1
# Login to Keycloak admin
docker exec $KC /opt/keycloak/bin/kcadm.sh \
  config credentials --server http://localhost:8180 \
  --realm master --user admin --password admin

# Create public client (if missing)
if ! docker exec $KC /opt/keycloak/bin/kcadm.sh get clients -r master -q clientId=pressing-api | grep -q '"clientId" : "pressing-api"'; then
  docker exec $KC /opt/keycloak/bin/kcadm.sh create clients -r master \
    -s clientId=pressing-api -s protocol=openid-connect \
    -s publicClient=true -s directAccessGrantsEnabled=true -s standardFlowEnabled=true
fi

# Create test user (if missing)
if ! docker exec $KC /opt/keycloak/bin/kcadm.sh get users -r master -q username=testuser | grep -q '"username" : "testuser"'; then
  docker exec $KC /opt/keycloak/bin/kcadm.sh create users -r master -s username=testuser -s enabled=true
fi

# Set password for the user
docker exec $KC /opt/keycloak/bin/kcadm.sh set-password -r master --username testuser --new-password password
```

5) Obtain a JWT access token

```bash
curl -s -X POST \
  'http://localhost:8180/realms/master/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=password&client_id=pressing-api&username=testuser&password=password'
```

From the JSON response, copy the `access_token` value.

6) Call APIs with the Bearer token

Example calls that require only authentication (no special scopes):

```bash
# Replace $TOKEN with your access_token value
curl -i -H "Authorization: Bearer $TOKEN" -H "Accept: application/json" \
  http://localhost:8082/api/merchants

curl -i -H "Authorization: Bearer $TOKEN" -H "Accept: application/json" \
  http://localhost:8082/api/agencies
```

Expected response on a fresh DB is HTTP 200 with `[]`.

## Testing Protected v1 Routes (Scopes)
Versioned routes in [java.SecurityFilterChain filterChain()](pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java:22) require authorities like `SCOPE_ADMIN` or `SCOPE_MERCHANT`. By default, Keycloak-issued tokens include OIDC scopes (e.g., `profile`, `email`). To access `/api/v1/...` routes you have two options:

Option A: Configure Keycloak to emit scope claim values matching required authorities
- Create client scopes (e.g., `ADMIN`, `MERCHANT`).
- Assign these client scopes to the `pressing-api` client.
- Ensure the `scope` (or `scp`) claim in the token contains `ADMIN` and/or `MERCHANT`.
- Then Spring Security will map them to authorities `SCOPE_ADMIN` / `SCOPE_MERCHANT` automatically.

Option B: Customize authority mapping in Spring (advanced)
- Add a custom `JwtAuthenticationConverter` bean to map Keycloak roles (e.g., realm or resource roles) into `SCOPE_...` authorities expected by the matchers.
- This approach requires code changes and is not included by default.

## Notable Files
- Security: [pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java](pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java) — see [java.SecurityFilterChain filterChain()](pressing-web/src/main/java/com/pressing/auth/SecurityConfig.java:17)
- App entrypoint: [pressing-web/src/main/java/com/pressing/Application.java](pressing-web/src/main/java/com/pressing/Application.java)
- Gradle web module dependencies: [pressing-web/build.gradle](pressing-web/build.gradle)
- Spring profiles and config: [pressing-web/src/main/resources/application.yml](pressing-web/src/main/resources/application.yml), [pressing-web/src/main/resources/application-postgres.yml](pressing-web/src/main/resources/application-postgres.yml)

## Development Tips
- Ports in use:
  - App: default 8080; use `--server.port=8081/8082` if needed
  - Keycloak: 8180
  - Postgres: 5432
- If you hit `Port in use` errors, either stop the conflicting process or change `--server.port`.
- Hibernate schema generation is enabled for convenience (`ddl-auto=update`). For production, prefer Flyway migrations and disable automatic schema updates.

## Stopping Services
- Stop the Spring app with Ctrl+C in the Gradle run terminal.
- Stop Docker services:

```bash
docker-compose down
```

## Prior Notes
- This repository previously referenced MySQL and manual schema creation. The current quick start uses Postgres with schema auto-creation for development.
- Flyway is configured in [pressing-web/build.gradle](pressing-web/build.gradle) but no SQL migration scripts are provided. If you add migrations under `classpath:db/migration`, you can integrate Flyway into your workflow and switch `ddl-auto` to `none`.

# Project Architecture Rules (Non-Obvious Only)

*   **Stateless Sessions:** The application is configured for stateless session management. Do not rely on session state.
*   **CORS:** All API endpoints are open to cross-origin requests via a global `@CrossOrigin` annotation on each controller.
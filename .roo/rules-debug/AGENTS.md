# Project Debug Rules (Non-Obvious Only)

*   **Database:** A MySQL database named `pressing` must be created manually. The application will not create it automatically. Default credentials are `root`/`mysql`.
*   **Database Migrations:** The Flyway migration scripts in [`src/main/resources/db/migration`](src/main/resources/db/migration) are not executed automatically. They must be run manually to initialize the database schema.
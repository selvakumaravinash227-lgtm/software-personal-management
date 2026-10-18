# SPMS architecture

```text
React + Vite → HTTP bearer session → Spring Boot REST controllers → application services → Spring Data JPA repositories → MySQL SPMS_DB
```

| Layer | Location | Responsibility |
|---|---|---|
| Web frontend | `frontend/web/src/spms` | React login, responsive app shell, dashboard, module pages, forms, search, and table actions. |
| Active REST API | `spring-backend/src/main/java/edu/spms/web` | Spring Boot REST routes, Spring Security bearer sessions, services, JPA entities, and repositories for the React client. |
| Preserved JDBC backend | `backend/src/main/java/spms` | Existing JDBC/DAO services and OOAD classes retained for the Swing/OOAD deliverable. |
| Business services | `spring-backend/src/main/java/edu/spms/web/service` | Validation, role checks, employee scoping, leave/payroll/performance workflows, password hashing, and report calculations. |
| JPA data access | `spring-backend/src/main/java/edu/spms/web/repository` | Database operations using Spring Data JPA. The legacy backend's JDBC DAOs remain separate. |
| Models | `backend/src/main/java/spms/model` | User hierarchy and workforce domain entities. |
| DB configuration | `backend/src/main/java/spms/util/DatabaseConnection.java` | JDBC configuration from environment variables or root `.env`. |
| MySQL | `database/*.sql` | Schema, fictional seed data, and indexes. |
| Stitch reference | `stitch-reference/` | Original HTML, screenshot, and design document, preserved unchanged. |
| Legacy Swing app | `frontend/src/main/java/spms` | Prior Java Swing implementation retained as recoverable source. |

Sign-in verifies a PBKDF2-HMAC-SHA256 password hash in MySQL and creates a random opaque bearer session with a 12-hour expiry. The server stores the associated authenticated user; services check role and apply employee record scoping. Passwords are not stored in browser storage. The browser keeps the opaque session token in `sessionStorage` and sends it as a bearer token.

MySQL is the source for dashboard aggregates, module lists, and records. The frontend has no seeded operational mock data. API errors return readable messages; database exceptions are logged server-side. Database schema extensions are additive SQL migrations because `ddl-auto=none` is enabled. Employee passwords use BCrypt after migration; newly provisioned employee accounts are forced to change their temporary password before normal API access. Payroll payment records are internal audit records; no bank transfer provider is configured.

## OOAD

`User` is abstract with `Admin`, `HRManager`, and `Employee` subclasses; overridden access checks provide polymorphism. Services and DAOs separate business rules from persistence. The SPMS use-case and sequence mapping is documented in [uml-mapping.md](uml-mapping.md). The original OOAD presentation was not among the inspected files.

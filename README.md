# Software Personnel Management System (SPMS)

SPMS is a workforce operations application based on the supplied Google Stitch designs. The primary application is a React + Vite web frontend, a Java Spring Boot REST API, and MySQL persistence. The former Swing implementation and the untouched Stitch ZIP/design exports are preserved alongside the new application.

## Workspace layout

```text
SPMS/
├── frontend/
│   ├── web/                         React + Vite application
│   └── src/main/java/               Preserved Swing source
├── backend/                         Preserved JDBC backend and earlier API adapter
├── spring-backend/                  Standalone Spring Boot + Spring Security + JPA API
├── database/                        MySQL schema, repeatable sample + history data, indexes
├── docs/                            Setup, API, architecture, OOAD and test notes
├── preservation/                    Baseline source/config/docs/SQL/tests/ZIP backup
├── stitch-reference/                Extracted original HTML, screenshot and DESIGN.md
└── stitch_software_personnel_management_system.zip
```

The standalone web API and detailed Windows setup are documented in [docs/web-application-setup.md](docs/web-application-setup.md). For deploying the frontend to Vercel with the Spring API and MySQL hosted separately, see [docs/vercel-deployment.md](docs/vercel-deployment.md). The original Swing and JDBC files remain at their existing paths; they were not moved. The Stitch archive is static design material, not a React project.

## Modules

The app contains the six requested modules: User Authentication, Employee Management, Attendance & Leave Management, Payroll Management, Performance & Reports, and Administration. Access is derived from the signed-in account and checked on the server; the browser does not allow users to choose a role. Forms and table actions call the API. MySQL is the source of operational records; dashboard values and lists are loaded from it.

## Windows setup

Install JDK 17+, Maven 3.9+, Node.js 20+, and MySQL 8+. Open this project folder in VS Code. The Stitch files are preserved under `stitch-reference/`; the primary React app is under `frontend/web/`.

1. Create the database and load the fictional demonstration records. In Command Prompt from the project root:

   ```cmd
   mysql -u root -p < database\schema.sql
   mysql -u root -p SPMS_DB < database\sample_data.sql
   mysql -u root -p SPMS_DB < database\historical_demo_data.sql
   mysql -u root -p SPMS_DB < database\indexes.sql
   mysql -u root -p SPMS_DB < database\migrations\20261014_identity_payments_performance.sql
   ```

2. Copy `.env.example` to `.env` at the project root and set `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD`. Never commit the local `.env` file.

3. Start the API in a VS Code terminal:

   ```powershell
   .\target\tools\apache-maven-3.9.11\bin\mvn.cmd -o "-Dmaven.repo.local=target/maven-local-repository" -f spring-backend/pom.xml "-DskipTests" spring-boot:run
   ```

   The API listens on `http://localhost:8080/api` by default. Optional `SERVER_PORT` changes the port.

4. In a second terminal, install and start the web frontend:

   ```powershell
   cd frontend/web
   npm install
   npm run dev
   ```

   Open the Vite URL printed in the terminal (normally `http://localhost:5173`). The default API URL is `http://localhost:8080/api`; override it by setting `VITE_API_URL` in `frontend/web/.env.local` and restart Vite.

## Demonstration accounts

After importing sample data, use the fictional Admin and HR accounts `admin` / `Admin@123` and `hr` / `Hr@123`. In the existing database, employee credentials are generated as one-time temporary passwords: open **Administration → User accounts → Issue employee passwords** as Admin, download the CSV once, deliver it privately, and remove it after distribution. Each employee must change their password on first sign-in. Fresh databases also have an E001-linked demo `employee` account that is forced to change its sample password.

Payroll payment records are internal audit confirmations only. SPMS has no bank integration and does not transfer funds.

## Build and test

```powershell
cd frontend/web
npm run build
```

```powershell
.\target\tools\apache-maven-3.9.11\bin\mvn.cmd -f spring-backend/pom.xml test
```

Build and integration verification results for the web stack are recorded in [docs/testing.md](docs/testing.md). Use the project-local Maven command above if `mvn` is not installed on PATH.

## Design and OOAD documentation

The React workspace follows the Stitch color tokens, navigation, typography, spacing, KPI cards, attendance view, leave table, and quick-action patterns. Read [docs/architecture.md](docs/architecture.md), [docs/api.md](docs/api.md), [docs/setup.md](docs/setup.md), and [docs/uml-mapping.md](docs/uml-mapping.md).


# Windows and VS Code setup

## Requirements

- JDK 17 or later
- Maven 3.9 or later
- Node.js 20 or later with npm
- MySQL 8.0 or later
- VS Code (recommended extensions: Extension Pack for Java, Spring Boot Extension Pack, and ES7+ React snippets)

## Database

From MySQL Workbench, open and execute `database/schema.sql`, `database/sample_data.sql`, `database/historical_demo_data.sql`, then `database/indexes.sql`. These scripts are designed to be safely re-run; they do not drop or reset data. Historical seed fills a database below 30 employees, then inserts linked attendance, leave, payroll, performance, and report records without duplicates.

At the repository root, copy `.env.example` to `.env`. Set `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD`. The Java JDBC connection utility reads process environment variables first and falls back to the root `.env` file. Keep `.env` private.

## Start Spring Boot

Open the SPMS root as a VS Code workspace and use a terminal at the root:

```powershell
.\target\tools\apache-maven-3.9.11\bin\mvn.cmd -o "-Dmaven.repo.local=target/maven-local-repository" -f spring-backend/pom.xml "-DskipTests" spring-boot:run
```

The REST API defaults to port 8080. Set `SERVER_PORT` to change it.

## Start React + Vite

Open a second VS Code terminal:

```powershell
cd frontend/web
npm install
npm run dev
```

Open the local URL Vite prints, normally `http://localhost:5173`. The API base URL defaults to `http://localhost:8080/api`. To change it, create `frontend/web/.env.local` with `VITE_API_URL=http://localhost:8080/api`, then restart Vite.

## Build / test

```powershell
cd frontend/web
npm run build
```

In a root terminal:

```powershell
.\target\tools\apache-maven-3.9.11\bin\mvn.cmd -f spring-backend/pom.xml test
.\target\tools\apache-maven-3.9.11\bin\mvn.cmd -f spring-backend/pom.xml package
```

See [testing.md](testing.md) for verified results and current environment limitations.

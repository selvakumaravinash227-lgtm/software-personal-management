# Software Personnel Management System Web Application

The workspace contains the preserved Java Swing/JDBC implementation and a separately runnable web stack. The active browser client is `frontend/web/`. The standalone Spring Boot API is `spring-backend/` and maps its JPA entities to the existing `database/schema.sql`; it does not create or modify database tables automatically (`ddl-auto=none`). The earlier implementation under `backend/` stays in place for comparison and recovery.

## Stitch source inventory

The original archive `stitch_software_personnel_management_system.zip` is preserved unchanged. Its contents are extracted under `stitch-reference/stitch_software_personnel_management_system/`:

- Two `code.html` static dashboard exports.
- One dashboard screenshot (`screen.png`).
- One `workforce_intelligence_design_system/DESIGN.md`.

The archive is not a React project: it has no React components, `package.json`, Vite configuration, or app source. The web app follows its dark navy sidebar, Inter typography, pale workspace, white cards, blue/teal accents, dashboard KPIs, attendance visualization, department chart, leave table, and quick actions. Static example figures from the Stitch HTML are not used as live database values.

## Windows + VS Code setup

Requirements: JDK 17+, Maven 3.9+, Node.js 20.19+ or 22.12+, and MySQL 8+.

1. In MySQL Workbench or Command Prompt from the project root, create/import the schema, baseline, historical data, indexes, and additive workflow migration in this order:

   ```cmd
   mysql -u root -p < database/schema.sql
   mysql -u root -p SPMS_DB < database/sample_data.sql
   mysql -u root -p SPMS_DB < database/historical_demo_data.sql
   mysql -u root -p SPMS_DB < database/indexes.sql
   mysql -u root -p SPMS_DB < database/migrations/20261014_identity_payments_performance.sql
   ```

2. Copy the root `.env.example` to `.env`, then set `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD`. The API reads this file when launched from the project root or `spring-backend`; environment variables can override it.

3. From the project root, start the standalone Spring Boot API:

   ```powershell
   .\target\tools\apache-maven-3.9.11\bin\mvn.cmd -o "-Dmaven.repo.local=target/maven-local-repository" -f spring-backend/pom.xml "-DskipTests" spring-boot:run
   ```

   It listens at `http://localhost:8080/api`. To use another port, set `$env:SERVER_PORT = '8081'` before starting it. If the API uses another port, set `VITE_API_URL` in `frontend/web/.env.local` to `http://localhost:8081/api` and restart Vite.

4. In a second VS Code terminal:

   ```powershell
   cd frontend/web
   npm install
   npm run dev
   ```

   Open the Vite URL printed in the terminal, usually `http://localhost:5173`.

The database schema defines foreign keys and constraints for departments, employees, users, attendance, leave requests, payroll, performance, and report history. The historical seed safely adds enough employees to bring a smaller database to 30 total and then adds linked historical records. It may be re-run; it does not duplicate employee IDs, daily attendance, employee/month payroll, evaluations by period, or seeded leave requests. The Spring API does not run schema-generation commands. `spring.jpa.hibernate.ddl-auto=none` prevents accidental schema changes.

Fresh databases initialized from `sample_data.sql` have `admin` / `Admin@123`, `hr` / `Hr@123`, and the E001-linked `employee` / `Employee@123` demo account. These are local-only sample credentials; the employee account must change its password at first sign-in. On the existing database, employee account passwords were reset to generated temporary values, so sign in as Admin and use the **Issue employee passwords** action to retrieve a fresh one-time credential CSV. Existing PBKDF2 demo hashes are verified and upgraded to BCrypt after successful sign-in; newly provisioned or reset passwords use BCrypt.

### Employee accounts and passwords

An administrator signs in with the demo admin account, opens **Administration → User accounts**, and chooses **Provision missing accounts** to create accounts for employees who do not have one. The separate **Issue employee passwords** action creates a new random temporary password for every active linked employee account, revokes those employees' existing sessions, and displays a one-time CSV download. Deliver that file privately and remove it after use; the API stores only BCrypt hashes. Each employee must change the temporary password at first sign-in before accessing SPMS. Usernames for newly provisioned accounts use `employee.<employee-id-lowercase>`; valid existing usernames remain unchanged. New employee IDs are assigned as the next unused `EMP####` value when the ID field is left blank.

### Leave and payroll workflows

Employees submit their own leave applications from **Attendance & Leave Management → Leave Requests**. HR Managers see Approve and Reject actions beside pending requests in that same section; Admins may also review. Rejection requires a reason. After review, the employee sees the persisted status, reviewer, review date, and decision note in their request history, with the latest decision highlighted at the top of the section. Leave ranges overlapping an existing pending or approved request are rejected. Performance forms store five scores (1–5), and the server calculates the overall rating as their arithmetic mean.

Payroll rows move from **Draft** to **Processed**. HR/Admin can then choose **Record payment** and confirm that the salary was paid outside SPMS. This records the initiator/confirmer, amount, timestamps, and optional reference in `payroll_payments` and updates the payroll status in one transaction. SPMS has no bank/payment-provider integration and does not transfer funds.

For an existing database, back up `SPMS_DB` first, then run `database/migrations/20261014_identity_payments_performance.sql`. The migration is additive and repeatable: it adds first-login password flags and structured performance fields, and creates the payroll payment audit table without dropping or resetting existing tables or rows.

## Build and verification commands

```powershell
cd frontend/web
npm run build
cd ../..
.\target\tools\apache-maven-3.9.11\bin\mvn.cmd -f spring-backend/pom.xml test
```

To check the database employee count:

```sql
USE SPMS_DB;
SELECT COUNT(*) AS employee_count, COUNT(DISTINCT employee_id) AS distinct_ids FROM employees;
```

See `docs/testing.md` for the latest actual build, database and API verification results. No database credentials are committed to this project.

For the separate Vercel frontend and hosted Spring/MySQL deployment, follow [`vercel-deployment.md`](vercel-deployment.md). Vercel environment variables must be configured before building because Vite embeds `VITE_API_URL` in the public frontend bundle.

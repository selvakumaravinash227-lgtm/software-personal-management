# Testing and verification

Verification performed on 14 October 2026 against the configured local MySQL `SPMS_DB` and the Spring API on port 8093. The pre-change backup is under `preservation/repair-20261014-204940/`.

## Build results

- `npm run build` in `frontend/web` — passed with Vite 8.3.4.
- Maven `compile` for `spring-backend/pom.xml` — passed with Java 17.
- Maven test sources compile, but `mvn test` cannot execute offline because the local cache does not contain `org.junit.platform:junit-platform-launcher:1.11.4`. The build stops before JUnit tests run. Network resolution was not available in the offline Maven invocation.
- No automated browser/UI test was run.

## MySQL verification

The final database contains:

| Data | Count |
| --- | ---: |
| Employees | 30 |
| Linked employee user accounts | 30 |
| Attendance rows | 2,020 |
| Leave requests | 41 |
| Payroll rows | 93 |
| Performance evaluations | 93 |

Employee IDs and email addresses are unique. All 30 employees have attendance, leave, payroll, and performance history. Payroll contains 30 rows each for July, August, and September 2026 plus 3 existing October rows. Historical totals and records were retained. After the final temporary-password rotation, all 30 linked employee accounts have `must_change_password=1`; temporary passwords were not printed or saved in this report.

Re-ran `sample_data.sql`, `historical_demo_data.sql`, `indexes.sql`, and `migrations/20261014_identity_payments_performance.sql`. All completed successfully. Final row counts above remained unchanged, demonstrating repeatable seed/migration execution for the current database.

## API checks

The Spring API started successfully, opened a MySQL connection, and served authenticated requests. After the final seed run, the authenticated admin API returned 30 employees, 2,020 attendance rows, 41 leave requests, 93 performance records, and dashboard data. Payroll history search is supported through the API `search` query parameter, including employee ID/name and period text.

Previously executed workflow checks in the same repair run:

- Authentication: invalid credentials rejected; forced password change required for temporary employee accounts; short password rejected; password change succeeded; employee could access only their own employee, leave, attendance, and payslip data; employee/admin-only operation denials returned 403 where checked.
- Employees: generated ID, duplicate ID rejection, create/edit/delete, list/search, and employee ownership scoping.
- Attendance: employee self-marking without a supplied employee ID, duplicate daily mark upsert, and employee history scoping.
- Leave: application, invalid date/overlap rejection, required rejection remarks, HR approval/rejection, employee history scoping, and unauthorized employee decision rejection.
- Payroll: backend salary calculation, duplicate employee/month prevention, processing transition, internal payment recording guard, no-bank-transfer messaging, and employee payslip scoping. The test did not represent an actual bank payment.
- Performance: create/update, calculated rating summary, employee history scoping, and employee write restriction.
- Reports/dashboard: five report categories and database-backed dashboard values; current final API smoke call also confirmed the dashboard endpoint responds.

Temporary workflow rows were removed after testing. The final repeatable seed contains the demonstration data described above. Report audit history created by the report checks is retained.

## Not verified

- JUnit assertions did not execute because `junit-platform-launcher:1.11.4` is missing from the offline local Maven repository.
- No browser-driven UI test, restart-and-refresh test, or print/PDF rendering test was run in this verification pass.
- SPMS records external payment confirmation for audit; it has no bank integration. A real payment was neither initiated nor tested.

## Deployment preparation check — 18 October 2026

- Parsed `frontend/web/vercel.json`, `package.json`, and `package-lock.json` successfully.
- `npm run build` passed with a production-style HTTPS `VITE_API_URL`; the generated bundle contained that API URL and did not contain the local API fallback.
- Spring Boot `compile` passed after adding the API health endpoint and container deployment settings.
- Local `GET /api/health` returned `{"status":"UP","database":"UP"}` against the configured MySQL database.
- No Vercel deployment, hosted Java container, or managed production MySQL connection was run from this workspace. Those require the user's provider accounts, hosted URLs, and private credentials.

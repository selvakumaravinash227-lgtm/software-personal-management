# SPMS_DB setup

# SPMS database setup

Requires MySQL 8.0+. From the repository root, initialize in this order:

1. `schema.sql` — creates `SPMS_DB` and tables if absent. It does not drop tables.
2. `sample_data.sql` — repeatable fictional baseline records. It inserts missing records only and leaves existing records unchanged.
3. `historical_demo_data.sql` — repeatable historical records for attendance, leave, payroll, performance, and report audit. It fills the employee pool to at least 30 total records when fewer than 30 exist, preserves existing employee IDs, and adds three completed monthly payroll periods.
4. `indexes.sql` — creates missing indexes only and may be re-run.

Use MySQL Workbench to open and execute each SQL file in order, or from PowerShell run these commands (replace `root` with your MySQL account):

```powershell
cmd /c "mysql -u root -p < database\schema.sql"
cmd /c "mysql -u root -p SPMS_DB < database\sample_data.sql"
cmd /c "mysql -u root -p SPMS_DB < database\historical_demo_data.sql"
cmd /c "mysql -u root -p SPMS_DB < database\indexes.sql"
```

The history script uses the schema's supported attendance values (`PRESENT`, `ABSENT`, `LEAVE`); half-day is not represented in the existing schema. Performance strengths and development areas are recorded in `remarks`, since the schema has no separate columns. Payroll amounts use the generated `net_salary` formula in MySQL. Historical processed rows do not represent bank transfers.

The schema has no reporting-manager field. No manager column is added by the seed.

Demo accounts are documented in the root README. Change them before using non-demo data.

```cmd
mysql -u root -p < database/schema.sql
mysql -u root -p SPMS_DB < database/sample_data.sql
mysql -u root -p SPMS_DB < database/indexes.sql
```


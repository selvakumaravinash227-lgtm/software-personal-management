# SPMS REST API

Base URL: `http://localhost:8080/api`. Except login, endpoints require `Authorization: Bearer <session-token>`. Requests and responses use JSON. Role and ownership checks are performed by server-side services.

| Method and path | Operation |
|---|---|
| `POST /auth/login` | Authenticate `{username,password}`, return `{token,user}`. |
| `POST /auth/logout` | Revoke the current session. |
| `POST /auth/password` | Change password using `{currentPassword,newPassword}`; new passwords must be at least 12 characters. Temporary-password sessions may call only this endpoint or logout until changed. |
| `GET /auth/me` | Return the authenticated account. |
| `GET /dashboard` | Live dashboard metrics and department/activity summaries. |
| `GET /employees?search=` | Search employee records. `POST /employees`, `PUT/DELETE /employees/{id}` manage records. |
| `GET/POST /attendance` | List / mark attendance. |
| `GET/POST /leaves` | List / apply for leave. `POST /leaves/{id}/decision` approves or rejects. |
| `GET/POST /payroll` | List / create payroll. `POST /payroll/{id}/process` processes a draft. `POST /payroll/{id}/paid` records HR-confirmed external payment with `{confirmExternally:true,reference}`. This records an audit event but does not transfer funds. |
| `GET/POST /performance` | List / add evaluations with five 1–5 score inputs; the server calculates the mean rating. `PUT /performance/{id}` updates an evaluation. |
| `GET/POST /departments` | List / create departments. `PUT/DELETE /departments/{id}` updates/deletes. |
| `GET /users` | List safe account metadata (no hashes). `POST /users` creates an account. `POST /users/provision-employees` creates missing accounts with one-time generated credentials. `POST /users/reset-employee-passwords` resets all active employee accounts and returns temporary passwords once. `POST /users/{id}/reset-password` resets one account. `PUT /users/{id}/role` assigns a role; `PUT /users/{id}/active` changes account status. Admin only. |
| `GET /reports` | List report history. `POST /reports/{type}` queries and logs a report and returns its rows. |

Supported report types are `employees`, `attendance`, `leaves`, `payroll`, and `performance`. Validation errors use HTTP 400; unauthorized and forbidden requests use HTTP 401/403; database unavailable errors use HTTP 503.

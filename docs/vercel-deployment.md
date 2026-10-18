# Vercel deployment guide

The React + Vite frontend is prepared for Vercel. Deploy the Spring Boot API and MySQL separately: Vercel's documented official function runtimes do not include Java, while this backend is a persistent Spring Boot service ([Vercel runtimes](https://vercel.com/docs/functions/runtimes)). The Vite SPA rewrite follows Vercel's [Vite deployment guide](https://vercel.com/docs/frameworks/frontend/vite).

This config prepares the project for deployment; no Vercel account, API host, or managed MySQL instance is connected from this workspace, so a live deployment has not been performed.

## 1. Deploy the API and database

Use a Java/container host for the Spring Boot API and a managed MySQL 8 service with network access from that API host. Keep MySQL private where the provider allows it. Build the backend Docker image using the **repository root** as its build context and `spring-backend/Dockerfile` as its Dockerfile path. The container binds to the provider's `PORT` environment variable.

Set these environment variables in the API host's private configuration:

| Variable | Value |
| --- | --- |
| `DB_URL` | `jdbc:mysql://<mysql-host>:3306/SPMS_DB?sslMode=REQUIRED&serverTimezone=UTC` |
| `DB_USERNAME` | A dedicated database user with only the privileges SPMS needs |
| `DB_PASSWORD` | The database user's password |
| `SPMS_CORS_ALLOWED_ORIGINS` | Exact Vercel origin(s), comma-separated with no trailing slash |
| `PORT` | Supplied by most container hosts; the app also accepts `SERVER_PORT` |

Example CORS value: `https://spms-demo.vercel.app,https://spms.example.org`. Include each production/custom domain explicitly. Do not use `*`. Preview deployments need their exact preview origins added if they are to call the API.

Run `database/schema.sql` and `database/migrations/20261014_identity_payments_performance.sql` against the managed database. Run `database/sample_data.sql` and `database/historical_demo_data.sql` only when fictional demonstration data is wanted. Do not import the sample users into a public production system without changing their known demo passwords. The Spring app does not run schema creation or sample seeding at startup.

Configure the host health check to use `GET /api/health`. It checks that the API can get a valid database connection and returns only generic UP/DOWN status.

SPMS currently keeps opaque login tokens in memory. Start with one API instance; a restart invalidates active sessions. Multiple API replicas require a shared session/token store before enabling them.

## 2. Deploy the frontend to Vercel

Import the repository in Vercel and set:

- **Root Directory:** `frontend/web`
- **Framework Preset:** Vite
- **Install Command:** `npm ci`
- **Build Command:** `npm run build`
- **Output Directory:** `dist`

`frontend/web/vercel.json` supplies these build settings and the SPA fallback rewrite. Add this Vercel Project Environment Variable for Production (and Preview only if the preview origin is in the API CORS list):

```text
VITE_API_URL=https://<your-api-host>/api
```

This URL is public because it is compiled into browser JavaScript. Never put `DB_PASSWORD`, API secrets, or other credentials in a `VITE_*` variable. Redeploy after changing frontend environment variables.

Before publishing a public URL, change the known sample `admin` and `hr` passwords, remove any demo accounts you do not want public, and issue employee temporary passwords privately. Do not commit `.env`, `.env.local`, or the one-time employee credential CSV.

## 3. Verify the deployment

1. Open `https://<your-api-host>/api/health`; expect `{"status":"UP","database":"UP"}`.
2. Open the Vercel site and confirm assets load over HTTPS.
3. Log in with a private account and confirm the dashboard loads without a CORS or API URL error.
4. Verify a database-backed page such as Employee Management and confirm records load.
5. Check the browser developer console and Network panel for failed requests; requests should go to the HTTPS API origin.
6. Submit one leave request and review it with HR, then confirm the employee sees the stored decision.
7. Check API logs for database connection errors; do not expose SQL details to the browser.

If the browser reports a CORS error, set `SPMS_CORS_ALLOWED_ORIGINS` to the exact `https://` origin shown in the browser address bar, restart/redeploy the API, and retry.

## Local production-build check

From the frontend folder in PowerShell, set a placeholder production API URL and build:

```powershell
$env:VITE_API_URL = 'https://spms-api.example.org/api'
npm ci
npm run build
Remove-Item Env:VITE_API_URL
```

This checks the Vite production build only. It does not validate a real deployment, managed MySQL networking, CORS at the chosen host, or deployed credentials.

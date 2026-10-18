# SPMS static frontend deployment

The deployable static frontend output is `frontend/web/dist/`. Build it from `frontend/web` with Node.js 20.19 or newer:

```powershell
cd "C:\Users\DELL\Documents\software personal management\frontend\web"
npm ci
npm run build
```

## Drag-and-drop deploy

- **Netlify:** open the site's manual deploy page and drag the complete `dist` folder.
- **Vercel Drop:** drag the complete `dist` folder (it must contain `index.html` and `assets/`) and choose `index.html` as the root page if prompted.

The `public/_redirects` file is copied into `dist` for Netlify single-page navigation. The Vercel project configuration in this directory provides the equivalent SPA rewrite for Git deployments.

## Backend requirement

This folder contains only the React website. It cannot run the Java Spring Boot API or MySQL. The login screen can load without the API, but login and data workflows require a separately hosted, public HTTPS API and database. Do not use `localhost` or a private Wi-Fi address in the deployed frontend. Before building for a live API, set `VITE_API_URL` to the actual public API base ending in `/api`, for example:

```powershell
$env:VITE_API_URL = 'https://<your-public-api-host>/api'
npm run build
Remove-Item Env:VITE_API_URL
```

The API host must allow the exact deployed website origin through `SPMS_CORS_ALLOWED_ORIGINS`. Since Vite embeds `VITE_API_URL` into the build, rebuild and redeploy after changing it. Never put database passwords or other secrets in a `VITE_*` variable.

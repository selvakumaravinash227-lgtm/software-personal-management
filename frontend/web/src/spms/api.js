const configuredApi = import.meta.env.VITE_API_URL?.trim();
const API = (configuredApi || (import.meta.env.DEV ? 'http://localhost:8080/api' : '')).replace(/\/+$/, '');
const tokenKey = 'spms_session';
export const getToken = () => sessionStorage.getItem(tokenKey);
export const setToken = value => value ? sessionStorage.setItem(tokenKey, value) : sessionStorage.removeItem(tokenKey);
export async function request(path, options = {}) {
  if (!API) throw new Error('SPMS API is not configured. Set VITE_API_URL to the deployed backend URL ending in /api.');
  const headers = { 'Content-Type': 'application/json', ...(getToken() ? { Authorization: `Bearer ${getToken()}` } : {}), ...options.headers };
  const response = await fetch(`${API}${path}`, { ...options, headers });
  if (response.status === 204) return null;
  const result = await response.json().catch(() => ({}));
  if (response.status === 401 && path !== '/auth/login') { setToken(null); sessionStorage.removeItem('spms_user'); window.location.reload(); }
  if (!response.ok) throw new Error(result.message || result.error || `Request failed (${response.status})`);
  return result;
}
export const signIn = async (username, password) => { const s = await request('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) }); setToken(s.token); return s.user; };
export const changePassword = async (currentPassword, newPassword) => { const s = await request('/auth/password', { method: 'POST', body: JSON.stringify({ currentPassword, newPassword }) }); setToken(s.token); return s.user; };
export const signOut = async () => { try { await request('/auth/logout', { method: 'POST' }); } finally { setToken(null); } };

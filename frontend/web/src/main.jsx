import React from 'react';
import { createRoot } from 'react-dom/client';
import App from './spms/App.jsx';
import './spms/styles.css';

createRoot(document.getElementById('root')).render(<React.StrictMode><App /></React.StrictMode>);

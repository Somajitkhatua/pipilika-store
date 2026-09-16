const API_BASE_URL = 'http://localhost:8080/api';

async function apiRequest(endpoint, method = 'GET', data = null, requiresAuth = true) {
    const headers = {
        'Content-Type': 'application/json'
    };

    if (requiresAuth) {
        const token = localStorage.getItem('token');
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
    }

    const config = {
        method,
        headers
    };

    if (data && (method === 'POST' || method === 'PUT')) {
        config.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        
        if (response.status === 401 || response.status === 403) {
            localStorage.clear();
            window.location.href = '/login.html';
            throw new Error('Unauthorized or session expired');
        }

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            const result = await response.json();
            if (!response.ok) {
                throw new Error(result.message || 'Something went wrong');
            }
            return result;
        }

        if (!response.ok) {
            throw new Error('Something went wrong');
        }

        return {};
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

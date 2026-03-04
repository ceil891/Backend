/**
 * Fetch API Example (không dùng Axios)
 * Tương thích với Smart Retail Backend
 */

const API_BASE_URL = 'http://localhost:8080';

// Helper function để lấy token
const getToken = () => {
  return localStorage.getItem('token');
};

// Helper function để tạo headers
const createHeaders = (includeAuth = true) => {
  const headers = {
    'Content-Type': 'application/json',
  };
  
  if (includeAuth) {
    const token = getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
  }
  
  return headers;
};

// Helper function để xử lý response
const handleResponse = async (response) => {
  if (!response.ok) {
    if (response.status === 401) {
      // Token hết hạn hoặc không hợp lệ
      localStorage.removeItem('token');
      window.location.href = '/login';
      throw new Error('Phiên đăng nhập đã hết hạn');
    }
    
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || `HTTP error! status: ${response.status}`);
  }
  
  return response.json();
};

// Auth API
export const authAPI = {
  async login(email, password) {
    const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
      method: 'POST',
      headers: createHeaders(false),
      body: JSON.stringify({ email, password }),
    });
    
    const data = await handleResponse(response);
    
    if (data.success && data.data?.accessToken) {
      localStorage.setItem('token', data.data.accessToken);
    }
    
    return data;
  },

  async register(userData) {
    const response = await fetch(`${API_BASE_URL}/api/auth/register`, {
      method: 'POST',
      headers: createHeaders(false),
      body: JSON.stringify(userData),
    });
    
    return handleResponse(response);
  },

  async logout() {
    try {
      await fetch(`${API_BASE_URL}/api/auth/logout`, {
        method: 'POST',
        headers: createHeaders(true),
      });
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      localStorage.removeItem('token');
    }
  },
};

// Product API Example
export const productAPI = {
  async getAll() {
    const response = await fetch(`${API_BASE_URL}/api/v1/san-pham`, {
      headers: createHeaders(true),
    });
    return handleResponse(response);
  },

  async getById(id) {
    const response = await fetch(`${API_BASE_URL}/api/v1/san-pham/${id}`, {
      headers: createHeaders(true),
    });
    return handleResponse(response);
  },

  async create(productData) {
    const response = await fetch(`${API_BASE_URL}/api/v1/san-pham`, {
      method: 'POST',
      headers: createHeaders(true),
      body: JSON.stringify(productData),
    });
    return handleResponse(response);
  },

  async update(id, productData) {
    const response = await fetch(`${API_BASE_URL}/api/v1/san-pham/${id}`, {
      method: 'PUT',
      headers: createHeaders(true),
      body: JSON.stringify(productData),
    });
    return handleResponse(response);
  },

  async delete(id) {
    const response = await fetch(`${API_BASE_URL}/api/v1/san-pham/${id}`, {
      method: 'DELETE',
      headers: createHeaders(true),
    });
    return handleResponse(response);
  },
};

// Usage Example
/*
// Login
try {
  const result = await authAPI.login('user@example.com', 'Password123!');
  console.log('Login success:', result);
} catch (error) {
  console.error('Login error:', error.message);
}

// Get Products
try {
  const result = await productAPI.getAll();
  console.log('Products:', result.data);
} catch (error) {
  console.error('Error:', error.message);
}
*/

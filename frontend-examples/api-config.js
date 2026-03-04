/**
 * API Configuration cho Frontend
 * Tương thích với Smart Retail Backend
 */

// Base URLs
export const API_BASE_URL = 'http://localhost:8080';
export const AUTH_API = `${API_BASE_URL}/api/auth`;
export const BUSINESS_API = `${API_BASE_URL}/api/v1`;

// Axios Configuration
import axios from 'axios';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor - Thêm token vào header
apiClient.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor - Xử lý lỗi
apiClient.interceptors.response.use(
  (response) => {
    // Backend trả về data trong response.data
    return response;
  },
  (error) => {
    // Xử lý lỗi 401 - Unauthorized
    if (error.response?.status === 401) {
      clearAuth();
      // Redirect to login
      if (typeof window !== 'undefined') {
        window.location.href = '/login';
      }
    }
    
    // Xử lý lỗi khác
    const errorMessage = error.response?.data?.message || 
                        error.message || 
                        'Có lỗi xảy ra. Vui lòng thử lại.';
    
    return Promise.reject({
      ...error,
      message: errorMessage,
    });
  }
);

// Token Management
export const getToken = () => {
  if (typeof window !== 'undefined') {
    return localStorage.getItem('token');
  }
  return null;
};

export const setToken = (token) => {
  if (typeof window !== 'undefined') {
    localStorage.setItem('token', token);
  }
};

export const clearAuth = () => {
  if (typeof window !== 'undefined') {
    localStorage.removeItem('token');
  }
};

export const isAuthenticated = () => {
  return !!getToken();
};

// Auth API Methods
export const authAPI = {
  login: async (email, password) => {
    const response = await apiClient.post('/api/auth/login', {
      email,
      password,
    });
    
    if (response.data.success && response.data.data?.accessToken) {
      setToken(response.data.data.accessToken);
    }
    
    return response.data;
  },

  register: async (userData) => {
    const response = await apiClient.post('/api/auth/register', userData);
    return response.data;
  },

  logout: async () => {
    try {
      await apiClient.post('/api/auth/logout');
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      clearAuth();
    }
  },
};

// Business API Methods Examples
export const productAPI = {
  getAll: () => apiClient.get('/api/v1/san-pham'),
  getById: (id) => apiClient.get(`/api/v1/san-pham/${id}`),
  create: (data) => apiClient.post('/api/v1/san-pham', data),
  update: (id, data) => apiClient.put(`/api/v1/san-pham/${id}`, data),
  delete: (id) => apiClient.delete(`/api/v1/san-pham/${id}`),
  search: (keyword) => apiClient.get(`/api/v1/san-pham/search?keyword=${keyword}`),
};

export const orderAPI = {
  getAll: () => apiClient.get('/api/v1/hoa-don'),
  getById: (id) => apiClient.get(`/api/v1/hoa-don/${id}`),
  create: (data) => apiClient.post('/api/v1/hoa-don', data),
  updateStatus: (id, status) => 
    apiClient.put(`/api/v1/hoa-don/${id}/trang-thai`, { trangThai: status }),
};

export default apiClient;

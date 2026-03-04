/**
 * React Auth Context Example
 * Quản lý authentication state trong React app
 */

import React, { createContext, useContext, useState, useEffect } from 'react';
import { authAPI } from './api-config';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Kiểm tra token khi app khởi động
    const token = localStorage.getItem('token');
    if (token) {
      setIsAuthenticated(true);
      // Có thể decode token để lấy user info
      // hoặc gọi API để lấy user info
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const result = await authAPI.login(email, password);
      if (result.success) {
        setIsAuthenticated(true);
        // Có thể lưu thêm user info nếu cần
        return result;
      }
      throw new Error(result.message || 'Đăng nhập thất bại');
    } catch (error) {
      setIsAuthenticated(false);
      throw error;
    }
  };

  const register = async (userData) => {
    try {
      const result = await authAPI.register(userData);
      return result;
    } catch (error) {
      throw error;
    }
  };

  const logout = async () => {
    try {
      await authAPI.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setUser(null);
      setIsAuthenticated(false);
    }
  };

  const value = {
    user,
    isAuthenticated,
    loading,
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

// Usage trong App.jsx
/*
import { AuthProvider } from './context/AuthContext';
import { BrowserRouter } from 'react-router-dom';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/dashboard" element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          } />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
*/

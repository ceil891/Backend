/**
 * Vue 3 Composition API Example
 * Quản lý authentication trong Vue 3
 */

import { ref, computed } from 'vue';
import { authAPI } from './api-config';
import router from './router';

// Auth Store (Composable)
export const useAuth = () => {
  const token = ref(localStorage.getItem('token'));
  const user = ref(null);
  const loading = ref(false);
  const error = ref(null);

  const isAuthenticated = computed(() => !!token.value);

  const login = async (email, password) => {
    loading.value = true;
    error.value = null;
    
    try {
      const result = await authAPI.login(email, password);
      if (result.success) {
        token.value = localStorage.getItem('token');
        // Có thể lưu user info
        router.push('/dashboard');
        return result;
      }
      throw new Error(result.message || 'Đăng nhập thất bại');
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const register = async (userData) => {
    loading.value = true;
    error.value = null;
    
    try {
      const result = await authAPI.register(userData);
      if (result.success) {
        router.push('/login');
      }
      return result;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const logout = async () => {
    try {
      await authAPI.logout();
      token.value = null;
      user.value = null;
      router.push('/login');
    } catch (err) {
      console.error('Logout error:', err);
    }
  };

  return {
    token,
    user,
    isAuthenticated,
    loading,
    error,
    login,
    register,
    logout,
  };
};

// Router Guard Example
export const requireAuth = (to, from, next) => {
  const token = localStorage.getItem('token');
  if (token) {
    next();
  } else {
    next('/login');
  }
};

// Usage trong component
/*
<template>
  <div>
    <form @submit.prevent="handleLogin">
      <input v-model="email" type="email" placeholder="Email" />
      <input v-model="password" type="password" placeholder="Password" />
      <button type="submit" :disabled="loading">
        {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
      </button>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuth } from '@/composables/useAuth';

const email = ref('');
const password = ref('');
const { login, loading, error } = useAuth();

const handleLogin = async () => {
  try {
    await login(email.value, password.value);
  } catch (err) {
    console.error('Login failed:', err);
  }
};
</script>
*/

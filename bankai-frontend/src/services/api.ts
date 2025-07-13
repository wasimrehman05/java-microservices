import axios, { AxiosResponse } from 'axios';

// Base URLs for your microservices (configured in axios instances below)

// Create axios instance for auth service
const authApi = axios.create({
  baseURL: 'http://localhost:8080',
});

// Create axios instance for user service
const userApi = axios.create({
  baseURL: 'http://localhost:8081',
});

// Request interceptor to add auth token for auth API
authApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Request interceptor to add auth token for user API
userApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor to handle errors for auth API
authApi.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors for user API
userApi.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// TypeScript interfaces
export interface User {
  id: number;
  name: string;
  email: string;
  phone: string;
}

export interface AuthRequest {
  email: string;
  password: string;
}

export interface RegisterRequest extends AuthRequest {
  name: string;
  phone: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  email: string;
  name: string;
  role: string;
}

export interface PagedResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface UserRequest {
  name: string;
  email: string;
  phone: string;
}

// Auth Service API
export const authAPI = {
  login: async (credentials: AuthRequest): Promise<AuthResponse> => {
    const response: AxiosResponse<AuthResponse> = await authApi.post(
      '/api/auth/login',
      credentials
    );
    return response.data;
  },

  register: async (userData: RegisterRequest): Promise<AuthResponse> => {
    const response: AxiosResponse<AuthResponse> = await authApi.post(
      '/api/auth/register',
      userData
    );
    return response.data;
  },
};

// User Service API
export const userAPI = {
  getAllUsers: async (page: number = 1, size: number = 10): Promise<PagedResponse<User>> => {
    const response: AxiosResponse<PagedResponse<User>> = await userApi.get(
      `/api/users?page=${page}&size=${size}`
    );
    return response.data;
  },

  getUserById: async (id: number): Promise<User> => {
    const response: AxiosResponse<User> = await userApi.get(`/api/users/${id}`);
    return response.data;
  },

  createUser: async (userData: UserRequest): Promise<User> => {
    const response: AxiosResponse<User> = await userApi.post('/api/users', userData);
    return response.data;
  },

  updateUser: async (id: number, userData: UserRequest): Promise<User> => {
    const response: AxiosResponse<User> = await userApi.put(`/api/users/${id}`, userData);
    return response.data;
  },

  deleteUser: async (id: number): Promise<void> => {
    await userApi.delete(`/api/users/${id}`);
  },
};

// Auth helper functions
export const authHelpers = {
  setToken: (token: string) => {
    localStorage.setItem('token', token);
  },

  getToken: (): string | null => {
    return localStorage.getItem('token');
  },

  removeToken: () => {
    localStorage.removeItem('token');
  },

  isAuthenticated: (): boolean => {
    return !!localStorage.getItem('token');
  },

  getCurrentUser: (): { name: string; email: string; role: string } | null => {
    const token = localStorage.getItem('token');
    if (!token) return null;
    
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        name: payload.name,
        email: payload.sub,
        role: payload.role
      };
    } catch {
      return null;
    }
  }
}; 
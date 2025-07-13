# 🚀 Bankai - Microservices Application

## 📋 Overview

**Bankai** is a comprehensive full-stack microservices application demonstrating modern enterprise architecture with Spring Boot backend services, React TypeScript frontend, and PostgreSQL database integration. Built for scalability, security, and maintainability.

## 🏗️ Architecture

### System Architecture
```
┌─────────────────┐    REST API     ┌─────────────────┐    HTTP/JWT     ┌─────────────────┐
│  React Frontend │ ──────────────> │   Auth-Service  │ ──────────────> │   User-Service  │
│  (Port 3000)    │                 │   (Port 8080)   │                 │   (Port 8081)   │
│                 │                 │                 │                 │                 │
│ • Login/Register│                 │ • Registration  │                 │ • User Profiles │
│ • Dashboard     │                 │ • Authentication│                 │ • CRUD Ops      │
│ • User Mgmt     │                 │ • JWT Tokens    │                 │ • JWT Validation│
│ • Material-UI   │                 │ • CORS Config   │                 │ • Pagination    │
└─────────────────┘                 └─────────────────┘                 └─────────────────┘
        │                                    │                                    │
        │                                    │                                    │
        │                             ┌──────▼─────┐                      ┌───────▼───────┐
        │                             │  Auth DB   │                      │    User DB    │
        │                             │(auth_users)│                      │(user_profiles)│
        │                             └────────────┘                      └───────────────┘
        │                                    │                                    │
        │                                    └────────────────────────────────────┘
        │                                                  │
        └──────────────────────────────────────────────────┼───────────────────────────┘
                                                           │
                                                 ┌─────────▼─────────┐
                                                 │ PostgreSQL Server │
                                                 │                   │
                                                 │ • auth_users      │
                                                 │ • user_profiles   │
                                                 └───────────────────┘
```

### Technology Stack

#### Frontend
- **React 18** with TypeScript
- **Material-UI (MUI)** for modern UI components
- **Axios** for HTTP client with interceptors
- **React Router** for navigation
- **JWT Token Management** with automatic refresh

#### Backend
- **Spring Boot 3.x** microservices
- **Spring Security 6** with JWT authentication
- **Spring Data JPA** with Hibernate
- **PostgreSQL** database
- **Maven** for dependency management
- **OpenAPI 3** with Swagger UI

#### Infrastructure
- **PostgreSQL** (Supabase hosted)
- **CORS** configuration for cross-origin requests
- **JWT** shared secret for service communication
- **Environment Variables** for configuration

## 🚀 Local Setup Instructions

### Prerequisites
- **Java 17+** installed
- **Node.js 18+** and npm installed
- **Maven 3.6+** installed
- **PostgreSQL** database (hosted or local)

### 1. Database Setup

Create `.env` files in both service directories:

**auth-service/.env:**
```env
DB_NAME=postgres
DB_USERNAME=postgres
DB_PASSWORD=your_password
DB_HOST=your_host
DB_PORT=5432
```

**user-service/.env:**
```env
DB_NAME=postgres
DB_USERNAME=postgres
DB_PASSWORD=your_password
DB_HOST=your_host
DB_PORT=5432
```

### 2. Build Security Common Module
```bash
cd security-common
mvn clean install
```

### 3. Start Backend Services

#### Start Auth Service (Port 8080)
```bash
cd auth-service
mvn spring-boot:run
```

#### Start User Service (Port 8081)
```bash
cd user-service
mvn spring-boot:run
```

### 4. Start Frontend (Port 3000)
```bash
cd bankai-frontend
npm install
npm start
```

## 🧪 API Testing

### Authentication Endpoints

#### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

#### Login User
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

### User Management Endpoints

#### Get All Users (Protected)
```bash
curl -X GET "http://localhost:8081/api/users?page=1&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Create User (Protected)
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "+1234567890"
  }'
```

#### Update User (Protected)
```bash
curl -X PUT http://localhost:8081/api/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Jane Smith Updated",
    "email": "jane.updated@example.com",
    "phone": "+0987654321"
  }'
```

#### Delete User (Protected)
```bash
curl -X DELETE http://localhost:8081/api/users/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📊 API Documentation

### Swagger UI Access
- **Auth Service**: http://localhost:8080/swagger-ui.html
- **User Service**: http://localhost:8081/swagger-ui.html

### Health Checks
- **Auth Service**: http://localhost:8080/actuator/health
- **User Service**: http://localhost:8081/health

## 🔑 Key Features

### ✅ **Security & Authentication**
- JWT-based authentication with automatic token refresh
- CORS configuration for frontend integration
- Protected API endpoints with role-based access
- Secure password hashing with BCrypt

### ✅ **Frontend Features**
- Modern React TypeScript application
- Material-UI components with responsive design
- Protected routes with authentication guards
- Real-time form validation and error handling
- Pagination and search functionality

### ✅ **Backend Services**
- Microservices architecture with Spring Boot
- PostgreSQL database integration
- Service-to-service communication
- Comprehensive error handling and logging
- API documentation with OpenAPI 3

### ✅ **Database Design**
- Separate tables for different services
- Hibernate auto-schema generation
- Environment-based configuration
- Connection pooling and optimization

## 🏃‍♂️ Running the Complete Application

### Quick Start (All Services)
```bash
# Terminal 1: Start Auth Service
cd auth-service && mvn spring-boot:run

# Terminal 2: Start User Service  
cd user-service && mvn spring-boot:run

# Terminal 3: Start Frontend
cd bankai-frontend && npm start
```

### Application URLs
- **Frontend**: http://localhost:3000
- **Auth Service**: http://localhost:8080
- **User Service**: http://localhost:8081

## 🚨 Troubleshooting

### Common Issues

#### CORS Errors
- Ensure both services are running on correct ports
- Check CORS configuration in WebConfig classes
- Verify Origin headers in browser dev tools

#### Database Connection Issues
- Verify `.env` files are properly configured
- Check PostgreSQL database is accessible
- Ensure correct database credentials

#### JWT Token Issues
- Check token expiration (24 hours default)
- Verify shared JWT secret across services
- Ensure proper Authorization header format

#### Service Communication
- Verify all services are running on expected ports
- Check service health endpoints
- Review application logs for detailed errors

### Log Locations
- **Auth Service**: `auth-service/logs/auth-service.log`
- **User Service**: `user-service/logs/user-service.log`
- **Frontend**: Browser console and network tab

## 🎯 Production Considerations

### Security Enhancements
- Use environment-specific JWT secrets
- Implement refresh token rotation
- Add rate limiting and request validation
- Enable HTTPS in production

### Performance Optimization
- Implement caching strategies
- Add connection pooling
- Use CDN for static assets
- Optimize database queries

### Monitoring & Logging
- Add distributed tracing
- Implement health checks
- Set up metrics collection
- Configure log aggregation


---

**Built with ❤️ using Spring Boot, React, and PostgreSQL** 
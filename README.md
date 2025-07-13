# 🚀 Bankai Microservices Integration Guide

## 📋 Overview
This guide demonstrates the complete integration between Auth-Service and User-Service microservices.

## 🏗️ Architecture
```
┌─────────────────┐    HTTP/JWT     ┌─────────────────┐
│   Auth-Service  │ ──────────────> │   User-Service  │
│   (Port 8080)   │                 │   (Port 8081)   │
│                 │                 │                 │
│ • Registration  │                 │ • User Profiles │
│ • Authentication│                 │ • CRUD Ops      │
│ • JWT Tokens    │                 │ • JWT Validation│
└─────────────────┘                 └─────────────────┘
        │                                    │
        │                                    │
   ┌────▼────┐                          ┌────▼────┐
   │ Auth DB │                          │ User DB │
   │ (H2)    │                          │ (H2)    │
   └─────────┘                          └─────────┘
```

## 🔧 Setup Instructions

### 1. Start Security-Common Module
```bash
cd security-common
mvn clean install
```

### 2. Start Auth-Service (Port 8080)
```bash
cd auth-service
mvn spring-boot:run
```

### 3. Start User-Service (Port 8081)
```bash
cd user-service
mvn spring-boot:run
```

## 🧪 Integration Test Scenarios

### Scenario 1: User Registration & Profile Creation

#### Step 1: Register a new user in Auth-Service
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "phone": "+1234567890"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "email": "john.doe@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

#### Step 2: Verify user profile was created in User-Service
```bash
# Extract the token from registration response
TOKEN="your_jwt_token_here"

curl -X GET http://localhost:8081/api/users \
  -H "Authorization: Bearer $TOKEN"
```

### Scenario 2: Authentication & Protected Access

#### Step 1: Login to Auth-Service
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

#### Step 2: Access protected User-Service endpoints
```bash
TOKEN="your_jwt_token_here"

# Get all users (protected)
curl -X GET http://localhost:8081/api/users \
  -H "Authorization: Bearer $TOKEN"

# Create a new user (protected)
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com", 
    "phone": "+0987654321"
  }'
```

### Scenario 3: Security Validation

#### Attempt to access without a token (should fail)
```bash
curl -X GET http://localhost:8081/api/users
# Expected: 401 Unauthorized
```

#### Attempt with invalid token (should fail)
```bash
curl -X GET http://localhost:8081/api/users \
  -H "Authorization: Bearer invalid_token"
# Expected: 401 Unauthorized  
```

## 🔍 Health Checks

### Auth-Service Health
```bash
curl http://localhost:8080/actuator/health
```

### User-Service Health  
```bash
curl http://localhost:8081/
```

## 📊 API Documentation

### Auth-Service Swagger
- URL: http://localhost:8080/swagger-ui.html

### User-Service Swagger
- URL: http://localhost:8081/swagger-ui.html

## 🔑 Key Integration Features

### ✅ **Security Integration**
- User-Service validates JWT tokens from Auth-Service
- Shared security components via security-common module
- Stateless authentication across services

### ✅ **Service Communication** 
- Auth-Service automatically creates user profiles in User-Service
- Internal API endpoints for service-to-service communication
- Non-blocking HTTP calls to prevent auth failures

### ✅ **Data Synchronization**
- Users registered in Auth-Service appear in User-Service
- Separate databases maintain service independence
- Eventual consistency model

### ✅ **Port Management**
- Auth-Service: 8080 (handles authentication)
- User-Service: 8081 (handles user management)
- No port conflicts

## 🚨 Troubleshooting

### Common Issues
1. **Port conflicts**: Ensure services run on different ports
2. **JWT validation fails**: Check security-common is properly installed
3. **Service communication fails**: Verify both services are running
4. **Database issues**: Check H2 console access

### Log Locations
- Auth-Service: Console output
- User-Service: `logs/user-service.log`

## 🎯 Success Criteria

The integration is successful when:
- ✅ Both services start without errors
- ✅ User registration creates profiles in both services  
- ✅ JWT tokens work across services
- ✅ Protected endpoints require valid authentication
- ✅ Invalid tokens are properly rejected
- ✅ Services can communicate internally

## 🔮 Next Steps

1. **Production Deployment**: Add service discovery and load balancing
2. **Database Migration**: Move from H2 to production databases
3. **API Gateway**: Add centralized routing and rate limiting
4. **Monitoring**: Add metrics and distributed tracing
5. **CI/CD Pipeline**: Automate testing and deployment 
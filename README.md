# JWT Authentication and Task Management System

A RESTful web application built with Java 17 and Spring Boot that implements JWT-based authentication and provides task management functionality.

## Features

### Authentication
- User registration with role-based access control (User, Moderator, Admin)
- Secure login with JWT token generation
- Password encryption

### Task Management
- Create, read, update, and delete tasks
- User-specific task lists
- Task properties: title, description, due date, and status

## API Endpoints

### Authentication Endpoints
- `POST /api/auth/signin`: Authenticate user and generate JWT token
- `POST /api/auth/signup`: Register new user account

### Task Endpoints
- `GET /me`: Get current user profile information
- `POST /task`: Create a new task
- `GET /task`: Retrieve all user tasks
- `PUT /task`: Update an existing task
- `DELETE /task`: Delete a task

## Technologies
- Java 17
- Spring Boot
- Spring Security with JWT
- JPA/Hibernate
- RESTful API architecture
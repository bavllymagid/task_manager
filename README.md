# Task Manager
## Overview
### The Task Manager project is a microservices-based application designed to manage tasks and users efficiently. It consists of two primary microservices:

#### - User Management Microservice: Handles user authentication, authorization, and user data management.
#### - Task Management Microservice: Manages tasks, including creation, retrieval, updating, and deletion.
## Features
#### User Management Service:
- User registration and login
- Token-based authentication
- User profile management 
#### Task Management Service:
- Create, update, retrieve, and delete tasks
- Assign tasks to users
- List tasks with filters
## Technologies
- Backend Framework: Spring Boot
- Database: [Specify the database used, e.g., PostgreSQL, MySQL]
- Authentication: JWT (JSON Web Token)
- Communication: RESTful APIs

## Getting Started
#### Prerequisites
- Java 17 or higher
- Maven
- [Database software and setup instructions]
## Installation
#### Clone the repository:

```bash
git clone https://github.com/bavllymagid/task_manager.git
cd task_manager
```
#### Navigate to each microservice directory and build the project:

```bash
cd user-management
mvn clean install
cd ../task-management
mvn clean install
```
#### Configure the application:

- Create a application.properties file in each microservice's src/main/resources directory.
- Configure database connection and JWT secret key.
#### Example configuration for user-management:

<span>application.properties(user-management)</span>
```properties title = "application.properties(user-management)"
spring.datasource.url=jdbc:mysql://localhost:3306/userdb
spring.datasource.username=root
spring.datasource.password=password
jwt.secret=your_jwt_secret_key
```
#### Example configuration for task-management:
<span>application.properties(task-management)</span>
```properties title = "application.properties(task-management)"
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
spring.datasource.username=root
spring.datasource.password=password
```
#### Run the services:

```bash
cd user-management
mvn spring-boot:run
cd ../task-management
mvn spring-boot:run
```

## Usage
#### User Management API:

- Register User: ``` POST /api/users/register ```
- Login: ``` POST /api/users/login ```
- Get User Profile: ``` GET /api/users/profile ```
#### Task Management API:

- Create Task: ``` POST /api/tasks ```
- Get Tasks: ``` GET /api/tasks ```
- Update Task: ``` PUT /api/tasks/{taskId} ```
- Delete Task: ``` DELETE /api/tasks/{taskId} ```

## Contributing
- Fork the repository
- Create a feature branch:

```bash
git checkout -b feature/your-feature
```
- Commit your changes:

```bash
git commit -am 'Add new feature'
```
- Push to the branch:

``` bash
git push origin feature/your-feature 
```
- Create a new Pull Request

## Contact
For any questions or issues, please contact Me.

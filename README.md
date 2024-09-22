# Task Manager API

## Overview

The **Task Manager API** is a microservice-based solution that helps manage users, authentication, and task assignments. The API consists of two main services:
1. **Authentication Service**: Manages user registration, login, token validation and user data management.
2. **Task Management Service**: Manages tasks, assignments, and notifications.
---
## Features
#### User Management Service:
- User registration and login
- Token-based authentication
- User profile management
#### Task Management Service:
- Create, update, retrieve, and delete tasks
- Assign tasks to users
- List tasks with filters
--- 
## Technologies
- Backend Framework: Spring Boot
- Database: MySQL
- Authentication: JWT (JSON Web Token)
- Communication: RESTful APIs
---
## Getting Started
#### Prerequisites
- Java 17 or higher
- Maven
- MySQL :

    1. **Install MySQL**
        - **For Windows:**
            - Download the MySQL installer from the official MySQL website: https://dev.mysql.com/downloads/installer/
            - Run the installer and follow the on-screen instructions.
            - During the setup, ensure that MySQL Server is selected.
              <br></br>
        - **For Linux:**
            - Update your package index and install MySQL:
          ``` bash
          sudo apt update
          sudo apt install mysql-server
          ```
            - Start MySQL and enable it to run on boot:
          ```bash
          sudo systemctl start mysql
          sudo systemctl enable mysql
          ```

    2. **Log in to MySQL**
       ``` bash
       cd task_manager/script/
       mysql -u root -p 
       ```
        - Once MySQL is installed and running, execute the **script/Task_manager_script.sql** file to set up the necessary database tables.
       ```sql
       source Task_manager_script.sql; 
       ```

##### This will set up the required tables and schema for the Task Manager application.

---
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

- There's an application.properties file in each microservice's src/main/resources directory, configure database connection in it.
#### Example configuration for user-management:

```properties title = "application.properties(user-management)"
spring.datasource.url=jdbc:mysql://localhost:3306/(your_database_name) 
spring.datasource.username=(your_username)
spring.datasource.password=(your_password)
```
---
## API Documentation

### Base URLs
- Authentication Service: `http://localhost:8080`
- Task Management Service: `http://localhost:8081`

### Endpoints

## 1. Authentication Service

### 1.1 User Management

#### Register a User

- **Endpoint**: `POST /api/users/register`
- **Description**: Registers a new user.
- **Request Body**:
    ```json
    {
      "username": "exampleUsername",
      "email": "exampleEmail@example.com",
      "password": "@examplePassword1"
    }
    ```

#### Login a User

- **Endpoint**: `POST /api/users/login`
- **Description**: Logs in a user and returns a JWT token.
- **Request Body**:
    ```json
    {
      "email": "exampleEmail@example.com",
      "password": "@examplePassword1"
    }
    ```

#### Update User

- **Endpoint**: `PUT /api/users/update`
- **Description**: Updates user information.
- **Authentication**: Bearer Token
- **Request Body**:
    ```json
    {
      "username": "exampleUsername3",
      "email": "exampleEmail@example.com",
      "password": "@examplePassword1"
    }
    ```

#### Delete User

- **Endpoint**: `DELETE /api/users/delete`
- **Description**: Deletes a user.
- **Authentication**: Bearer Token
- **Query Parameters**:
    - `email`: User Email

#### Get All Users

- **Endpoint**: `GET /api/users/get_users`
- **Authentication**: Bearer Token
- **Description**: Retrieves all users.
- **Query Parameters**:
    - `page`: Page number (default: 0)
    - `size`: Page size (default: 20)

#### Get User
- **Endpoint**: `GET /api/users/get_user`
- **Authentication**: Bearer Token
- **Description**: Retrieves a user by Email.
- **Query Parameters**:
    - `email`: User Email

#### logout User
- **Endpoint**: `GET /api/users/logout`
- **Authentication**: Bearer Token
- **Description**: Logs out a user.

### 1.2 Token Management

#### Validate Token

- **Endpoint**: `GET /api/token/validate`
- **Authentication**: Bearer Token
- **Description**: Validates a JWT token.

#### Refresh Token

- **Endpoint**: `GET /api/token/refresh`
- **Authentication**: Bearer Token
- **Description**: Refreshes the access token.



## 2. Task Management Service

### 2.1 Task Management
#### Create Task

- **Endpoint**: `POST /api/task/create`
- **Authentication**: Bearer Token
- **Description**: Creates a new task.
- **Request Body**:
```json

{
"title": "Sample Task Title",
"description": "This is a detailed description of the task.",
"status": "In Progress",
"dueDate": "2024-10-15T12:45:00"
} 
```
#### Get User Created Tasks

- **Endpoint**: `GET /api/task/get/user_tasks/{userId}`
- **Authentication**: Bearer Token
- **Description**: Retrieves tasks created by a specific user.
- **Query Parameters**:
    - `page`: Page number (default: 0)
    - `size`: Page size (default: 20)

#### Get Task By ID
- **Endpoint**: `GET /api/task/get/{taskId}`
- **Authentication**: Bearer Token
- **Description**: Retrieves a task by ID.

#### Update Task
- **Endpoint**: `PUT /api/task/update`
- **Authentication**: Bearer Token
- **Description**: Updates task details.
- **Request Body**:
```json
{
  "taskId": 1,
  "title": "New Title",
  "description": "Updated task description.",
  "status": "Completed",
  "dueDate": "2024-11-15T12:45:00"
}
```

#### Delete Task
- **Endpoint**: `DELETE /api/task/delete/{taskId}`
- **Authentication**: Bearer Token
- **Description**: Deletes a task.

### 2.2 Task Assignment

#### Assign Task to User
- **Endpoint**: `POST /api/task/assign/{taskId}`
- **Authentication**: Bearer Token
- **Description**: Assigns a task to a user.
- **Request Body**:
```json
[1, 2, 3]
```

#### Get User Assigned Tasks
- **Endpoint**: `GET /api/task/get/user_assigned_tasks/{userId}`
- **Authentication**: Bearer Token
- **Description**: Retrieves tasks assigned to a specific user.
- **Query Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20)

#### Unassign Task
- **Endpoint**: `DELETE /api/task/unassign/{taskId}`
- **Authentication**: Bearer Token
- **Description**: Unassigns a task from a user.
- **Request Body**:
```json
[1, 2, 3]
```


#### Unassign All Tasks from User
- **Endpoint**: `DELETE /api/task/unassign/all/task/{userId}`
- **Description**: Unassigns all tasks from a user.
- **Authentication**: Bearer Token

### 2.3 Notification Management

#### Get User Notifications
- **Endpoint**: `GET /api/task/notification/send`
- **Authentication**: Bearer Token
- **Description**: Sends notifications to user.
- **Query Parameters**:
  - `size`: Page size (default: 20)
  - `page`: Page number (default: 0)

#### Update Read Status
- **Endpoint**: `PUT /api/task/notification/update_status`
- **Authentication**: Bearer Token
- **Description**: Updates notification read status.
- **Query Parameters**:
  - `notificationId`: Notification ID
--- 

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

---

## Contact
For any questions or issues, please contact Me.

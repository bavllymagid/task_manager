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
- **Response**:
    ```json
    {
      "message": "User registered successfully",
      "user": {
        "id": 1,
        "username": "exampleUsername",
        "email": "exampleEmail@example.com",
        "roles": []
      }
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
- **Response**:
    ```json
    {
      "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
    }
    ```

#### Update User

- **Endpoint**: `PUT /api/users/update`
- **Description**: Updates user information.
- **Request Body**:
    ```json
    {
      "username": "exampleUsername3",
      "email": "exampleEmail@example.com",
      "password": "@examplePassword1"
    }
    ```
- **Response**:
    ```json
    {
      "message": "User updated successfully"
    }
    ```

#### Delete User

- **Endpoint**: `DELETE /api/users/delete`
- **Description**: Deletes a user.
- **Request**:
    - **Query Parameter**: `email=exampleEmail@example.com`
- **Response**:
    ```json
    {
      "message": "User deleted successfully"
    }
    ```

#### Get All Users

- **Endpoint**: `GET /api/users/get_users`
- **Description**: Retrieves all users.
- **Response**:
    ```json
    [
      {
        "id": 1,
        "username": "exampleUsername",
        "email": "exampleEmail@example.com",
        "roles": []
      }
    ]
    ```

### 1.2 Token Management

#### Validate Token

- **Endpoint**: `GET /api/token/validate`
- **Description**: Validates a JWT token.
- **Response**:
    ```json
    {
      "valid": true
    }
    ```

#### Refresh Token

- **Endpoint**: `GET /api/token/refresh`
- **Description**: Refreshes the access token.
- **Response**:
    ```json
    {
      "token": "newGeneratedToken"
    }
    ```



## 2. Task Management Service

#### TODO

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

# User REST API

This project is a REST API that allows user management, including functionalities for registration, login, and access to user information.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Project Architecture Description](#project-architecture-description)
- [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Tests](#tests)
- [Contributions](#contributions)
- [License](#license)

## Features

- Registration of new users.
- Login with authentication.
- Retrieval of user information.
- Security implemented with Spring Security.
- API documentation generated with Swagger.

## Technologies

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Security**
- **Spring Data JPA**
- **H2 Database**
- **Swagger (Springdoc OpenAPI)**
- **JUnit and Mockito for testing**

## Project Architecture Description

The architecture of the User Management API is based on a modular and scalable approach, using best software development practices. Below are the main components and their interaction:

## Flowchart: User Registration Process

1. **Start**
2. **Receive Registration Request**
   - The controller receives user data.
3. **Validate Data**
   - Checks that all required fields are complete and in the correct format.
   - If the data is invalid, an error message is returned.
4. **Check Email Existence**
   - Checks if the email is already registered.
   - If the email already exists, an error message is returned.
5. **Register User**
   - Saves the new user in the database.
6. **Return Success Response**
   - Sends a response with the registration status.
7. **End**

## Data Diagram: Structure of Users and Phones

### Entities

- **users**
   - `id`: UUID (Unique identifier for the user)
   - `name`: String (User's name)
   - `email`: String (Email address)
   - `password`: String (Encrypted password)
   - `isActive`: Boolean (User's status)
   - `created`: Date (Creation date)
   - `modified`: Date (Modification date)

- **phone**
   - `id`: UUID (Unique identifier for the phone)
   - `number`: String (Phone number)
   - `type`: String (Type of phone, e.g., personal, work)
   - `userId`: UUID (Reference to the user to whom the phone belongs)

### Relationships

- **A User can have multiple Phones.**
   - One-to-many relationship between User and Phone.

### 1. **General Architecture**

The API follows an **MVC (Model-View-Controller)** design pattern, where:

- **Model:** Represents the data structure and business logic. Uses JPA entities to interact with the database.
- **View:** Not used in this project as it is a REST API, but Swagger documentation can be considered as a user interface to interact with the API.
- **Controller:** Manages HTTP requests and defines the API endpoints.

### 2. **Key Components**

- **REST Controllers:** Responsible for receiving requests and returning responses. Each controller corresponds to a resource (users, phones, etc.) and uses services to handle business logic.

- **Services:** Contain the business logic of the application. Services interact with repositories to perform CRUD (Create, Read, Update, Delete) operations on the data.

- **Repositories:** Use Spring Data JPA to interact with the database. Provide methods to perform operations on entities.

- **Security:** Implements authentication and authorization using Spring Security. The API uses JWT (JSON Web Tokens) to manage user sessions.

### 3. **Data Flow**

1. **Client Request:** The client sends an HTTP request to one of the API endpoints.
2. **Controller:** The controller receives the request and validates it. If the data is valid, it calls the corresponding service.
3. **Service:** The service executes the business logic and may interact with one or more repositories to access the data.
4. **Repository:** Performs the necessary operations in the database.
5. **Response:** The service returns the results to the controller, which formats the response and sends it back to the client.

### 4. **Database**

**H2 Database** is used as an in-memory database for development and testing. In production, it can be configured to use other databases like PostgreSQL or MySQL.

### 5. **Documentation and Swagger**

The API is documented using **Swagger**, allowing developers and users to explore the endpoints, required parameters, and expected responses interactively.

### 6. **Tests**

Unit and integration tests are implemented using **JUnit** and **Mockito** to ensure that each component works as expected.

### 7. **Deployment**

The application can be packaged as a JAR file and deployed on any Java-compatible server. It can also be containerized using Docker to facilitate deployment in production environments.

## Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/ISC-Ignacio-Naranjo/restApiUser.git
   cd restApiUser

2. **Make sure you have Maven installed.**

3. **Open the project in your favorite IDE (e.g., IntelliJ IDEA or Eclipse).**

## Usage
1. **Build the project:**
   Run the following command in the root of the project:
   ```bash
   mvn clean install 

2. **Run the application:**
   ```bash
   mvn spring-boot:run
 API will be available on http://localhost:8080.


## Endpoints
1. **Register a User:**
   - URL: /user/users
   - Method: POST

 - Request:
   ```json
   {
   "name": "Juan Perez",
   "email": "juan.perez@ejemplo.com",
   "password": "contraseñaSegura123"
   }
   
 - Response:
   ```json
   { "id": "123e4567-e89b-12d3-a456-426614174000",
   "name": "Juan Perez",
   "email": "juan.perez@ejemplo.com",
   "isActive": true,
   "created": "2024-10-02T12:00:00Z",
   "modified": "2024-10-02T12:00:00Z"
   }




1. **Authenticate a User:**

   - Request:
   ```json
   
   {
   "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Juan Perez",
    "email": "juan.perez@ejemplo.com",
    "isActive": true,
    "created": "2024-10-02T12:00:00Z",
    "modified": "2024-10-02T12:00:00Z" 
   }
****

   - Response:
      ```json 
     { 
          "token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     }
   
   
3.**getAllUsers:**
- URL: /user/users
- Method: GET

****
- Request: 
  ```json 
   {
  
   }
****
  - Response: 
    ```json 
     [
    {
        "id": "c1c1c1c1-1a1a-1a1a-1a1a-c1c1c1c1c1c1",
        "name": "Alice Johnson",
        "created": "2024-01-01T16:00:00.000+00:00",
        "modified": "2024-01-01T18:00:00.000+00:00",
        "lastLogin": "2024-01-01T18:00:00.000+00:00",
        "token": "token_1",
        "active": true
    },
    {
        "id": "d2d2d2d2-2b2b-2b2b-2b2b-d2d2d2d2d2d2",
        "name": "Bob Smith",
        "created": "2024-01-02T16:00:00.000+00:00",
        "modified": "2024-01-02T21:30:00.000+00:00",
        "lastLogin": "2024-01-02T21:30:00.000+00:00",
        "token": "token_2",
        "active": true
    } 
]
****

## Swagger
For more details on the endpoints and to use the default values, visit the Swagger documentation at the following link: http://localhost:8080/swagger-ui/index.html

## Tests
To run the unit tests, use the following command: `mvn test`

## Contributions
Contributions are welcome. If you would like to contribute, please follow these steps:

1. Fork the repository https://github.com/ISC-Ignacio-Naranjo/restApiUser.git.
2. Create a new branch (git checkout -b feature/new-functionality).
3. Make your changes and commit (git commit -m 'Add new functionality').
4. Push your changes (git push origin feature/new-functionality).
5. Create a new Pull Request.


## Licence
- [By ISC. Jose Ignacio Naranjo  Guerra](https://www.linkedin.com/in/ignacio-naranjo-guerra-40706a144/)
- [GITHUB](https://github.com/ISC-Ignacio-Naranjo)





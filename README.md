# Spring Boot Online Book Store API

## Introduction
Welcome to the **Online Book Store API**! 

This project is a fully functional RESTful web service designed to manage an online bookstore. It provides a seamless backend infrastructure for browsing books, managing categories, handling a digital shopping cart, and securely processing user orders.

The main goal of this project was to build a robust, scalable, and secure enterprise-level application using the Spring ecosystem while adhering to best practices like SOLID principles, layered architecture, and comprehensive testing.

## Technologies and Tools
*   **Java 17**
*   **Spring Boot 3.2.0** (Web, Security, Data JPA)
*   **Database:** MySQL (Production) & H2 (In-memory for testing)
*   **Migration:** Liquibase
*   **Authentication:** Spring Security with JWT (JSON Web Tokens)
*   **Mapping:** MapStruct & Lombok
*   **Testing:** JUnit 5, Mockito, Testcontainers
*   **Documentation:** Swagger / OpenAPI
*   **Deployment:** Docker, AWS (EC2, RDS, ECR)

## Features & Controllers
The application is structured into several controllers to handle specific business logic:
*   **AuthenticationController:** Manages user registration and login endpoints. Issues JWT tokens for secure access.
*   **BookController:** CRUD operations for books. Includes pagination, sorting, and dynamic filtering.
*   **CategoryController:** Endpoints to manage book categories and fetch books by specific categories.
*   **ShoppingCartController:** Allows users to add books to their cart, update quantities, and clear the cart.
*   **OrderController:** Processes the shopping cart into a final order, updates order statuses, and retrieves order history.

## How to Run the Project

### Prerequisites
*   Java 17 installed
*   Maven installed
*   MySQL Server / Docker installed

### Installation steps
1. Clone the repository:
   ```
   git clone https://github.com/KSORIGY/spring-book-store.git
    ```
   
### THAN
1. Navigate to the project directory
2. Configure your database settings. In src/main/resources/application.properties, set your MySQL credentials:
    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/book_store
    spring.datasource.username=your_username
    spring.datasource.password=your_password
   ```
3. Build and run the application:
    ```
    mvn clean install
    mvn spring-boot:run
   ```
4. Access the API documentation via Swagger UI:
    ```
   http://localhost:8080/api/swagger-ui/index.html
   ```

## Challenges and Solutions
During the development of this project, I faced several technical challenges that helped me improve my architectural and debugging skills:

*   **Dynamic Search Filtering (Criteria API vs. Specifications):**
    *   *Challenge:* I needed to implement a dynamic, multi-parameter search for books (e.g., by authors, price range, categories) without cluttering the repository layer with dozens of specific `@Query` methods or writing complex, unmaintainable string-concatenated JPQL queries.
    *   *Solution:* I implemented **Spring Data JPA Specifications** under the hood of the JPA Criteria API. By creating a `SpecificationBuilder` and a `SpecificationProviderManager` pattern, I was dynamically constructing type-safe database queries based on incoming parameters. This made the search endpoint highly scalable and easy to extend with new filters.

*   **Testcontainers Configuration on Windows (WSL2):**
    *   *Challenge:* The standard Docker named-pipe socket configuration was throwing `BadRequestException (Status 400)` during integration tests due to Docker Desktop API version conflicts on Windows.
    *   *Solution:* Instead of fighting with environment variables and socket mappings, I refactored the integration tests to use the **Testcontainers JDBC URL approach** (`jdbc:tc:mysql:8.0.33:///book_shop_test`). This seamlessly abstracted the Docker lifecycle management away from the OS-level socket issues, resulting in perfectly stable tests.

## Postman Collection
If you want to test the endpoints locally, you can use the exported Postman collection provided in this repository:
[Download Postman Collection](./postman_collection.json)
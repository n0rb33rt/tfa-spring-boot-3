# Spring Boot Security with JWT Implementation & Two-Factor Authentication

![Java Version](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot-3.1.2-brightgreen)

This project demonstrates the implementation of security using Spring Boot 3.1.2 and JSON Web Tokens (JWT) with Two-Factor Authentication. It includes the following features:

## Features

- User registration and login with JWT authentication
- Possibility to enable 2FA in order to ensure a powerful protection
- Password encryption using BCrypt
- Customized exception handling
- Logout mechanism

## Technologies Used

- Spring Boot 3.1.2
- Spring Security
- JSON Web Tokens (JWT)
- Two-Factor Authentication
- BCrypt
- Maven

## Getting Started

To get started with this project, make sure you have the following installed on your local machine:

- JDK 17+
- Maven 3+

To build and run the project, follow these steps:

- Clone the repository: git clone https://github.com/n0rb33rt/tfa-spring-boot-3.git
- Navigate to the project directory: cd tfa-spring-boot-3
- Create a database by using docker: docker-compose up -d
- Build the project: mvn clean install
- Run the project: mvn spring-boot:run

  
-> The application will be available at http://localhost:8080.


**Author**: Norbert

**Email**: n0rb3rt.work@gmail.com

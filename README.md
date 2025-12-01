# Mini-SurveyMonkey

## Overview
This project is a  web application that allows users to create and manage surveys through a simple interface.   

The goal of Mini-SurveyMonkey is to simulate a real-world survey platform where users can register, design surveys, and collect responses for analysis.  
The system follows an MVC structure with both web-based and REST-based functionalities.

***

# Milestone #1
The focus of Milestone 1 was on building the core structure of the application and implementing user management.

### Key Features
- **Spring Boot Setup** – configured the base application with automatic component scanning and dependency injection.  
- **User Management** – allows adding, viewing, and listing users through Thymeleaf pages and REST endpoints.  
- **Entity Layer** – created user and survey entities with auto-generated IDs and basic attributes.  
- **Front-End Templates** – developed simple interfaces for user creation and display.  
- **Testing and Deployment** – verified application startup and functionality; runs locally on `http://localhost:8080/users`.

***
### UML Diagram - Model
<img width="448" height="361" alt="image" src="https://github.com/user-attachments/assets/d7f16ae8-7d9c-4fe2-a27e-c05cccb2da7e" />

### UML Diagram - Controller
<img width="1120" height="513" alt="image" src="https://github.com/user-attachments/assets/8123381d-d703-4654-a0df-7dc80b08e073" />


# Milestone #2
This milestone will introduce survey creation and response management.

### Planned Additions
- **Survey Creation** – users can build surveys with different question types (range, text, multiple choice).  
- **Response Collection** – respondents can fill and submit surveys online.  
- **Improved UI** – add pages for survey creation, submission, and result viewing.  
- **REST API Expansion** – provide endpoints for managing surveys and responses.

***
### UML Diagram - Model
<img width="1224" height="756" alt="image" src="https://github.com/user-attachments/assets/8cb41d07-56f1-4f58-964c-f45d96e8b6e9" />

***
### ER Diagram - Model
<img width="1377" height="542" alt="image" src="https://github.com/user-attachments/assets/18ce955f-1b8c-4e76-8a58-850f93db2745" />

***

# Milestone #3 
The final milestone will focus on user authentication, analytics, and deployment improvements.

### Planned Additions
- **Authentication System** – enable login, logout, and role-based access.  
- **Admin Dashboard** – manage users and monitor surveys.  
- **Analytics Module** – display statistics and response summaries.  
- **Cloud Deployment** – host the system on Azure with persistent storage.  

***

***
### ER Diagram - Model
<img width="1073" height="763" alt="image" src="https://github.com/user-attachments/assets/ad5b47bf-3058-4c01-bbf5-7517be4382bf" />

## Setup Instructions

### 1. Prerequisites
- **Java JDK 17+**
- **Maven 3.6+**
- **IntelliJ IDEA** (or any preferred IDE)

### 2. Clone the Repository
```bash
1. git clone https://github.com/aniqueali17/SpringMVC_Project.git
2. cd SpringMVC_Project
3. Run the main file
4. Open the application in your browser - go to: http://localhost:8080/users



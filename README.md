
# **Scrum Tracker**


## **Overview**

Scrum Tracker is a RESTful application built with Spring Boot for agile development using the Scrum framework. It helps manage users, teams, projects, sprints, and issues while offering project and sprint health metrics.
## **Features**

- Manage users, teams, projects, sprints, and issues.
- Track sprint and project health metrics.
- Role-based access control with JWT authentication and authorization.
- Email notifications for project health via Gmail SMTP.
- Centralized exception handling for consistent error responses.
- Logging with SLF4J and Logback.
- API documentation with Swagger.


## **Technologies Used**

- **Backend**: Spring Boot, Spring Security, JPA, Hibernate 
- **Database**: MySQL
- **Authentication**: JWT (JSON Web Tokens)
- **Logging**: SLF4J with Logback
- **API Documentation**: Swagger  
## **API Testing**

This repository includes API collections for easy testing:  
- **Postman Collection**: Contains pre-configured requests for all endpoints.  
- **Bruno JSON**: Alternative API collection for Bruno users (since Postman sucks now). 
## **Setup**

1. Clone the repository:
```
git clone https://github.com/yourusername/scrum-tracker.git  
cd scrum-tracker
```

2.  Configure the database:
- Update `application.properties` with your MySQL credentials.
```
spring.datasource.url=jdbc:mysql://localhost:3306/scrum_tracker  
spring.datasource.username=your_username  
spring.datasource.password=your_password 
```

3. Configure email service credentials
```
spring.mail.username=your_email
spring.mail.password=your_password
```

4. The repository includes a data.sql file for seeding initial data for testing purposes.

5. The **Password Migration Runner** automatically converts string passwords in the `data.sql` file into bcrypt hashed passwords upon application startup.

6. Run the application:
```
mvn spring-boot:run  
```
7. Access the Swagger API documentation:
http://localhost:8080/swagger-ui.html


## **Future Enhancements**

While the current version of the Scrum Tracker covers the essential features, there are a few areas planned for improvement:

- **Project and Sprint Health**: Expanding the current health tracking metrics to include more comprehensive insights.
- **Spring Boot Tests**: Implementing **unit tests** to ensure the robustness and reliability of the application.
- **Role Management**: Enhancing the role-based access control by making **user roles more fine-grained** (e.g., adding specific permissions for managers, project leads, etc.).
- **Scheduled Email Service**: Implementing a **scheduled service** to regularly send **health reports** about project and sprint status to managers and project leads, keeping them updated without manual intervention.
- **Frontend Integration**: While the application is currently backend-focused, eventually adding a **basic frontend** could provide a more user-friendly interface.

These enhancements will be implemented as time allows, helping to improve the user experience, testing coverage, and overall system functionality.

## **Author**

This project is developed and maintained by [Aashu Khare](https://github.com/aashu0036)

## How to Start the Application

### Prerequisites
Ensure you have the following installed:
- **Java 21**
- **Node.js 16+**
- **Maven 3.8+**

### Build the UI
ng build --output-path ../src/main/resources/static

### 1. Start the Backend
1. Navigate to the project root directory.
2. Run the backend using Maven:
   ```bash
   ./mvnw spring-boot:run


4. Access the H2 Database Console
Open the H2 console in your browser:
1 vulnerability
Use the following credentials:
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave blank)


###  Add task to the database

INSERT INTO TASK (ID, DESCRIPTION, DUE_DATE, COMPLETED, USER_ID)
VALUES (1, 'Test', 'Desc', '2026-02-20', false, 1);


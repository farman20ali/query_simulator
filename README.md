# query_simulator
backened development for react native app

# Quarkus Simulator

## Overview
This project is a Quarkus-based Java application that simulates a data-fetching service. It operates by handling queries provided in text format, fetching relevant data without requiring variable creation. The project utilizes RESTful endpoints to handle input and provide structured outputs.

---

## Features
- Fetch data dynamically using query strings.
- Supports POST requests for sending data.
- Simplified handling of file-based parameters.
- Example endpoint:
  - `http://localhost:8002/irs/lov/ambulance_services`

---

## Sample POST Request
```json
{
  "fileName": "mobile",
  "params": "03003"
}
```
- Endpoint: `{{ _.irs_ip }}:{{ _.irs_port }}/irs/user`
- Query Example:
  ```sql
  SELECT * FROM vehicle_involved;
  SELECT * FROM users WHERE mobile_number='%s';
  ```

---

## Project Dependencies
### Core Framework
- **Quarkus**

### Database
- PostgreSQL

### Key Dependencies (from `pom.xml`):
- Quarkus RESTEasy
- Quarkus RESTEasy Jackson
- Quarkus Agroal
- Quarkus JDBC PostgreSQL
- Quarkus Hibernate Validator
- Quarkus SmallRye OpenAPI
- Quarkus SmallRye Metrics
- Quarkus SmallRye Health
- Quarkus Narayana JTA
- BCrypt for encryption (via `jbcrypt`)
- Kubernetes support
- Jib for container image creation

---

## Getting Started
### Prerequisites
- Java 11 or later
- Maven
- PostgreSQL database

### Running the Application
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd query_simulator
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the Quarkus application:
   ```bash
   mvn quarkus:dev
   ```

### Configuration
Set the following environment variables in the application:
- `_.irs_ip` - IP address for the server.
- `_.irs_port` - Port for the server.

---

## Example Endpoints
### Fetching File Data
- **URL:** `http://localhost:8002/irs/lov/{fileName}`
- **Method:** GET
- **Description:** Fetches data based on the provided file name.

### Query Execution
- **URL:** `{{ _.irs_ip }}:{{ _.irs_port }}/irs/user`
- **Method:** POST
- **Payload Example:**
  ```json
  {
    "fileName": "mobile",
    "params": "03003"
  }
  ```

---

## Tags
- `Quarkus`
- `Simulator`
- `PostgreSQL`
- `Java`
- `REST API`
- `Data Fetching`

---

## Contributing
1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit changes:
   ```bash
   git commit -m "Description of changes"
   ```
4. Push to the branch:
   ```bash
   git push origin feature-name
   ```
5. Create a pull request.

---

## License
This project is licensed under the [MIT License](LICENSE).


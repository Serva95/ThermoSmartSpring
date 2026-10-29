# ThermoSmartSpring

**ThermoSmartSpring** is the core control application for a distributed home heating system. It manages the business logic, routine scheduling, and system state persistence.

The application is designed to run in a controlled environment (Raspberry Pi) and is orchestrated by the **ThermoWrapperK** component, which manages its lifecycle, automatic updates, and process monitoring.

## System Architecture

The system is based on a centralized control model:

*   **Central Controller (Raspberry Pi):** Runs the **ThermoSmartSpring** application. It manages heating decisions, hourly scheduling, and the control interface.
*   **End Nodes (ESP32):**
    *   **Sensing:** DS18B20 digital temperature sensors.
    *   **Actuation:** Relay modules for thermostatic valve control.
*   **Orchestrator (ThermoWrapperK):** A separate service that monitors versions, downloads new artifacts, and launches `ThermoSmartSpring` as a supervised child process.

## Tech Stack

*   **Framework:** Spring Boot 4.1.2
*   **Language:** Mixed Kotlin 2.1.0 and Java (targeting JVM 17)
*   **Database:** MariaDB (default) / PostgreSQL (production) / H2 in-memory (tests)
*   **Security:** Spring Security with SSL/TLS (PKCS12) support
*   **Build Tool:** Gradle

## System Requirements

*   **Runtime:** OpenJDK 17 (required for compatibility with the Raspberry Pi deployment driver).
*   **Database:** Accessible PostgreSQL instance.
*   **Environment:** Managed via the **ThermoWrapperK** orchestrator.

## Installation and Configuration

### 1. Build

To generate the executable JAR artifact:

```bash
./gradlew clean build
```

The artifact will be available in `build/libs/`.

### 2. Configuration

The application uses `src/main/resources/application.properties`. **Never commit real credentials to the repository.**

Create a local file based on the following template:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://<db-host>:5432/<db-name>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=none

# Server & Security
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore/thermo.p12
server.ssl.key-store-password=<keystore-password>
server.ssl.key-store-type=PKCS12

# Session Management
server.servlet.session.timeout=60m
```

### 3. Deploy

Deployment is managed entirely by **ThermoWrapperK**. Follow the instructions in the Wrapper repository for service installation and updates.

## Development

### Running Tests

Ensure that business logic and integration points are verified before deployment:

```bash
./gradlew test
```

### Local Development

If working locally, ensure you have a database instance (MariaDB or PostgreSQL) configured according to the properties file.

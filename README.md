# ThermoSmartSpring

Core control application for a distributed smart heating system. The system manages temperature regulation through a network of sensors and actuators connected via ESP32 microcontrollers.

## System Architecture

The system follows a centralized control pattern:

*   **Central Controller (Raspberry Pi):** Runs the **ThermoSmartSpring** Spring Boot application. It handles business logic, scheduling, routine management, and state persistence.
*   **End Nodes (ESP32):**
    *   **Sensing:** DS18B20 digital temperature sensors.
    *   **Actuation:** Relays controlling heating valves based on commands from the controller.
*   **Communication:** [Note: Specify protocol here, e.g., MQTT or HTTP]

## Tech Stack

*   **Framework:** Spring Boot 4.1.2
*   **Language:** Java 17
*   **Database:** PostgreSQL (Production) / MariaDB (Test)
*   **Security:** Spring Security with SSL/TLS (PKCS12)
*   **Build Tool:** Gradle

## Prerequisites

*   **Runtime:** OpenJDK 17 (required for Raspberry Pi deployment).
*   **Database:** PostgreSQL instance.
*   **OS:** Linux (specifically configured for `systemd` service management).

## Installation & Deployment

### 1. Build
Compile the application and generate the executable JAR:
```bash
./gradlew clean build
```
The resulting artifact will be located in `build/libs/`.

### 2. Configuration
The application requires an `application.properties` file. **Do not commit real credentials to version control.**

Create a local `src/main/resources/application.properties` based on the following template:

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
spring.mvc.hiddenmethod.filter.enabled=true
```

### 3. Deployment via systemd
To run the application as a background service on the Raspberry Pi, create a service unit file (e.g., `/etc/systemd/system/thermo-smart.service`):

```ini
[Unit]
Description=ThermoSmartSpring Control Service
After=network.target postgresql.service

[Service]
User=<service-user>
WorkingDirectory=/opt/thermosmart
ExecStart=/usr/bin/java -jar /opt/thermosmart/thermosmart-app.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Enable and start the service:
```bash
sudo systemctl daemon-reload
sudo systemctl enable thermo-smart
sudo systemctl start thermo-smart
```

## Development

### Running Tests
Ensure all business logic and integration points are verified before deployment:
```bash
./gradlew test
```

### Local Development
If running locally, ensure you have a local database instance (MariaDB or PostgreSQL) configured as per the properties file.

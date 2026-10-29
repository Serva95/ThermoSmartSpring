# ThermoSmartSpring

**ThermoSmartSpring** è l'applicazione core di controllo per un sistema di riscaldamento domestico distribuito. Gestisce la logica di business, la programmazione delle routine e la persistenza dello stato del sistema.

L'applicazione è progettata per essere eseguita in un ambiente controllato (Raspberry Pi) ed è orchestrata dal componente **ThermoWrapperK**, che ne gestisce il ciclo di vita, l'aggiornamento automatico e il monitoraggio del processo.

## Architettura del Sistema

Il sistema si basa su un modello di controllo centralizzato:

*   **Central Controller (Raspberry Pi):** Esegue l'applicazione **ThermoSmartSpring**. Gestisce le decisioni di riscaldamento, le programmazioni orarie e l'interfaccia di controllo.
*   **End Nodes (ESP32):**
    *   **Sensing:** Sensori di temperatura digitali DS18B20.
    *   **Actuation:** Moduli relè per il controllo delle valvole termostatiche.
*   **Orchestratore (ThermoWrapperK):** Un servizio separato che monitora le versioni, scarica i nuovi artefatti e lancia `ThermoSmartSpring` come processo figlio supervisionato.

## Stack Tecnologico

*   **Framework:** Spring Boot 4.1.2
*   **Linguaggio:** Java 17
*   **Database:** PostgreSQL (Produzione) / MariaDB (Test)
*   **Sicurezza:** Spring Security con supporto SSL/TLS (PKCS12)
*   **Build Tool:** Gradle

## Requisiti di Sistema

*   **Runtime:** OpenJDK 17 (necessario per la compatibilità con il driver di deploy sul Raspberry Pi).
*   **Database:** Istanza PostgreSQL accessibile.
*   **Ambiente:** Gestito tramite l'orchestratore **ThermoWrapperK**.

## Installazione e Configurazione

### 1. Costruzione (Build)
Per generare l'artefatto JAR eseguibile:
```bash
./gradlew clean build
```
L'artefatto sarà disponibile in `build/libs/`.

### 2. Configurazione
L'applicazione utilizza `src/main/resources/application.properties`. **Non committare mai credenziali reali nel repository.**

Crea un file locale basato sul seguente template:

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

### 3. Deploy
Il deploy è gestito interamente da **ThermoWrapperK**. Segui le istruzioni nel repository del Wrapper per l'installazione e l'aggiornamento del servizio.

## Sviluppo

### Esecuzione Test
Assicurati che la logica di business e i punti di integrazione siano verificati prima del deploy:
```bash
./gradlew test
```

### Sviluppo Locale
Se lavori localmente, assicurati di avere un'istanza del database (MariaDB o PostgreSQL) configurata secondo il file delle proprietà.

# Plan Item: Upgrade Java and Gradle

**Owning ADR(s):** adr/0001-migrate-to-spring-boot-4-1-2.md
**Scope:** Update the project to use JDK 17 and ensure Gradle wrapper is compatible with Spring Boot 4.1.2.
**Exit Criteria:**
1. `gradle/wrapper` and `gradlew` are updated.
2. `build.gradle` `sourceCompatibility` is set to `17`.
3. `gradlew clean --help` runs successfully.

**Dependencies:** None.

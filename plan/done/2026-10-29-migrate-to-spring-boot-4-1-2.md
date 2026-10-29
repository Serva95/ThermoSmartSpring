# Plan Item: Migrate to Spring Boot 4.1.2

**Owning ADR(s):** adr/0001-migrate-to-spring-boot-4-1-2.md
**Scope:** Upgrade the project from Spring Boot 2.7.18 to 4.1.2.
**Exit Criteria:**
1. `build.gradle` updated to Spring Boot 4.1.2.
2. Java version upgraded to a compatible modern version (e.g., Java 21).
3. Project compiles and `./gradlew build` passes.
4. All existing tests pass.
5. All `javax.*` imports are migrated to `jakarta.*` where required.
6. Dependencies (PostgreSQL, BouncyCastle, etc.) are updated and verified.

**Dependencies:** None.

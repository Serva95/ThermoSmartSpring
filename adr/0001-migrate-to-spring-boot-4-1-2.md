---
adr: 0001
title: Migrate to Spring Boot 4.1.2
status: Implemented
date: 2026-10-29
owner: agent
supersedes:
superseded-by:
depends-on: []
tags: [infrastructure, upgrade, spring]
---

# ADR 0001 — Migrate to Spring Boot 4.1.2

## Context

The current project is running on **Spring Boot 2.7.18**. In the current ecosystem (late 2026), this version is considered legacy and is nearing or has already passed its End of Life (EOL) for open-source support. 

Staying on a 2.x version branch presents several risks:
1. **Security Vulnerabilities:** Lack of patches for underlying dependencies.
2. **Technical Debt:** Inability to use modern Java features (Project Loom, etc.) and latest Spring ecosystem enhancements.
3. **Maintainability:** Difficulty in finding documentation and community support for obsolete versions.

## Capability statement

Upgrade the core framework of the `thermoSmartSpring` application from Spring Boot 2.7.18 to **Spring Boot 4.1.2**. This will ensure the project remains secure, maintainable, and leverages the latest capabilities for cloud-native and IoT-focused applications.

## User stories / scenarios

- As a developer, I want to use the latest Spring Boot features so that I can write more efficient and modern code.
- As a system administrator, I want the application to run on a supported, secure framework to minimize the attack surface on the Raspberry Pi.

## Acceptance criteria

1. **Framework Upgrade:** `build.gradle` effectively uses Spring Boot 4.1.2 and compatible Spring Framework versions.
2. **Java Upgrade:** The project is successfully migrated to JDK 17, ensuring compatibility with the production Raspberry Pi environment and Spring Boot 4.1.2 requirements.
3. **Successful Build:** The project compiles without errors using `./gradlew build`.
4. **Test Integrity:** All existing unit and integration tests pass in the new environment.
5. **Dependency Resolution:** All critical dependencies (PostgreSQL, BouncyCastle, Lombok, Thymeleaf, Spring Security) are updated to versions compatible with Spring Boot 4.
6. **Namespace Compliance:** All `javax.*` imports are correctly migrated to `jakarta.*` (if applicable for the upgraded version).

## Out of scope

- Migration of the separate backend application (this is a separate repository/project).
- Changes to the business logic or heating control algorithms.
- Refactoring of the existing UI layout in Thymeleaf.

## Open questions

- **Hardware Compatibility:** Does the newer JDK and Spring Boot version run optimally on the specific Raspberry Pi hardware/OS version used in production?
- **Driver Compatibility:** Are the current PostgreSQL drivers compatible with the new Spring Data JPA version included in SB 4?

## References / cross-links

- [Spring Boot 4.1 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.1-Release-Notes)
- [Spring Boot 4.1.2 Release Announcement](https://spring.io/blog/2026/10/23/spring-boot-4-1-2-available-now)

## Revision History

| Date | Revision | Author | Change |
|------|----------|--------|--------|
| 2026-10-29 | r1 | agent | Initial draft. |
| 2026-10-29 | r2 | agent | Corrected Java version requirement to align with Raspberry Pi (JDK 17) constraints. |
| 2026-10-29 | r3 | agent | Implemented: Upgraded to Spring Boot 4.1.2 and JDK 17. |

## Approvals

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Agent | agent | 2026-10-29 | [Verified] |

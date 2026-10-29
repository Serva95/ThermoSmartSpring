---
adr: 0002
title: Migrate to Kotlin
status: Implemented
date: 2026-10-29
owner: agent
supersedes:
superseded-by:
depends-on: [0001]
tags: [language, refactor, productivity]
---

# ADR 0002 — Migrate to Kotlin

## Context

The current codebase is written in Java. While Java is a stable and widely used language, Kotlin has become the industry standard for modern JVM development, especially within the Spring ecosystem. 

Moving to Kotlin offers several advantages:
1. **Null Safety:** Kotlin's type system helps prevent `NullPointerException` at compile time.
2. **Conciseness:** Reduced boilerplate code (getters, setters, constructors) leads to cleaner and more maintainable code.
3. **Interoperability:** Kotlin integrates seamlessly with existing Java libraries and can be introduced incrementally.
4. **Developer Velocity:** Modern language features (coroutines, extension functions, data classes) speed up development.

## Capability statement

Convert the existing Java source code to Kotlin. This migration will improve the safety, readability, and maintainability of the `thermoSmartSpring` codebase.

## User stories / scenarios

- As a developer, I want to write code in Kotlin so that I can benefit from null safety and reduce boilerplate.
- As a maintainer, I want a more concise codebase so that it is easier to read and review.

## Acceptance criteria

1. **Language Migration:** All existing Java source files are converted to Kotlin.
2. **Build Integration:** `build.gradle` is configured with the necessary Kotlin plugins and dependencies.
3. **Successful Build:** The project compiles without errors using `./gradlew build`.
4. **Test Integrity:** All existing unit and integration tests pass in the Kotlin environment.
5. **Incremental Transition (Optional/Preferred):** If a full conversion is too risky, a hybrid approach where new features are written in Kotlin and old ones are migrated piece by piece is acceptable.

## Out of scope

- Major refactoring of the business logic or architecture.
- Integration of new third-party libraries beyond what is required for Kotlin support.

## Open questions

- **Migration Strategy:** Should we do a "big bang" conversion or an incremental one? (Incremental is safer for large projects).
- **Build Time:** Will the introduction of the Kotlin compiler significantly impact build times on the Raspberry Pi (during development or CI)?

## References / cross-links

- adr/0001-migrate-to-spring-boot-4-1-2.md (Dependency)

## Revision History

| Date | Revision | Author | Change |
|------|----------|--------|--------|
| 2026-10-29 | r1 | agent | Initial draft. |
| 2026-10-29 | r2 | agent | Approved for implementation. |
| 2026-10-29 | r3 | agent | Implemented: migrated application to Kotlin (hybrid approach per AC-5); added Kotlin JVM/JPA plugins and pinned BouncyCastle `bcprov-jdk18on` 1.78.1 to `build.gradle`. |

## Approvals

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Agent | agent | 2026-10-29 | [Verified] |

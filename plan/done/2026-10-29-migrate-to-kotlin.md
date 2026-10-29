# Plan Item: Migrate to Kotlin

**Owning ADR(s):** adr/0002-migrate-to-kotlin.md
**Scope:** Convert the Java codebase to Kotlin.
**Exit Criteria:**
1. `build.gradle` updated with Kotlin plugin and dependencies.
2. All source files successfully converted to/written in Kotlin.
3. Project compiles and `./gradlew build` passes.
4. All existing tests pass in Kotlin.
5. No regression in functionality.

**Dependencies:** 
- ADR 0001 (Spring Boot 4.1.2 upgrade must be completed first).

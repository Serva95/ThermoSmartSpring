# Plan Item: Complete the Kotlin migration

**Owning ADR(s):** [adr/0007-complete-the-kotlin-migration.md](../../adr/0007-complete-the-kotlin-migration.md)

**Cross-repo scope note.** This item is **single-repo** (`ThermoSmartSpring` only). It finishes the conversion ADR 0002's AC-5 hybrid close deliberately left open — the "old ones migrated piece by piece" that were never picked up. No changes to `ThermoTools` or `ThermoWrapperK`.

**Scope (30 Java files → Kotlin), grouped in a recommended conversion order (each layer should keep the build green):**

1. **Exceptions (6):** `exception/BlankFieldsException`, `exception/InvalidAuthorityException`, `exception/InvalidFieldException`, `exception/ObjectAlreadyExistException`, `exception/PasswordException`, `exception/UsernameAlreadyExistException` — leaf classes, no dependencies; convert first.
2. **DTO (1):** `dto/AVGDTO`.
3. **Repositories (8):** `repo/AuthoritiesRepository`, `repo/InfoRepository`, `repo/OrariOnOffRepository`, `repo/RoomRepository`, `repo/SensorRepository`, `repo/TempRepository`, `repo/UserRepository`, `repo/VmcRepository`.
4. **DAOs (8):** `dao/AuthoritiesDAO`, `dao/InfoDAO`, `dao/OrariOnOffDAO`, `dao/RoomDAO`, `dao/SensorDAO`, `dao/TempDAO`, `dao/UserDAO`, `dao/VmcDAO`.
5. **Config / entry points (5):** `AppConfig`, `WebMvcConfiguration`, `WebSecurityConfig`, `ServletInitializer`, `ThermoSmartSpringApplication`. `WebSecurityConfig` is security-sensitive — its rules must be preserved **in effect** (including the `POST /register → ROLE_ADMIN` and `/register` authentication requirements added this cycle); byte-for-byte rule changes are out of scope here.
6. **Tests (2):** `SecurityDialectRenderTest`, `ThermoSmartSpringApplicationTests` — convert last so the conversion is validated by the suite running in Kotlin.

**Approach (resolves the ADR's open question).** Staged, per-layer commits in the order above — each commit independently passes `./gradlew build`. This keeps every step small and revertable, consistent with the repo's verify-gate discipline. (A single big-bang commit is acceptable if preferred, but per-layer is the recommendation.)

**Exit Criteria (mapped to ADR 0007 acceptance criteria):**

| # | Criterion | AC mapping |
|---|-----------|-----------|
| 1 | `find src -name '*.java'` returns no files — no Java source remains in the main or test trees. | AC-1 |
| 2 | All 28 main-source files listed in scope plus both test classes are converted to Kotlin with equivalent behaviour. | AC-1, AC-3 |
| 3 | `./gradlew build` passes: clean compilation and all unit and integration tests green. | AC-2 |
| 4 | Behaviour preserved end to end: identical HTTP endpoints, response bodies, and error strings; `WebSecurityConfig`'s security rules unchanged in effect. | AC-3 |
| 5 | No ADR identifier introduced into any user-emitted string (UI copy, API responses, error messages, customer-visible logs). | AC-4 |

**Completion event** (per CONVENTIONS.md / AGENTS.md):
- ADR 0007 walked `Proposed` → `Accepted` first (populate the Approvals table, regenerate `INDEX.md` to `Accepted`).
- `./gradlew build` passes (exit criterion 3).
- `git mv plan/todo/0002-complete-the-kotlin-migration.md plan/done/<date>-0002-complete-the-kotlin-migration.md`.
- ADR 0007 advances `Accepted` → `Implemented` on that same commit; a new Revision History row is appended; `INDEX.md` regenerated to `Implemented`.
- Append the change to `_agent/WORKLOG.md` (audit trail).

**Dependencies:**

- **ADR 0007** ([adr/0007-complete-the-kotlin-migration.md](../../adr/0007-complete-the-kotlin-migration.md)) — the spec.
- **ADR 0002** ([adr/0002-migrate-to-kotlin.md](../../adr/0002-migrate-to-kotlin.md)) — the parent decision whose AC-5 hybrid close this item completes; its Kotlin/JPA/Gradle tooling (Kotlin JVM & JPA plugins, `build.gradle`) already exists and is reused, so **no** build-system changes are expected.

**Out of scope** (per ADR 0007):
- Changing business logic, architecture, or observable behaviour of any component — language conversion only.
- The deferred security items (the `/debug` admin gate; the forgeable `apikey` scheme in `TempService.save`; dead `permitAll` entries) — separate decisions, not part of this conversion.
- Introducing new third-party libraries beyond what Kotlin support already requires.
- Any change to `ThermoTools` or `ThermoWrapperK`.

---

Shipped at ThermoSmartSpring HEAD `1dbcb25` (all 30 Java files converted to Kotlin across six per-layer commits — `c6a5686` exceptions, `12b291e` DTO, `84b8bb6` repositories, `518faa9` DAOs, `f576d5c` config/entry points, `1dbcb25` tests — each `./gradlew build` green; `find src -name '*.java'` returns nothing; 3 tests pass). ADR 0007 status `Accepted → Implemented`.

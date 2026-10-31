# Plan Item: Migrate ThermoTools to Spring Boot 4.1.2

**Owning ADR:** [adr/0006-migrate-thermotools-to-spring-boot-4-1-2.md](../adr/0006-migrate-thermotools-to-spring-boot-4-1-2.md)

**Cross-repo scope note.** The code changes happen in the **ThermoTools
repository** (submodule at `ThermoTools/` in the umbrella checkout;
canonical build entry `./gradlew build` from the repository root after
the tree hoist — ADR 0006 AC-8). The ADR and this plan item live
**here**, in the cross-component catalogue, because `ThermoTools` has
no ADR catalogue of its own (see ADR 0006 Context).

**Status:** queued — **work is NOT started** until explicitly
authorised (owner: 2026-10-29).

## Scope

Upgrade `ThermoTools` from Spring Boot 2.7.18 / Java 11 to **Spring
Boot 4.1.2 / Java 17** in `ThermoTools/build.gradle` (repository root
after the tree hoist of item 7):

1. Bump `org.springframework.boot` to `4.1.2`; **keep**
   `io.spring.dependency-management` (owner decision, 2026-10-31) and
   adjust it to the version SB 4 requires.
2. `sourceCompatibility` from `11` to `17`.
3. `javax.*` → `jakarta.*` migration where SB 4 no longer carries the
   old packages (JPA entities/repositories and Postgres setup must
   keep working — driver 42.7.4 or the BOM-pinned version).
4. **Remove** the compileOnly `spring-integration-mqtt` line — no
   `ThermoTools` source imports `org.springframework.integration`
   (owner decision, 2026-10-31) — and pin or resolve the SB 4 BOM
   versions for: `org.eclipse.paho.client.mqttv3`, `org.json`, Lombok,
   `spring-boot-starter-data-jpa`, `postgresql` runtime.
5. **`version` string token sync in the same commit**
   (`0.5.1_2.7.18` → `0.5.1_4.1.2`).
6. Run `./gradlew build` from the `ThermoTools` repository root; the
   existing test suite must pass (after the hoist of item 7).
7. **Hoist the tree** (owner decision, 2026-10-31 — the project is no
   longer version-dependent): `git mv` the contents of `2.7.18-J/`
   (including `gradlew`, `gradle`, `build.gradle`, `src/`) up to the
   repository root and remove the version-named folder; no `2.7.18-J`
   path reference may remain in the tree.

**Run order:** this item deliberately runs **after**
`ThermoWrapperK/plan/todo/0001-migrate-to-spring-boot-4-1-2.md`
(separate repository) — the lower-risk single-major upgrade (wrapper)
is sequenced first; the double-major jump (ThermoTools) closes out the
fleet.

## Exit criteria (mapped to ADR 0006 acceptance criteria)

| # | Criterion | AC mapping |
|---|-----------|-----------|
| 1 | `build.gradle` (at the repository root after the hoist) declares Spring Boot `4.1.2`; dependency-management plugin **kept** (owner decision) at a SB 4-compatible version. | AC-1 |
| 2 | `sourceCompatibility` = `17`. | AC-2 |
| 3 | `javax.*` → `jakarta.*` migration complete where required; JPA `Info`-style entities + repository intact. | AC-3 |
| 4 | All runtime + test dependencies resolve under the new BOM; `spring-integration-mqtt` line removed, Paho 1.2.5 intact. | AC-4 |
| 5 | `version` string token updated in the same commit. | AC-5 |
| 6 | `./gradlew build` from the repository root passes, test suite green. | AC-6 |
| 7 | Business logic unchanged (MQTT, JSON payloads, persistence). | AC-7 |
| 8 | Tree hoisted: former `2.7.18-J/` contents at the repository root, no `2.7.18-J` path references left in the tree. | AC-8 |

## Completion event (per CONVENTIONS.md)

- `./gradlew build` from the `ThermoTools` root passes
  (exit criterion 6).
- The umbrella `AGENTS.md` build-table row for `ThermoTools` is
  updated (`cd 2.7.18-J && ./gradlew build` → `./gradlew build`) in the
  umbrella repository at ship time, with its pin re-bumped (separate
  umbrella commit; owner go-ahead per push).
- Code commits land in the `ThermoTools` repository (per the umbrella
  umbrella-checkout rule: work happens in the component, commit happens
  in the component) — each push requires the owner's explicit
  go-ahead.
- ADR 0006 status advances `Proposed` → `Accepted` on go-ahead of
  work, then `Accepted` → `Implemented` on ship (Revision History row
  appended, `INDEX.md` regenerated in this repository).
- `git mv todo/0002-migrate-thermotools-spring-boot-4-1-2.md plan/done/<YYYY-MM-DD>-0002-migrate-thermotools-spring-boot-4-1-2.md`
  (completion event in this repository).

## Dependencies

- **ADR 0006** (above) — the spec.
- **ADR 0001** ([adr/0001](../adr/0001-migrate-to-spring-boot-4-1-2.md))
  — the `ThermoSmartSpring` 4.1.2 precedent; target version.
- **After** `ThermoWrapperK/plan/todo/0001-migrate-to-spring-boot-4-1-2.md`
  (separate repository) — run-order dependency, not a hard
  dependency.

## Out of scope

- Any change to business logic, MQTT topic layout, or database schema.
- Creating a new ADR catalogue for `ThermoTools` — the
  cross-component home (this repository) remains the authoritative
  record.

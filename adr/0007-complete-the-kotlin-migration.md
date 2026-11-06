---
adr: 0007
title: Complete the Kotlin migration
status: Implemented
date: 2026-11-06
owner: agent
supersedes:
superseded-by:
depends-on: [0002]
tags: [language, refactor]
---

# ADR 0007 — Complete the Kotlin migration

## Context

ADR 0002 decided to migrate `ThermoSmartSpring` to Kotlin and was closed as `Implemented` under its AC-5 hybrid clause ("if a full conversion is too risky, a hybrid approach where new features are written in Kotlin and old ones are migrated piece by piece is acceptable"). That close was legitimate: the controllers, services, models, and most DTOs were converted. But it left AC-1 ("All existing Java source files are converted to Kotlin") unmet — 30 Java files remain in the data-access, configuration, exception, DTO, and test layers.

0002 is already in a terminal-ish `Implemented` state, and repo convention forbids re-scoping a closed ADR. Rather than reopen it, this ADR records the decision — as a distinct unit of work — to finish the conversion so the "all source files in Kotlin" goal is actually reached instead of deferred indefinitely.

## Capability statement

All source code in `ThermoSmartSpring` is written in Kotlin. The data-access (DAO, repository), configuration, exception, DTO, and test layers that ADR 0002's hybrid close left in Java are converted to Kotlin, so that no `.java` files remain in the main or test source trees, with identical behaviour and a passing build.

## User stories / scenarios

- As a developer, I want every source file in one language, so that I work in a single idiom and there is no ambiguity about where new code belongs.
- As a maintainer, I want the hybrid migration finished, so that Kotlin's null-safety and concision reach the data-access and configuration layers, not just the controllers and services.

## Acceptance criteria

1. `find src -name '*.java'` returns no files — no Java source remains in the main or test trees.
2. `./gradlew build` passes: clean compilation and all unit and integration tests green.
3. Behaviour is preserved end to end: identical HTTP endpoints, response bodies, and error strings, and `WebSecurityConfig`'s security rules unchanged in effect.
4. The conversion introduces no ADR identifier into any user-emitted string (UI copy, API responses, error messages, or customer-visible logs).

## Out of scope

- Changing business logic, architecture, or observable behaviour of any component — this is a language conversion only (matching 0002's own out-of-scope stance).
- The deferred security items tracked elsewhere (e.g. the `/debug` admin gate and the forgeable `apikey` scheme in `TempService`) — separate decisions, not part of this conversion.
- Introducing new third-party libraries beyond what Kotlin support already requires.

## Open questions

None outstanding. The one question raised in the initial draft was settled at transition to Accepted on 2026-11-06:

- **Staging:** **per-layer commits** — exceptions → DTOs → repositories → DAOs → config/entry points → tests — each independently passing `./gradlew build`. Recorded in the plan item's conversion order (plan/todo/0002-complete-the-kotlin-migration.md).

## References / cross-links

- adr/0002-migrate-to-kotlin.md — parent decision; 0007 completes the AC-1 gap its AC-5 hybrid close left open.
- CONVENTIONS.md — ADR-privacy rule and Git contract that govern the conversion work.

## Revision History

| Date | Revision | Author | Change |
|------|----------|--------|--------|
| 2026-11-06 | r1 | agent | Initial draft. |
| 2026-11-06 | r2 | agent | Accepted. Status `Proposed → Accepted` (owner directive "move to accepted", 2026-11-06). Open question resolved: staging settled as per-layer commits, each passing `./gradlew build` (reflected in plan item 0002). Work item already queued in `plan/todo/`. |
| 2026-11-06 | r3 | agent | Implemented: converted the remaining 30 Java files to Kotlin in six per-layer commits (c6a5686 … 1dbcb25), each passing ./gradlew build; find src -name '*.java' returns nothing. Plan item 0002 completed and moved to plan/done/. |

## Approvals

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Agent | agent | 2026-11-06 | [Verified] |
| Owner | lse | 2026-11-06 | "move to accepted" |
| Agent | agent | 2026-11-06 | [Verified] (revision 3) |
| Owner | lse | 2026-11-06 | "yes, ship it" (revision 3) |

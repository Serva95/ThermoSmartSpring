---
adr: 0006
title: Migrate ThermoTools to Spring Boot 4.1.2
status: Implemented
date: 2026-10-30
owner: agent
supersedes:
superseded-by:
depends-on: [0001]
tags: [infrastructure, upgrade, spring, thermotools]
---

# ADR 0006 — Migrate ThermoTools to Spring Boot 4.1.2

## Context

`ThermoTools` (the backend for routing and logic management) currently
runs on **Spring Boot 2.7.18, Java 11**. In late 2026 that line has
passed end of open-source support, so the component carries the same
security and maintainability risk that motivated
[ADR 0001](0001-migrate-to-spring-boot-4-1-2.md) for
`ThermoSmartSpring` — which is already on 4.1.2. The third component,
`ThermoWrapperK`, is being moved to 4.1.2 in its own catalogue
(`ThermoWrapperK/adr/0005-migrate-to-spring-boot-4-1-2.md`, separate
repository), leaving ThermoTools on an EOL framework.

This migration is a **double major jump** (2.7 → 4.1) plus a mandatory
Java 11 → 17 bump: the risk profile is higher than ADR 0001's, because
it spans two Spring Framework major versions and an API
namespace migration (`javax.*` → `jakarta.*`).

`ThermoTools` has no ADR catalogue of its own, so this decision — and
its queued work item — live here, in the cross-component catalogue.

## Capability statement

Upgrade the framework of the `ThermoTools` backend from Spring Boot
2.7.18 to **Spring Boot 4.1.2** (Java 17), with no change to its
observable behaviour: MQTT integration, JSON payload handling, JPA
persistence, and its role as the routing/logic backend of the heating
system all continue to work as they do on 2.7.18.

## User stories / scenarios

- As the operator of the Raspberry Pi, I want no component running on
  an EOL framework, so auto-updates of the ThermoTools jar keep
  landing on a maintained base.
- As a maintainer, I want all three components on Spring Boot 4.1.2,
  so one dependency line, one set of upgrade notes, one security
  patch to track.
- As a developer, I want the jump done in one ADR-scoped step with a
  passing `build`, so the 2.7 → 4.1 gap is closed rather than carried
  forward.

## Acceptance criteria

1. `build.gradle` (at the `ThermoTools` repository root after the tree
   hoist of criterion 8) declares `org.springframework.boot` version
   `4.1.2`; the `io.spring.dependency-management` plugin is **kept**
   (owner decision of 2026-10-31) and adjusted to the version it needs
   to work with Spring Boot 4.
2. `sourceCompatibility` is raised from `11` to `17`.
3. All `javax.*` usage that Spring Boot 4 no longer carries is
   migrated to `jakarta.*`; existing JPA entities/repositories and the
   Postgres setup (driver 42.7.4 or the version the new BOM requires)
   work with no schema migration.
4. Dependencies resolve and behave unchanged under the Spring Boot 4
   BOM: `org.eclipse.paho.client.mqttv3` (1.2.5 or a
   compatibility-required bump) is the MQTT stack — the compileOnly
   `spring-integration-mqtt` line is **removed** (no `ThermoTools`
   source imports `org.springframework.integration`; owner decision of
   2026-10-31) — plus `org.json`, Lombok (compileOnly/annotationProcessor),
   `spring-boot-starter-data-jpa`, `postgresql` runtime.
5. The `version` string in `build.gradle` is updated in the same
   commit to keep its Spring Boot token in sync
   (e.g. `0.5.1_4.1.2`).
6. `./gradlew build` run from the `ThermoTools` repository root
   passes, including the existing test suite (after the tree hoist of
   criterion 8).
7. No change to business logic: MQTT integration points, JSON payload
   shapes consumed/emitted, and the `Info`-style persistence layer
   behave as before; only framework-level changes are made, with the
   single authorized exception recorded below.

   **Authorized exception (owner decision 2026-11-03, phase 2):**
   before the 2.7.18 baseline gate could run, the existing test was
   proven unrunnable — `ThermoToolsApplication.runner()` called
   `Future.get()` on the two `scheduleAtFixedRate()` futures, but a
   periodic `ScheduledFutureTask` resets to NEW after every run (JDK
   `runAndReset`) and never reaches a terminal state, so `get()` can
   never complete by construction; both jobs swallow all of their own
   exceptions, so the catch blocks can never fire either. The only
   reachable outcome was an infinite hang at startup (proven by a
   standalone JDK repro; pre-existing — introduced in ThermoTools
   `edc5539`, before the canonical tree). Removing the two dead
   `try/get/catch` blocks (ThermoTools `a5d3d05`) preserves job firing
   exactly — both still run at t=0 / t=5s and repeat on the 12h / 315s
   cadence — and deletes only the unreachable, infinite-blocking wait.
8. The `ThermoTools` source tree is **hoisted one level up** (owner
   decision of 2026-10-31 — the project is no longer version-bound):
   the contents of `2.7.18-J/` move to the repository root,
   `gradlew`/`gradle`/`build.gradle` live at the root, no reference to
   the `2.7.18-J` path remains in the tree, and the umbrella
   checkout's `AGENTS.md` build-table row for `ThermoTools` is updated
   from `cd 2.7.18-J && ./gradlew build` to `./gradlew build` at ship
   time (umbrella repository, separate commit).

## Out of scope

- Migration of `ThermoSmartSpring` (done — ADR 0001) or
  `ThermoWrapperK` (its own catalogue, separate repository).
- Any change to the business logic (save the authorized AC-7
  exception), MQTT topic layout, or database schema.
- A new ThermoTools ADR catalogue — the catalogue-less component keeps
  recording its decisions in this cross-component home.

## Open questions

None outstanding. All three questions raised in the initial draft were
resolved by the owner on 2026-10-31:

- **`2.7.18-J/` tree:** hoist the project into the `ThermoTools` root
  — the project is no longer version-dependent; canonical build entry
  becomes `./gradlew build` from the root (reflected in AC-1, AC-6,
  and new AC-8).
- **`spring-integration-mqtt`:** **remove** it — no `ThermoTools`
  source uses it; the real MQTT client is Eclipse Paho
  `org.eclipse.paho.client.mqttv3:1.2.5` (reflected in AC-4).
- **`io.spring.dependency-management` plugin:** **keep** it (reflected
  in AC-1).

## References / cross-links

- [ADR 0001](0001-migrate-to-spring-boot-4-1-2.md) — the `ThermoSmartSpring` 4.1.2 precedent and this ADR's `depends-on`
- `ThermoWrapperK/adr/0005-migrate-to-spring-boot-4-1-2.md` — the sibling component's migration (separate repository)
- `build.gradle` — current baseline (at the `ThermoTools` repository root)
- [Spring Boot 4.1.2 release notes](https://spring.io/blog/2026/10/23/spring-boot-4-1-2-available-now)
- [Spring Boot 3 → 4 migration guide (jakarta namespace)](https://github.com/spring-projects/spring-boot/wiki/Migrating-from-Spring-Boot-3.x-to-4.0)

## Revision History

| Date | Revision | Author | Change |
|------|----------|--------|--------|
| 2026-10-30 | r1 | agent | Initial draft. |
| 2026-10-31 | r2 | agent | Owner resolved all three open questions (2026-10-31): `2.7.18-J/` tree hoisted into the repository root (new AC-8; AC-1 and AC-6 updated; out-of-scope bullet dropped), `spring-integration-mqtt` removed (AC-4), `io.spring.dependency-management` plugin kept (AC-1). Open-questions section converted to the resolved record. |
| 2026-11-03 | r3 | agent | Accepted. Owner authorised the work start (2026-11-03); status `Proposed → Accepted`. |
| 2026-11-03 | r4 | agent | Phase 2 progress: recorded the authorized AC-7 exception (ThermoTools `a5d3d05` — two dead `Future.get()` blocks on periodic tasks removed; job firing unchanged), AC-8 hoist with README rewrite (ThermoTools `8406e18`); baseline gate and post-hoist root gate both GREEN. Editorial: reference path `2.7.18-J/build.gradle` → `build.gradle` after the hoist. |
| 2026-11-04 | r5 | agent | Implemented: ThermoTools shipped at HEAD `3946325` (Spring Boot 4.1.2, dep-mgmt 1.1.6, Java 17, Gradle 8.14, `version='0.5.1_4.1.2'`; `./gradlew build` GREEN including `contextLoads()` against live dev Postgres); plan item 0002 moved to `plan/done/`; status `Accepted → Implemented`. |

## Approvals

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Agent | agent | 2026-11-03 | [Verified] |
| Agent | agent | 2026-11-03 | [Verified] (revision 4) |

---
adr: 0006
title: Migrate ThermoTools to Spring Boot 4.1.2
status: Proposed
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

1. `2.7.18-J/build.gradle` declares `org.springframework.boot` version
   `4.1.2` and the `io.spring.dependency-management` plugin at a
   version compatible with Spring Boot 4 (or is removed if no longer
   needed).
2. `sourceCompatibility` is raised from `11` to `17`.
3. All `javax.*` usage that Spring Boot 4 no longer carries is
   migrated to `jakarta.*`; existing JPA entities/repositories and the
   Postgres setup (driver 42.7.4 or the version the new BOM requires)
   work with no schema migration.
4. Dependencies resolve and behave unchanged under the Spring Boot 4
   BOM: `org.eclipse.paho.client.mqttv3` (1.2.5 or a
   compatibility-required bump), `org.json`, `spring-integration-mqtt`
   (compileOnly), Lombok (compileOnly/annotationProcessor),
   `spring-boot-starter-data-jpa`, `postgresql` runtime.
5. The `version` string in `build.gradle` is updated in the same
   commit to keep its Spring Boot token in sync
   (e.g. `0.5.1_4.1.2`).
6. `cd 2.7.18-J && ./gradlew build` passes, including the existing
   test suite.
7. No change to business logic: MQTT integration points, JSON payload
   shapes consumed/emitted, and the `Info`-style persistence layer
   behave as before; only framework-level changes are made.

## Out of scope

- Migration of `ThermoSmartSpring` (done — ADR 0001) or
  `ThermoWrapperK` (its own catalogue, separate repository).
- Renaming or restructuring the `2.7.18-J/` source tree (see Open
  questions).
- Any change to the business logic, MQTT topic layout, or database
  schema.
- A new ThermoTools ADR catalogue — the catalogue-less component keeps
  recording its decisions in this cross-component home.

## Open questions

- Whether the `2.7.18-J/` tree should be renamed after the migration
  (its name refers to the Spring Boot version it will no longer run on)
  — owner call; until then `2.7.18-J/` remains the canonical build
  entry point (`cd 2.7.18-J && ./gradlew build`).
- Whether `spring-integration-mqtt` (pinned as compileOnly, version
  from the Spring Boot BOM) resolves cleanly on the Spring Boot 4 BOM,
  or needs an explicit version pin — decide at implementation time.
- Whether the `io.spring.dependency-management` 1.0.15 plugin remains
  required on Spring Boot 4 or can be dropped.

## References / cross-links

- [ADR 0001](0001-migrate-to-spring-boot-4-1-2.md) — the `ThermoSmartSpring` 4.1.2 precedent and this ADR's `depends-on`
- `ThermoWrapperK/adr/0005-migrate-to-spring-boot-4-1-2.md` — the sibling component's migration (separate repository)
- `2.7.18-J/build.gradle` — current baseline
- [Spring Boot 4.1.2 release notes](https://spring.io/blog/2026/10/23/spring-boot-4-1-2-available-now)
- [Spring Boot 3 → 4 migration guide (jakarta namespace)](https://github.com/spring-projects/spring-boot/wiki/Migrating-from-Spring-Boot-3.x-to-4.0)

## Revision History

| Date | Revision | Author | Change |
|------|----------|--------|--------|
| 2026-10-30 | r1 | agent | Initial draft. |

## Approvals

| Role | Name | Date | Signature |
|------|------|------|-----------|

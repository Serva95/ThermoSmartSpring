---
adr: 0004
title: Consolidate the three thermo component repositories into a single monorepo
status: Superseded
date: 2026-10-29
owner: agent
supersedes:
superseded-by: [0005]
depends-on: [0003]
tags: [repository, monorepo, engineering-process, collaboration, adr-catalogue, ci]
---

# ADR 0004 — Consolidate the three thermo component repositories into a single monorepo

> **Superseded by [ADR 0005 — Umbrella checkout of the three thermo component repositories](0005-umbrella-checkout.md)** (2026-10-29). The monorepo consolidation was withdrawn at the proposal stage: the three components keep their own remotes, branches, and histories, and are joined in a single working tree via submodules in a new umbrella repository. The adopted decision is recorded in ADR 0005. This document is retained as the historical record of the considered-and-rejected approach.

## Context

The heating system is built from three tightly coupled components that today live in three separate git repositories:

- `ThermoSmartSpring` — the Spring Boot 4.1.2 / Kotlin web application (user management, room control UI, API).
- `ThermoTools` — the headless decision backend (room on/off algorithms; reads the same database; emits the relay control message).
- `ThermoWrapperK` — the Kotlin supervisor on the same Raspberry Pi that runs the two jars, and owns the Let's Encrypt certificate lifecycle (certbot install/issue/renewal per the companion ADR for the wrapper side).

These are not independent products. They share a single host, a single database (MariaDB/PostgreSQL `thermo`), a single build toolchain (Kotlin + Gradle + Spring Boot), and the certificate lifecycle is explicitly split across the pair (one decision, two repos, two plan queues). That produces recurring friction:

- **One decision, many ADRs.** Decisions like *trusted TLS on the public domain*, *the relay-message contract*, and *database schema ownership* span two or three repos. They are impossible to record in one place, and each repo re-derives the shared assumptions.
- **Cross-repo work is awkward.** A schema change, a shared constant, or a cert-bundle path fix currently means checking out three repos, editing three times, and committing in three places. The ADR catalogue that *should* own these decisions lives in only one of them.
- **No single source of truth for the system's decisions.** The convention home (`AGENTS.md`, `adr/`, `plan/`) is one of three arbitrary choices.
- **Security debt.** At least two repos carry real credential values in tracked files: `src/main/resources/application.properties` in the app, and `application.properties` in the decision backend (local and production DB passwords). In a single repo these are fixable with one convention and one CI secret-gate; today each repo must remember to do it independently, and one has not.

The forces pulling the other way are real too: the three components have different release cadences, different owners of day-to-day changes, and the decision backend still carries a legacy branch (`2.7.18-J`) alongside the active one. A monorepo must not *worsen* local iteration or rewrite history, and it must not be done as a big-bang copy that destroys git history.

## Capability statement

The system is developed in **one git repository** that contains all three components, each in a stable top-level subdirectory, with one ADR catalogue (in the `app/` component, which is the conventions home), one plan queue, one CI configuration, and one remote. Git history of every component is preserved in full. No component's build, run, or deploy behaviour changes as a result of the move. Cross-component decisions are recordable in a single ADR and workable in a single checkout.

## User stories / scenarios

- As a **builder**, I want app, decision backend, and supervisor in one checkout, so that a cross-component change (shared DB schema, relay-message contract, cert-bundling path) is one edit, one build set, one commit — not a three-repo dance.
- As a **builder**, I want every component's full git history intact, so that `git log -S`, `git blame`, and past-A lookups keep working after the move.
- As an **agent or new contributor**, I want one `AGENTS.md`, one ADR catalogue, one plan queue, so that the conventions I follow are the same wherever in the tree I work.
- As a **maintainer of the decision backend**, I want its legacy `2.7.18-J` branch preserved and clearly marked, so that the move does not force either deletion or a premature port.
- As a **security reviewer**, I want one secret-scanning gate applied to the whole tree, so that no component can carry tracked credentials again.
- As a **builder**, I want the same `opencode` integration model (fast-forward, `Rationale:` footer, verify gate) to hold at the root, so that the workflow I already use does not change.

## Acceptance criteria

1. After the move, the root repository contains all three components under stable top-level subdirectories: `app/` (ThermoSmartSpring), `tools/` (ThermoTools, including both the active `3.5.X` and the legacy `2.7.18-J` branch content as it existed), and `wrapper/` (ThermoWrapperK).
2. `git log` at the root shows the complete, unrewritten commit history of each component (commits attributable to the component's original tree are reachable, with original author, date, and message); no commit was squashed, re-authored, or rebased as part of the move.
3. The ADR catalogue moves (or is designated home) in `app/adr/`; the existing ADRs 0001–0003 and the new ADR 0004 are reachable at that path, and `INDEX.md` at the catalogue root regenerates from their metadata; no ADR loses its number or its metadata block.
4. The convention home (`AGENTS.md`, `CONVENTIONS.md`, `adr/`, `INDEX.md`, `plan/`) is a single set at the root of the repository; no component subtree carries a second, divergent `CONVENTIONS.md` that could be mistaken for authoritative. Where a component keeps its own `AGENTS.md`, it defers to the root conventions and names the subdirectory.
5. `./gradlew build` succeeds for the app and for the decision backend at their new subdirectory paths, with no source changes other than path-dependent config; the wrapper's build succeeds the same way. No behavioural change to any component results from the move.
6. A single CI configuration drives all three component builds (or their documented equivalents); a root-level run builds every component without requiring three separate CI setups.
7. Tracked credential values are removed from version control in the move: the app's `src/main/resources/application.properties` and the decision backend's `application.properties` no longer contain live DB-password literals; both components ship only a credential-free `.example` template, and `.gitignore` excludes real `application.properties` at both locations. This is treated as a precondition of the move, not deferred.
8. The single remote for the root repository is reachable and the `opencode` fast-forward integration model (no merge commits on `opencode`) applies to the root as it applied to the app repo before the move.
9. After the move, at least one cross-component change that previously required three repos (e.g., a shared constant or a coordinated config value) is demonstrated as doable in a single checkout and a single commit.
10. The three former remotes are archived or renamed (not deleted) with a note pointing at the root repository, so that stale links and clones resolve to the new location rather than a dead end.

## Out of scope

- Any change to the behaviour, features, or API of any of the three components.
- The Let's Encrypt deployment itself (that is ADR 0003 and its companion wrapper-side work); this ADR only preserves the ability to track that decision in one place.
- Deleting, archiving, or force-rewriting the legacy `2.7.18-J` branch of the decision backend.
- Changing the runtime deployment layout on the Raspberry Pi (the supervisor still runs the two jars from wherever it puts them).
- Adopting a CI platform, a release pipeline, or a dependency-lock migration.
- Moving the database, its host, or its credentials beyond the secret-hygiene change in AC 7.

## Open questions

- Should the catalogue home stay in `app/` (the current conventions home), or move to the repository root? (Recommendation: keep `app/` as the conventions home to avoid a second move; note this in `CONVENTIONS.md`.)
- Should the legacy `2.7.18-J` branch be folded into the root as a preserved branch, or left frozen? (Recommendation: freeze it; do not rewrite.)
- Does the root keep one `opencode` branch model, or one branch per previous remote? (Recommendation: one `opencode` branch at the root; former remotes archived.)

## References / cross-links

- adr/0003-trusted-letsencrypt-tls.md — the cross-component TLS decision whose joint tracking this ADR makes coherent.
- CONVENTIONS.md — the convention home that moves with the catalogue.
- `app/adr/0001-migrate-to-spring-boot-4-1-2.md`, `app/adr/0002-migrate-to-kotlin.md` — prior decisions that establish the shared Kotlin/Gradle/Spring stack this ADR consolidates.

## Revision History

| Date       | Revision | Author | Change                                                        |
|------------|----------|--------|---------------------------------------------------------------|
| 2026-10-29 | 1        | agent  | Initial draft. Proposed.                                       |
| 2026-10-29 | 2        | agent  | Superseded by ADR 0005 (umbrella checkout). Monorepo approach withdrawn; status set to Superseded. |

## Approvals

| Date       | Role    | Name |
|------------|---------|------|
| _(empty — awaiting acceptance)_ | | |

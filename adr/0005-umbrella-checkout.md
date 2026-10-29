---
adr: 0005
title: Umbrella checkout of the three thermo component repositories
status: Accepted
date: 2026-10-29
owner: agent
supersedes: [0004]
superseded-by:
depends-on: [0003]
tags: [repository, submodules, engineering-process, collaboration, adr-catalogue, git]
---

# ADR 0005 — Umbrella checkout of the three thermo component repositories

## Context

The heating system is built from three tightly coupled components in three separate git repositories:

- `ThermoSmartSpring` — the Spring Boot 4.1.2 / Kotlin web application (user management, room control UI, API).
- `ThermoTools` — the headless decision backend (room on/off algorithms; reads the same database; emits the relay control message).
- `ThermoWrapperK` — the Kotlin supervisor on the same Raspberry Pi that runs the two jars, and owns the Let's Encrypt certificate lifecycle (companion of ADR 0003).

They share a single host, a single database, a single build toolchain (Kotlin + Gradle + Spring Boot), and a certificate lifecycle that is explicitly split across two of them. That produces the same friction ADR 0004 identified: cross-component work is a three-repo dance, and the ADR catalogue that should own the shared decisions lives in only one of the three.

ADR 0004 answered that friction with a **monorepo**: one git repository, one remote, one history, three subdirectories, imported history-preserving. That approach was **withdrawn at the proposal stage**. The owner re-examined the requirement and asked the question the monorepo did not answer directly: *"can we keep the repos separate?"* The answer to that is the decision this ADR records. The monorepo was a means (one working tree, one catalogue) not an end; the umbrella below achieves the same means without the loss of three independent repositories.

What the monorepo gave up, and what this decision must therefore not give up:

- **Three identities.** Each component keeps its own remote, its own branch model, its own history, and its own CI. `ThermoTools` still has its legacy `2.7.18-J` branch alongside the active `3.5.X`; `ThermoWrapperK` is a distinct supervisor, not a sub-package of the app. A monorepo would have had to absorb or archive those identities.
- **Independent release cadence and ownership.** Each component is committed, reviewed, and pushed on its own schedule. A single root repository couples three independent change streams.
- **The existing trust model.** Builders already have three remotes, three sets of credentials, and muscle memory for three checkouts. A fresh root remote would add a fourth artefact to manage while removing three.

What a monorepo provided that this decision must still provide:

- **One working tree.** Cross-component work (shared DB schema, relay-message contract, cert-bundling path) is currently a three-checkout chore. A single tree on disk solves that, independent of how many repositories back it.
- **One authoritative conventions home.** One ADR catalogue, one `CONVENTIONS.md`, one `plan/` queue — reachable without ambiguity.
- **A reproducible entry point.** A new builder (or agent) gets all three components in one `git clone`-able unit.

Git already ships a primitive for exactly this shape: **submodules**. An *umbrella* repository pins named commits of three child repositories and materialises them as subdirectories in its working tree. Crucially, submodules are **not** a consolidation: the child repositories remain fully independent, with their own remotes, own branches (including the frozen legacy branch), and own histories. The umbrella is a *checkout aggregator*, not a repository merger. It is deliberately the lightest mechanism that delivers the two "provide" items above while preserving everything in the "not give up" items.

This ADR supersedes ADR 0004. It records the *repository-structure* decision only. It does not re-decide TLS (ADR 0003), nor does it adopt monorepo-specific machinery (a root CI gate, a root release pipeline). Those remain out of scope on purpose.

## Capability statement

The system is developed in **three independent git repositories**, each retaining its own remote, branch model, history, and CI, **joined in a single working tree** by a fourth *umbrella* repository that pins each of the three to a named commit via git submodules. The umbrella has no component code of its own; it carries only (a) the `.gitmodules` pin definitions, (b) a `README.md` and `AGENTS.md` that name the system ADR catalogue in `ThermoSmartSpring/adr/` as the authoritative conventions home for cross-component decisions, and (c) a `.gitignore`. The system's cross-component decision record is that one catalogue (`ThermoSmartSpring/adr/`, ADRs 0001–0005, plus `INDEX.md`); the umbrella root adds no second `CONVENTIONS.md`, `adr/`, or `plan/` of its own. Component-internal conventions that a component repository already carries (such as the catalogue inside `ThermoWrapperK/`) are that component's own and are untouched by the umbrella. No child repository's history, branch, remote, or build behaviour is changed by the umbrella's existence.

## User stories / scenarios

- As a **builder**, I want app, decision backend, and supervisor in one working tree, so that a cross-component change (shared DB schema, relay-message contract, cert-bundling path) is one `ls`, one search, one set of builds — not a three-checkout dance — while I still commit and push each component to *its own* remote.
- As a **builder**, I want each component to keep its own remote, branch, history, and CI, so that the monorepo's coupling of three independent change streams does not happen, and `ThermoTools`'s legacy `2.7.18-J` branch is neither absorbed nor rewritten.
- As an **agent or new contributor**, I want one `AGENTS.md`, one ADR catalogue, and one `plan/` queue, so that the conventions I follow are the same wherever in the tree I work, and the catalogue home is unambiguous.
- As a **builder**, I want the umbrella to be reproducible with `git clone` + `git submodule update --init`, so that a fresh machine yields a working tree of all three components without manual path surgery.

## Acceptance criteria

1. **Umbrella with exactly the three submodules.** One umbrella repository (remote `https://github.com/Serva95/Thermo-monorepo.git`, branch `master`) whose only repository-level contents are `.gitmodules`, `README.md`, `AGENTS.md`, and `.gitignore`; exactly three submodules — `ThermoSmartSpring/`, `ThermoTools/`, `ThermoWrapperK/` — and its `.gitmodules` entries point at the real component remotes (`https://github.com/Serva95/ThermoSmartSpring.git`, `https://github.com/Serva95/ThermoTools.git`, `https://github.com/Serva95/ThermoWrapperK.git`). No component source code of its own.
2. **No history or branch rewritten.** All three components retain their own remotes, own branches (including `ThermoTools/2.7.18-J` and `ThermoTools/3.5.X`), and full commit histories. No history is squashed, re-authored, rebased, or imported into another repository.
3. **Reproducible fresh checkout.** `git clone https://github.com/Serva95/Thermo-monorepo.git` followed by `git submodule update --init` yields a working tree containing all three components at their pinned commits.
4. **All three builds pass in place.** `./gradlew build` succeeds for `ThermoSmartSpring`, `ThermoTools`, and `ThermoWrapperK` inside the umbrella working tree, with no source changes other than path-dependent config. No component's build, run, or deploy behaviour changes as a result of the umbrella.
5. **Exactly one conventions home.** The system's cross-component decision record is exactly one catalogue: `ThermoSmartSpring/adr/` (ADRs 0001–0005) plus `ThermoSmartSpring/INDEX.md`, with its `CONVENTIONS.md` and `plan/` queue beside it. The umbrella `README.md` and `AGENTS.md` both name it the authoritative conventions home, and the umbrella root adds **no** second `CONVENTIONS.md`, `adr/`, or `plan/` of its own. Catalogues that a component repository already carries internally (for example `ThermoWrapperK/adr/`) are that component's own and remain untouched by the umbrella.
6. **Original remotes stay active.** All three component remotes remain active and unarchived; each component's integration model (branch name, commit model, build gate) is unaffected by the umbrella's existence.
7. **One cross-component session demonstrated.** At least one change that spans components (the README "system context" pointers in `ThermoTools` and `ThermoWrapperK`, plus the pointer in the app's README/AGENTS) is committed to each component repository involved, and the umbrella commits the resulting updated submodule pins — proving the one-tree / own-remote workflow end to end.
8. **Local uncommitted work preserved.** Nothing uncommitted in any component at move time (the `ThermoTools` in-progress edits, `ThermoWrapperK`'s untracked local `application.properties`) is lost, staged, or committed by the move or by this project's commits.

## Out of scope

- Any change to the behaviour, features, API, or CI of any of the three components.
- The Let's Encrypt deployment and certificate lifecycle (that is ADR 0003); the umbrella only keeps that decision's two-sided tracking coherent.
- A root-level CI gate, release pipeline, or shared build orchestrator at the umbrella. Each component keeps its own build and CI; the umbrella adds none.
- Adopting or migrating any dependency-lock or build-toolchain configuration.
- Moving the database, its host, or its credentials.
- Rewriting, merging, or archiving any component's history or branches (including the legacy `2.7.18-J`).
- Renaming or restructuring the component source trees inside their own repositories.

## Open questions

- What should the umbrella pin *by default*: the tip of each component's active branch, or a specific reviewed commit? (Recommendation: pin explicit reviewed commits; update the pin deliberately, as part of the workflow ADR's AC-7 demonstrates, rather than auto-following branch tips.)
- Should the umbrella be *pushed* to its own remote, or kept as a local-only working-tree convenience? (Recommendation: give it a remote so the pin set is shareable and recoverable; it carries no secrets beyond what the three component remotes already expose.)
- Does a component ever add or remove a submodule? (Recommendation: no. The three-component set is fixed for this system; if the system grows a fourth component, revisit as a new ADR rather than extending this one.)

## References / cross-links

- adr/0004-consolidate-into-monorepo.md — the considered-and-superseded monorepo approach this ADR replaces.
- adr/0003-trusted-letsencrypt-tls.md — the cross-component TLS decision whose two-sided tracking (app + supervisor) a single working tree keeps coherent.
- CONVENTIONS.md — the convention home; this ADR fixes it as the single authoritative home in the tree.
- ThermoTools/ and ThermoWrapperK/ README(s) — the component self-descriptions this ADR leaves unchanged.

## Revision History

| Date       | Revision | Author | Change                                                        |
|------------|----------|--------|---------------------------------------------------------------|
| 2026-10-29 | 1        | agent  | Initial draft. Accepted; supersedes ADR 0004.                 |
| 2026-10-29 | 2        | agent  | Capability statement + AC-5: scope the single-conventions-home prohibition to the umbrella root; pre-existing component-internal catalogues (e.g. `ThermoWrapperK/adr/`) are that component's own and untouched. |

## Approvals

| Role    | Name | Date       | Signature |
|---------|------|------------|-----------|
| Agent   | agent| 2026-10-29 | [Verified]|
| Owner   | lse  | 2026-10-29 | "ok let's go" |

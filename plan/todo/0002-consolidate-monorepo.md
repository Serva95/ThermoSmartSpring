# Plan Item: Consolidate the three thermo component repositories into a single monorepo

**Owning ADR(s):** [adr/0004-consolidate-into-monorepo.md](../../adr/0004-consolidate-into-monorepo.md)

**Queue position:** 0002 — runs after `0001-letsencrypt-app-side.md` (the Let's Encrypt app-side work should land in the app repo before the tree moves, so the move does not carry a half-finished ADR 0003).

**Cross-repo scope note.** This is the first plan item that spans all three components (`ThermoSmartSpring`, `ThermoTools`, `ThermoWrapperK`). It changes *where* the code lives, not what it does. It executes ADR 0004 (Proposed until accepted — see Dependencies). The move is additive: no source is rewritten, no history is squashed.

**Scope:**

1. Create the new root repository (fresh remote) with the subdirectory layout `app/`, `tools/`, `wrapper/`.
2. `git subtree add` (or equivalent history-preserving import) each of the three components into its subdirectory, preserving all commit history of the active branch of each.
3. Designate the ADR catalogue home in `app/adr/` (existing 0001–0004 + INDEX) as the single catalogue; update `CONVENTIONS.md` to record the catalogue home and the root structure.
4. De-duplicate convention files: keep one authoritative `CONVENTIONS.md` (root or `app/`, per accepted open question); any per-component `AGENTS.md` defers to the root.
5. Secret hygiene (AC-7, precondition): remove the tracked credential values from the app's `src/main/resources/application.properties` and the decision backend's `application.properties`; ship only `.example` templates; add `.gitignore` entries for real `application.properties` in all three components.
6. Add a single CI configuration at the root that builds all three components; verify the three `./gradlew build` gates pass at their new subdirectory paths.
7. Archive (not delete) the three former remotes with a pointer to the root repository.
8. Demonstrate one cross-component change in a single checkout/commit (AC-9).

**Exit Criteria (mapped to ADR 0004 acceptance criteria):**

| # | Criterion | AC mapping |
|---|-----------|-----------|
| 1 | Root repo contains `app/`, `tools/`, `wrapper/` with the respective full component trees; the legacy `2.7.18-J` and active `3.5.X` content of the decision backend are both present. | AC-1 |
| 2 | `git log` at root shows each component's unrewritten history; no commits squashed/re-authored/rebased by the move. | AC-2 |
| 3 | ADR catalogue reachable at the designated home; ADRs 0001–0004 present with intact numbering and metadata; INDEX regenerates. | AC-3 |
| 4 | Single authoritative `CONVENTIONS.md`; no divergent per-component `CONVENTIONS.md` mistaken as authoritative; per-component `AGENTS.md` (if any) defers to root. | AC-4 |
| 5 | `./gradlew build` passes for app and decision backend at new subdirectory paths with no source changes beyond path-dependent config; wrapper builds likewise. | AC-5 |
| 6 | One root CI configuration builds all three components. | AC-6 |
| 7 | No live DB-password literals remain in tracked files in any component; both app and decision backend ship only a credential-free `.example` template; `.gitignore` excludes real `application.properties` in all three. | AC-7 |
| 8 | Single remote for root reachable; `opencode` fast-forward integration model applies at root. | AC-8 |
| 9 | One cross-component change demonstrated in a single checkout and single commit. | AC-9 |
| 10 | Three former remotes archived/renamed with a pointer to the root repo (not deleted). | AC-10 |

**Dependencies:**

- **ADR 0004** (this item) — the spec; must transition to Accepted before work starts.
- **ADR 0003** ([adr/0003-trusted-letsencrypt-tls.md](../../adr/0003-trusted-letsencrypt-tls.md)) — its app-side half (plan item 0001) should be completed before the move so ADR 0003 is not carried half-done.
- **All three existing remotes** — must be reachable for the history-preserving import; archival (AC-10) happens after the move succeeds.
- **Operator/owner approval** for secret hygiene (AC-7) — removing tracked credentials touches real config; confirm the live `application.properties` on the Pi is unaffected (it is git-ignored, not tracked).

**Out of scope** (per ADR 0004):
- Any behavioural/API change to the three components.
- Deleting or rewriting the legacy `2.7.18-J` branch of the decision backend.
- Changing the runtime deployment layout on the Raspberry Pi.
- Adopting a new CI platform, release pipeline, or dependency-lock migration.
- Let's Encrypt deployment itself (ADR 0003 + wrapper-side work).

**Completion event** (per CONVENTIONS.md):
- Exit criteria 1–10 satisfied; all three component builds green at new paths; root CI green.
- `git mv plan/todo/0002-consolidate-monorepo.md plan/done/<date>-0002-consolidate-monorepo.md`.
- ADR 0004 status advances `Accepted` → `Implemented`; new Revision History row appended; `INDEX.md` regenerated to `Implemented`.

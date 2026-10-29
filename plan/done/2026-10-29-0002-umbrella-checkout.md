# Plan Item: Umbrella checkout of the three thermo component repositories

**Owning ADR(s):** [adr/0005-umbrella-checkout.md](../../adr/0005-umbrella-checkout.md) (Accepted; supersedes [ADR 0004](../../adr/0004-consolidate-into-monorepo.md))

**Queue position:** 0002 — independent of `0001-letsencrypt-app-side.md`. The umbrella is a *thin pin layer*: it never moves component code, so the app-side Let's Encrypt work (0001) lands in the `ThermoSmartSpring` submodule on the `opencode` branch regardless of which item ships first, and the umbrella's pin simply picks up the new commit afterwards. Run 0002 now; 0001's completion is a later pin-bump, not a precondition.

**Cross-repo scope note.** This is the first plan item that spans all three components (`ThermoSmartSpring`, `ThermoTools`, `ThermoWrapperK`) **and** introduces a fourth repository — the umbrella. It changes *where the components are checked out together*, not what any of them does. It executes ADR 0005. The move is **additive and non-destructive**: no component source is rewritten, no history is squashed, no branch is merged or archived, and the three component remotes stay live. The umbrella carries no component code of its own.

**What we deliberately do NOT do (ADR 0004 withdrawn):**
- No monorepo merge. We do not `git subtree add` or otherwise import any component into a single repository with one history.
- No root CI / release pipeline / shared build orchestrator at the umbrella.
- No archiving or renaming of any component remote.
- No touching of the `ThermoTools/2.7.18-J` legacy branch.
- No secret-hygiene change in this item (that remains a decoupled follow-up; see ADR 0004 AC-7 is now out of scope for the umbrella).

**Scope:**

1. Create the umbrella remote/repository (`https://github.com/Serva95/Thermo-monorepo.git`, branch `master`). It is *not* a monorepo — the name is historical; the contents are only the pin layer below.
2. Add the three submodules with a `.gitmodules` pointing at the real component remotes:
   - `ThermoSmartSpring/` → `https://github.com/Serva95/ThermoSmartSpring.git` (branch `opencode`)
   - `ThermoTools/` → `https://github.com/Serva95/ThermoTools.git` (branch `3.5.X`)
   - `ThermoWrapperK/` → `https://github.com/Serva95/ThermoWrapperK.git` (branch `master`)
3. Umbrella repo-level files only: `README.md`, `AGENTS.md` (both naming `ThermoSmartSpring/` as the authoritative conventions home), `.gitignore`, and `.gitmodules`. No component source.
4. Record the single-conventions-home rule: the umbrella root adds **no** `CONVENTIONS.md`, `adr/`, or `plan/` of its own; the system's cross-component catalogue is `ThermoSmartSpring/adr/` (+ `INDEX.md`, 0001–0005) with its `CONVENTIONS.md` and `plan/` queue. Component-internal catalogues (e.g. `ThermoWrapperK/adr/`) are untouched.
5. Cross-component session (ADR 0005 AC-7): add a one-line "system context" pointer to `ThermoTools/README.md` and `ThermoWrapperK/README.md` (and the app's `README.md`/`AGENTS.md`), commit each to its own component remote, then commit the updated submodule pins in the umbrella.
6. **Preserve local uncommitted work (ADR 0005 AC-8):** the `ThermoTools` in-progress edits (9 files) and `ThermoWrapperK`'s untracked live `application.properties` must not be lost, staged, or committed by the move. In `ThermoTools` only the intended file is committed via pathspec (`git commit -- README.md`), never with the full index. `ThermoWrapperK`'s untracked `application.properties` is never staged.

**Exit Criteria (mapped to ADR 0005 acceptance criteria):**

| # | Criterion | AC mapping |
|---|-----------|-----------|
| 1 | Umbrella repo exists at `Thermo-monorepo` (branch `master`) with repo contents limited to `.gitmodules`, `README.md`, `AGENTS.md`, `.gitignore`; exactly the three submodules present. | AC-1 |
| 2 | `.gitmodules` URLs point at the three real component remotes; each submodule tracks the component's active branch (`opencode` / `3.5.X` / `master`). | AC-1 |
| 3 | `git log` in each component shows its full, unrewritten history; no commits squashed/re-authored/rebased/imported into the umbrella. | AC-2 |
| 4 | `git clone` of the umbrella + `git submodule update --init` reproduces all three components at their pinned commits on a fresh checkout. | AC-3 |
| 5 | `./gradlew build` passes for `ThermoSmartSpring`, `ThermoTools`, and `ThermoWrapperK` inside the umbrella working tree, with no source changes beyond path-dependent config. | AC-4 |
| 6 | Exactly one conventions home: `ThermoSmartSpring/adr/` (0001–0005) + `INDEX.md`; umbrella `README.md`/`AGENTS.md` name it authoritative; the umbrella root carries no `CONVENTIONS.md`/`adr/`/`plan/` of its own; component-internal catalogues (e.g. `ThermoWrapperK/adr/`) remain untouched. | AC-5 |
| 7 | All three component remotes remain active and unarchived; each component's integration model (branch, commit model, build gate) is unchanged. | AC-6 |
| 8 | A cross-component change (README "system context" pointers) is committed to each involved component remote and the umbrella commits the updated pins. | AC-7 |
| 9 | `ThermoTools` WIP (9 files) still dirty after the move; `ThermoWrapperK` untracked `application.properties` never staged; nothing uncommitted lost. | AC-8 |
| 10 | `ThermoTools/2.7.18-J` neither merged, rewritten, deleted, nor archived. | AC-2 / AC-6 |

**Dependencies:**

- **ADR 0005** (this item) — Accepted; the spec.
- **All three existing component remotes** — must be reachable for the pins to resolve.
- **Operator/owner** — umbrella remote `Thermo-monorepo.git` already created; confirm pin branch choices if any component's active branch differs.
- **Local WIP** — `ThermoTools` in-progress edits must be preserved (pathspec-limited commit only); `ThermoWrapperK` untracked `application.properties` must not be staged.

**Out of scope** (per ADR 0005):
- Any behavioural/API/CI change to the three components.
- A root CI gate, release pipeline, or shared build orchestrator.
- The Let's Encrypt deployment itself (ADR 0003 + wrapper-side work).
- Rewriting, merging, or archiving any component history or branch (incl. `2.7.18-J`).
- Secret hygiene (tracked credentials) — decoupled follow-up, not this item.

**Completion event** (per CONVENTIONS.md):
- Exit criteria 1–10 satisfied; all three component builds green in the umbrella worktree.
- `git mv plan/todo/0002-umbrella-checkout.md plan/done/<date>-0002-umbrella-checkout.md`.
- ADR 0005 status advances `Accepted` → `Implemented`; new Revision History row appended; `INDEX.md` regenerated to `Implemented`.
- Final umbrella pin-bump commit + push.

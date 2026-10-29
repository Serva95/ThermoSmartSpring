# AGENTS.md

This file provides guidance to coding agents working in this repository.

## What this repository is

thermoSmartSpring: An application running on Raspberry Pi to manage a home heating system via phone/tablet/PC using valves and temperature sensors in each room. Includes a separate backend for routing and logic management.

## Repository structure

- `adr/0000-template.md` — canonical ADR template.
- `adr/NNNN-<kebab-slug>.md` — one ADR per decision, contiguous numbering, no gaps.
- `INDEX.md` — table regenerated from every ADR's metadata block.
- `CONVENTIONS.md` — authoring rules (read before editing anything).
- `plan/todo/NNNN-<slug>.md` — pending work, lower numbers run first.
- `plan/done/<YYYY-MM-DD>-<slug>.md` — shipped work, chronological.
- `_agent/` — multi-agent coordination: `ROLES.md`, `WORKLOG.md`, `CURRENT_FOCUS.md`, `HANDOFF.md`, `prompts/`.

## Hard rules when editing ADRs

These come from `CONVENTIONS.md` and override default behaviour:

- **One decision per ADR.** Splits become new ADRs that supersede; never expand scope inside an existing one.
- **Status lifecycle:** `Proposed → Accepted → Implemented → (Superseded | Deprecated)`.
- **Capability ADR section order:** metadata → Context → Capability statement → User stories / scenarios → Acceptance criteria → Out of scope → Open questions → References → Revision History → Approvals.
- **Acceptance criteria are testable and numbered.**
- **ADRs are internal artefacts — never user-visible.** ADR numbers, ADR titles, and the existence of the ADR catalogue must NEVER appear in any string the product emits to users: UI copy, API response bodies, error messages, customer-visible log lines, public documentation, release notes, marketing copy, or support communications. The catalogue is a builder's tool, not a user-facing surface. References ARE allowed in: code comments (`// see adr/0042-foo.md`), commit messages, PR descriptions, internal docs, `AGENTS.md`, `CONVENTIONS.md`, `INDEX.md`, and the `plan/` queue. Rule of thumb: if a non-builder could ever see the string, the ADR reference comes out.

## Implementation work

- Start from the ADRs. Identify which ADRs a code change implements or affects before changing behaviour.
- If implementation reveals a capability gap or changed decision, update the relevant ADR rather than silently diverging.
- Add or update tests for implemented behaviour. Map tests back to ADR acceptance criteria where practical.
- **Do not leak ADR identifiers into user-visible surfaces.** When writing error messages, UI copy, API responses, log lines that ship to customers, public docs, or release notes, refer to the behaviour by its product-level name — never by ADR number, ADR title, or phrases like "per the ADR catalogue". The ADR link belongs in the commit message and (optionally) an inline code comment, not in the string the user reads.

## Audit trail and revision discipline

- Substantive ADR changes append a row to the Revision History table.
- Editorial changes — typos, formatting, link fixes — are excluded from Revision History but must be flagged "editorial" in the commit message.
- The Approvals table populates when an ADR transitions to Accepted and updates on every subsequent substantive revision.
- Regenerate `INDEX.md` from ADR metadata after any ADR status change or new ADR.

## Multi-agent workflow

A single agent owns this repo. The `_agent/` directory tracks live state and history; no LOCKS discipline.

## Plan folder

- A pending item gets a `plan/todo/NNNN-<slug>.md` file BEFORE work starts, naming the owning ADR(s), scope, and exit criteria.
- The completion event is: `git mv plan/todo/X plan/done/<date>-X`.
- The owning ADR(s) advance `Accepted → Implemented` on the same commit. Regenerate `INDEX.md`.

## Git contract

- Commit messages follow **Conventional Commits**.
- Mandatory `Rationale:` footer on any commit that touches an ADR.
- Cross-references between ADRs use relative paths (`adr/NNNN-*.md`).

## Integration model

- **Integration:** direct-to-main, **fast-forward only**. No merge commits on `main`. The verify gate (`./gradlew build`) runs locally and must pass before push. Completion event: fast-forwarded to `main` + remote push succeeded.

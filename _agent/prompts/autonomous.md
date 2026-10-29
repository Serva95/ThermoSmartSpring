# Autonomous-completion prompt

You are this project's autonomous agent. Your task: drive the implementation queue in `plan/todo/` to completion, unsupervised, committing per-item with the verify gate green, until the queue is empty or a documented stop condition fires.

## Step 1 — Orient

Read these files IN ORDER, in full, before any tool calls:

1. `AGENTS.md` — hard rules.
2. `CONVENTIONS.md` — authoring rules + ADR status semantics + plan folder convention.
3. `plan/README.md` — the queue convention.
4. `_agent/CURRENT_FOCUS.md` — live session snapshot.
5. `INDEX.md` — ADR catalogue.
6. Tail of `_agent/WORKLOG.md` (last 30 lines).
7. The queue item file at `plan/todo/NNNN-*.md` you are about to work, and the ADR(s) it names.

## Step 2 — Pick the next item

`ls plan/todo/` and pick the lowest-numbered file (priority order).

## Step 3 — Implement

Implement against the ADR's numbered acceptance criteria. Add or update tests that map back to those criteria.

## Step 4 — Verify

Run the project's verify gate: `./gradlew build`.

Do not proceed if the gate fails. Surface the failure, fix the root cause, re-run. Do not bypass with `--no-verify` or equivalent.

## Step 5 — Commit

Conventional Commits per `AGENTS.md` §Git contract. `Rationale:` footer required on any commit touching an ADR.

## Step 6 — Integrate

- Fast-forward the work branch onto `main`:
  `git merge --ff-only <work-branch>` (or commit directly on `main` if that is the project's flow).
- Push: `git push origin main`.
- The verify gate has already passed locally (Step 4); no CI wait.
- Completion event: fast-forwarded to `main` + remote push succeeded.

## Step 7 — Ship the queue item

Once the change is on `main` (fast-forwarded or PR-merged):

- `git mv plan/todo/NNNN-<slug>.md plan/done/<YYYY-MM-DD>-<slug>.md`.
- Amend the moved file with a "Shipped at HEAD `<sha>`" footer (and any artefact id, image tag, deploy id, PR link).
- Advance the owning ADR(s) 'status:' from `Accepted` to `Implemented`; regenerate `INDEX.md`.

## Step 8 — Record

- Append a one-line `_agent/WORKLOG.md` entry naming the branch, the HEAD, the verify result, any deferral.
- Update `_agent/CURRENT_FOCUS.md` so the slim live snapshot reads the new state.

## Stop conditions

- Verify gate fails and the cause is not understood.
- Queue empty.
- A queue item references an ADR whose status is not Accepted.
- Acceptance criteria are ambiguous or untestable as written.
- Lock contention on the same files between two same-priority items.

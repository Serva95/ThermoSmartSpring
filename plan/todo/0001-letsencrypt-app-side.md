# Plan Item: Serve Let's Encrypt TLS on the public domain (app-side)

**Owning ADR(s):** [adr/0003-trusted-letsencrypt-tls.md](../../adr/0003-trusted-letsencrypt-tls.md)

**Cross-repo scope note.** ADR 0003 spans **this repository** (the Spring app) and **ThermoWrapperK** (the deployment/lifecycle orchestrator). This plan item covers the **app-side, in-repo** work only:
- repointing the Spring server at a **Let's Encrypt**-issued certificate (sourced from a ThermoWrapperK-provided bundle), in place of the self-signed `thermo.p12`;
- updating the committed `application.properties.example` template accordingly;
- updating `README.md` ("Configuration" + "Deploy" sections) to document the Let's Encrypt flow and ThermoWrapperK's lifecycle ownership.

The **ThermoWrapperK-side** work (install certbot, initial issue, scheduled renewal, renew→Spring-restart hook, expiry monitor, alert channel) is tracked in the ThermoWrapperK repository. The **router** (Fritz!Box) must forward port 80 (ACME HTTP-01) and port 8443 (app TLS) to the Pi's LAN IP. Both are preconditions for the *live-system* acceptance criteria below (AC-1, AC-2, AC-3, AC-4, AC-6) to be verifiable in the running deployment — they do not block this repo's exit criteria.

**Scope (in-repo):**

1. `src/main/resources/application.properties.example` (committed template) — replace the self-signed PKCS12 block with the Let's Encrypt-sourced configuration. **Preferred:** Spring's PEM-direct properties (`server.ssl.certificate` / `server.ssl.certificate-private-key`) pointing at the certbot `live/<domain>/` PEM files. **Fallback:** `server.ssl.key-store` pointing at a **file** path (not `classpath:`) that the ThermoWrapperK renew hook populates/refreshes. Either way, **no reference to the self-signed `thermo.p12` bundle or its password remains** in the committed template.
2. `README.md` → "Configuration" (`Server & Security` snippet) and "Deploy" — reflect the Let's Encrypt flow and ThermoWrapperK's lifecycle ownership (certbot install, initial issue, renew hook, restart, expiry monitor, alerting). Retire the old `thermo.p12` snippet.
3. `src/main/resources/application.properties` (real, git-ignored) — operator step to match the committed template; not committed, but its shape must be documented in the README so a maintainer reading the README is not expected to know certbot.
4. (Optional, open question — **not in scope here**): a small `/cert-status`-style endpoint or admin page that surfaces the served cert's `notAfter`, days-to-expiry, and validation state to the maintainer, to support the AC-4 operational check. Defer to a follow-up ADR / item.

**Exit Criteria (mapped to ADR 0003 acceptance criteria):**

| # | Criterion | AC mapping |
|---|-----------|-----------|
| 1 | `application.properties.example` sources a Let's Encrypt-issued certificate for `2bquxcwzlpdfb6cz.myfritz.net` (PEM-direct or file-path PKCS12); **no self-signed `thermo.p12` reference or password remains** in the committed template as the trust anchor. | AC-5 (in-repo portion), AC-1 & AC-7 (prerequisite) |
| 2 | `README.md` "Configuration" + "Deploy" sections describe the Let's Encrypt flow and ThermoWrapperK's lifecycle ownership (certbot install, initial issue, renew hook, restart, expiry monitor, alerting); the old `thermo.p12` snippet is replaced. | AC-3, AC-4 (documentation portion) |
| 3 | `application.properties` (real, git-ignored) is updated to the same shape (operator step, documented via README). | AC-5 (in-repo portion) |
| 4 | `./gradlew build` passes. | CONVENTIONS.md gate |
| 5 | **Not satisfyable by this repo alone** (require the ThermoWrapperK-side work + router port-forward + a running deployment): AC-1 (trust on fresh iOS/Android, Wi-Fi + cellular), AC-2 (LE chain validates + SAN matches domain), AC-3 (automatic renew + restart), AC-4 (expiry monitor + alerting), AC-6 (HTTP-01 reachable at issuance and renewal). These are the live-system go-live gates; they do **not** block this plan item's completion. | AC-1, AC-2, AC-3, AC-4, AC-6 |

**Completion event** (per CONVENTIONS.md):
- `./gradlew build` passes (exit criterion 4).
- `git mv plan/todo/0001-letsencrypt-app-side.md plan/done/<date>-0001-letsencrypt-app-side.md`.
- ADR 0003 status advances `Accepted` → `Implemented`; a new Revision History row is appended; the Approvals table is kept; `INDEX.md` is regenerated to `Implemented`.

**Dependencies:**

- **ADR 0003** ([adr/0003-trusted-letsencrypt-tls.md](../../adr/0003-trusted-letsencrypt-tls.md)) — the spec.
- **ADR 0001** (`Spring Boot 4.1.2`) — the server whose SSL properties we are changing. The PEM-direct option depends on the pinned Spring Boot version supporting `server.ssl.certificate` / `server.ssl.certificate-private-key` for the embedded server; if not, the fallback (file-path PKCS12 via the renew hook) is used.
- **ThermoWrapperK repo** (separate) — certbot install, initial issue, scheduled renew, renew→Spring-restart hook, expiry monitor, alert channel. Tracked there.
- **Router (Fritz!Box)** — forwarding of port 80 (ACME HTTP-01) and port 8443 (app TLS) to the Pi's LAN IP. Owner: network / ThermoWrapperK.

**Out of scope** (per ADR 0003):
- Certificate issuance/renewal tooling (lives in ThermoWrapperK).
- A reverse-proxy / web-server front (de-scoped in ADR 0003; certbot + Spring directly is the path).
- Multi-host / wildcard certificates (not supportable via HTTP-01 on this setup).
- Client-side certificate pinning (out of scope; standard public-store validation is the goal).
- Rate limiting, MFA, WAF/DoS hardening on the now-public endpoint — separate ADR (flagged in ADR 0003 Open questions / AC-7).
- The `permitAll` re-review of the existing Spring Security `permitAll` list (`/`, `/home`, `/register`, `/js/**`, `/stylesheets/**`, `/logo.png`, `/favicon.ico`, `/user/registration`, `/authentication`, `/api/**`) — pre-existing, out of scope for this ADR, but a recommended precondition for a safe go-live (flagged in ADR 0003 AC-7 and Open questions).

---
adr: 0003
title: Serve TLS with an auto-renewing Let's Encrypt certificate on the public domain
status: Accepted
date: 2026-10-29
owner: agent
supersedes:
superseded-by:
depends-on: [0001]
tags: [security, tls, certificates, letsencrypt, acme, infrastructure]
---

# ADR 0003 — Serve TLS with an auto-renewing Let's Encrypt certificate on the public domain

## Context

The heating control web endpoint must be reachable by the phone from **anywhere** — at home on the LAN and on the road over cellular — and a standard client must accept its TLS connection without a scary "untrusted / self-signed certificate" warning.

Today the app serves a **self-signed** PKCS12 bundle on `8443` (`src/main/resources/application.properties` → `server.ssl.key-store=classpath:keystore/thermo.p12`, `server.ssl.key-store-password=…`). A self-signed chain is not present in any device's default trust store, so on the phone the browser/app shows an untrusted-certificate warning and (on some clients) requires manual certificate installation. That is the bug we are actually fixing, and the fix is **not** "make the phone trust this cert" — it is "present a certificate the phone already trusts."

The application's public name is a domain, not a LAN IP: **`2bquxcwzlpdfb6cz.myfritz.net`**. Key properties of that domain shape the design:

- It is an **AVM `myfritz.net` free DDNS**: it always resolves to the current public IP of the router. It gives us exactly one name (`<account>.myfritz.net`) and **no control of the underlying DNS zone** (no ability to add TXT records). This is decisive: **DNS-01 ACME validation is not possible** on this domain, so the only practical ACME method is **HTTP-01**, which relies on the router port-forwarding port 80 to the Pi.
- It is **dynamic** (the IP changes whenever the line reboots). A certificate bound to a fixed IP would not help and is not what Let's Encrypt will issue; the certificate must therefore be bound to **the domain**, and renewal must revalidate through the same domain.
- Let's Encrypt certificates are valid **90 days**. "Always up to date" therefore requires a **renewing** client, not a one-shot certificate. We are standardising on **certbot**, and on **ThermoWrapperK** to own the whole lifecycle (install, initial issue, scheduled renewal, renew→restart of the Spring service, expiry monitoring, alerting on failure). ThermoWrapperK is the declared deployment/lifecycle owner of this app (see `README.md` → Deploy), so the certificate lifecycle belongs with it, not in this repository.

The certificate source itself is an incidental detail of ADR 0001 (the Spring Boot server); moving it from self-signed to Let's Encrypt is a **new trust/capability decision** in its own right and warrants its own ADR. This does not supersede ADR 0001 or ADR 0002 — both remain in force. It only replaces the self-signed trust anchor that happened to be introduced inside ADR 0001's configuration.

Because a publicly-trusted certificate makes this endpoint unambiguously a **public-internet service** (whereas the self-signed cert effectively flagged it as private), this ADR also fixes the resulting boundary: the existing Spring Security form-login/`anyRequest().authenticated()` remains in force, and the only plaintext surface permitted is the brief ACME challenge window.

## Capability statement

The heating control web endpoint, addressed by phone over **`https://2bquxcwzlpdfb6cz.myfritz.net:8443`** from any network, must present a TLS certificate that is **universally trusted by standard clients out of the box** (a public CA, specifically Let's Encrypt) — so no client ever has to install a custom certificate or be shown an untrusted-certificate warning — and it must **stay current automatically** against the 90-day Let's Encrypt lifetime, with renewal and its associated Spring service restart handled by the deployment orchestrator (ThermoWrapperK) and surfaced to the operator only when something is wrong.

## User stories / scenarios

- As a **phone user**, I want to open the heating app whether I'm at home or on the road, so that I never see an "untrusted connection" warning or have to tap past a certificate error to control my heating.
- As a **phone user**, I want the connection to be trusted using only what's already in my phone, so that I don't have to download or install any certificate at all — on iOS or Android, on Wi-Fi or cellular.
- As a **maintainer**, I want the certificate to renew itself every few weeks, so that I never manually reissue or reinstall a certificate, and I am only alerted if a renewal is at risk.
- As a **maintainer**, I want a single command to confirm the served certificate is valid and to see exactly how many days it has left, so that "did it renew?" has a definitive answer.
- As a **security reviewer**, I want the only trust anchor presented on the public endpoint to be a public CA, so that the self-signed `thermo.p12` is retired and cannot linger as a covert fallback.

## Acceptance criteria

1. **Trusted everywhere, by default.** A standard client — a fresh iOS device and a fresh Android device, each on **Wi-Fi and on mobile data** — completes a TLS handshake to `https://2bquxcwzlpdfb6cz.myfritz.net:8443/` with **no untrusted/invalid-certificate warning** and **no manual certificate installation**. Independently verifiable: `openssl s_client -connect 2bquxcwzlpdfb6cz.myfritz.net:8443 -servername 2bquxcwzlpdfb6cz.myfritz.net -verify_return_error` returns `Verify return code: 0 (ok)` against the device/OS default trust store.
2. **Public CA, correct hostname.** The served leaf certificate is issued by a **Let's Encrypt** CA (chain resolving to a public Let's Encrypt root), its chain **validates** against the client's default store, and its **SAN/CN includes `2bquxcwzlpdfb6cz.myfritz.net`** — so the hostname the phone dials is itself the trust anchor (no hostname-mismatch warning).
3. **Renewal is automatic and observable.** ThermoWrapperK (a) installs certbot, (b) performs the initial issue, (c) schedules renewal (systemd timer or certbot's default renewal hook), and (d) runs a renew→restart hook that restarts the Spring service so it loads the refreshed bundle. A forced renewal (`certbot renew` / a scheduled run in dry-run) succeeds and, with **no manual step**, the running service then presents the updated certificate.
4. **Expiry is monitored and alerting works.** A scheduled renewal is present (`systemctl list-timers` and/or ThermoWrapperK logs), and a status check (`certbot certificates` or a ThermoWrapperK `cert-status` equivalent) reports "Valid until" with a **days-to-expiry** value and a **PASS/FAIL** verdict against a threshold (≥ 30 days remaining is PASS). A deliberately-failed renewal raises an operator alert.
5. **Self-signed anchor is retired.** After switchover the public `:8443` endpoint **no longer presents `thermo.p12` (or the `<keystore-password>` bundle) as its trust anchor**; the only chain presented to external clients on `2bquxcwzlpdfb6cz.myfritz.net` is the Let's Encrypt chain. The self-signed bundle must not silently revert as a fallback.
6. **ACME method is HTTP-01 by design.** Because `myfritz.net` offers no DNS-zone control, the designated ACME method is **HTTP-01**: the router forwards **port 80 to the Pi**, certbot's HTTP-01 challenge handler is reachable from the public internet, and validation succeeds at **both** issuance and at each subsequent renewal. (A failed HTTP-01 path — e.g., port not forwarded — must be detectable, not silent.)
7. **Public-exposure boundary is unchanged or better.** After switchover: (a) Spring Security is still enforced — every route *not* in the existing `permitAll` list still requires form-login authentication; (b) plaintext `:80` is used **only** for the ACME challenge window and serves **no application traffic**; (c) **TLS is the sole accepted protocol for application traffic** on `:8443`. The pre-existing `permitAll` list (including `/api/**`) is out of scope here but must be re-reviewed before the switchover is declared complete (see Open questions).

## Out of scope

- **Provisioning of the domain** (`2bquxcwzlpdfb6cz.myfritz.net`) or its DDNS — the AVM `myfritz.net` service and the router keep the A record current; this ADR assumes it exists and is maintained.
- **Router / Fritz!Box port-forwarding** of 80 and 8443 — owned outside this repository (network owner / ThermoWrapperK).
- **Certificate issuance/renewal tooling in this repository** — certbot, its systemd timer, the renew→restart hook, expiry monitoring, and alerting all live in **ThermoWrapperK**. This repository only changes what the app must present and be restart-friendly for.
- A **reverse proxy / web-server front** (Caddy, nginx, Apache) as the certificate holder — chosen *not* here in favour of certbot + the Spring server directly, per maintainer preference.
- **Multiple hostnames / additional domains**, wildcard certificates (not supportable via HTTP-01 on this setup), and per-tenant certificates.
- **Client-side certificate pinning** — standard public-store validation is the goal; pinning is explicitly not required.
- **Rate limiting, brute-force throttling, WAF, DoS hardening** — relevant given the endpoint is now genuinely public, but a separate concern (see Open questions / possible sibling ADR).
- **MFA / password-policy / account-lockout hardening** on the existing Spring Security login — pre-existing and out of scope; only re-confirmation that it is in force is in scope (AC-7a).

## Open questions

- **PEM-direct vs PKCS12 conversion for Spring.** certbot outputs PEM (`fullchain.pem` + `privkey.pem`). Preferred: Spring loads the PEM files directly (`server.ssl.certificate` / `server.ssl.certificate-private-key`) — no conversion, no extra secret. Fallback if the pinned Spring Boot 4.1.2 / embedded server doesn't accept those properties: the renew hook runs `openssl pkcs12 -export` into a fixed `thermo.p12` and points `server.ssl.key-store` at a file path (not `classpath:`), then restarts. **Decision needed at implementation; both are acceptable** and AC-1…AC-5 hold under either.
- **ACME challenge handler.** Default: certbot **standalone** on port 80 at issue/renewal (brief bind, then release; Spring keeps 8443). Alternative: a **webroot** directory served persistently. Need to confirm which ThermoWrapperK can operate and how it interacts with any other listener already bound to port 80 on the Pi.
- **Renewal resilience on a home line.** certbot renewal requires the Pi online, port 80 forwarded, and the `myfritz.net` A record valid *at each ~60-day renewal window*. If any fails, the cert can lapse. Mitigation already implied by AC-4 (expiry monitor + alert on failure). **Open:** what alert channel does ThermoWrapperK have (email/push/log), and is a manual "renew now" runbook documented?
- **`permitAll` re-review before go-live.** The existing security config `permitAll`s `/`, `/home`, `/register`, `/js/**`, `/stylesheets/**`, `/logo.png`, `/favicon.ico`, `/user/registration`, `/authentication`, and **`/api/**`**. Once the endpoint is unambiguously public, this list should be re-audited (in particular `/api/**` and `/register`). **Out of scope for this ADR** but a precondition for a safe switchover; flag to the operator.
- **`server.servlet.session.timeout=60m` + `maximumSessions(3)` + `rememberMe`** — unchanged by this ADR, but worth confirming the phone's "connect from anywhere" story (long sessions, remember-me token over a public endpoint) is acceptable. No change proposed.
- **Alerting channel + operator identity.** Who is paged when the cert is inside the expiry threshold or a renewal fails? (ThermoWrapperK detail; capture in its own doc.)
- **Does ThermoWrapperK expose a `thermo cert-status` (or equivalent) command?** AC-4 names it; if not, we need one added there or the acceptance is met via `certbot certificates` + `openssl x509 … -enddate` only.

## References / cross-links

- `adr/0001-migrate-to-spring-boot-4-1-2.md` — the Spring Boot 4.1.2 server whose SSL this ADR re-sources; dependency on the server version for the PEM-vs-PKCS12 question.
- `adr/0002-migrate-to-kotlin.md` — the Kotlin runtime; unaffected by this ADR (listed for catalogue completeness).
- `README.md` — "Deploy" section: deployment/lifecycle owned by **ThermoWrapperK**; "System Architecture" — central-controller model.
- `src/main/resources/application.properties` (.example) — current self-signed `server.ssl.*` config that this ADR supersedes in practice (not in the ADR-0001 decision).
- `src/main/java/it/srv/ThermoSmartSpring/WebSecurityConfig.java` — the existing Spring Security form-login/`permitAll` set that remains the authz boundary after switchover (AC-7).
- Let's Encrypt — 90-day certificate lifetime, ACME HTTP-01 vs DNS-01: <https://letsencrypt.org/docs/client-challenge-types> and <https://letsencrypt.org/docs/>.
- certbot — renew hooks, deploy hooks, `certbot renew`, standalone/webroot: <https://certbot.eff.org/>, <https://eff-certbot.readthedocs.io/en/stable/using.html#renewal-hooks>.
- AVM `myfritz.net` DDNS (free, no zone control → DNS-01 unavailable on this name): <https://myfritz.net/>.
- OpenSSL verification used as the AC-1 oracle: `openssl s_client … -verify_return_error` (returns code 0 on a chain that validates against the default store).

## Revision History

| Date | Revision | Author | Change |
|------|----------|--------|--------|
| 2026-10-29 | r1 | agent | Initial draft (Proposed). Chose certbot on `2bquxcwzlpdfb6cz.myfritz.net` (HTTP-01, ThermoWrapperK-owned lifecycle) over Caddy proxy or DNS-01 (DNS-01 infeasible on myfritz.net). |
| 2026-10-29 | r2 | agent | Approved for implementation. Plan item `plan/todo/0001-letsencrypt-app-side.md` created for the in-repo (config + README) scope; cross-repo dependencies (ThermoWrapperK certbot, router port-forward) captured. |

## Approvals

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Agent | agent | 2026-10-29 | [Verified] |

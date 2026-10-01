# Home Repair — Technical audit

## Scope

This audit covers the Java/Spring backend, security configuration, Thymeleaf views, CSS, JavaScript, persistence configuration, tests, repository structure and the static GitHub Pages demo.

## High-impact issues corrected

- Removed hardcoded database password from versioned configuration.
- Re-enabled CSRF protection by removing the global CSRF disable.
- Replaced broad public route access with explicit public routes plus authenticated fallback.
- Changed mutable work/admin operations from GET to POST.
- Added work ownership checks and explicit valid state transitions.
- Restricted administrative user operations with method authorization.
- Prevented customers/providers from modifying unrelated work records.
- Validated review ownership, work state and rating range.
- Fixed provider-search string comparisons that used Java reference equality.
- Removed JPQL LIKE operations against enum fields.
- Fixed rating calculation when a provider has no reviewed work.
- Replaced unsafe Optional.get() paths in core services.
- Replaced null authentication responses with UsernameNotFoundException.
- Centralized BCrypt password encoding.
- Reworked image serving to return valid media types and 404s.
- Fixed error controller to render the existing warning template.
- Removed unused legacy JavaScript and unused enums.
- Removed an unused image update path and repository query.
- Corrected broken resource paths, duplicate stylesheet import and invalid CSS.
- Rebuilt profile and registration forms to remove duplicate IDs and broken browser scripts.
- Added responsive rules to the original frontend.
- Added a functional static demo with local persistence and reset.
- Added CI with MySQL plus a GitHub Pages artifact build.
- Added regression tests for work authorization/status rules.

## Dependency decisions

The Java application remains on Spring Boot 2.7 to avoid combining this cleanup with the larger javax-to-jakarta migration required by Spring Boot 3.

It has been moved to Spring Boot 2.7.18, the final open-source release in the 2.7 line. A future backend modernization should migrate to a currently supported Spring Boot generation or be replaced by Home Repair 2.0.

Apache Commons IO was updated from 2.11.0 to 2.22.0.

## Repository structure

- `src/`: original Java/Spring application
- `demo/`: browser-only functional demo
- `.github/workflows/`: CI and GitHub Pages deployment
- `docs/`: audit and maintenance documentation

## Demo security model

The GitHub Pages demo intentionally runs entirely in the browser.

It simulates:

- authentication
- roles
- authorization
- persistence

using browser storage. This makes the product flow testable but is not production security.

The Java backend remains the reference implementation for server-side behavior.

## Remaining technical debt

### Package name

The historical Java package `com.egg.MiMaridoTeLoHace` does not follow modern lowercase package naming conventions.

Renaming it would touch nearly every Java file and directory. It is intentionally not mixed into this functional/security refactor because it offers little runtime benefit and would increase migration risk.

### Spring generation

Spring Boot 2.7 is an old generation. A future migration should move the backend to a supported Spring Boot version and Jakarta namespaces, or replace it with the planned Home Repair 2.0 stack.

### Legacy visual assets

Several original SVG/PNG assets are large. They are preserved because they are part of the project's visual identity and some remain referenced by the original Thymeleaf application.

Future optimization can compress or redraw these assets after visual comparison.

## Verification

CI is expected to validate:

1. Java compilation and tests against MySQL 8.
2. Static demo artifact construction.
3. Absence of internal `/src/main/resources` references in the deployed demo.
4. Successful GitHub Pages deployment after merge to `main`.

Do not treat a change as complete unless the corresponding CI run is green.

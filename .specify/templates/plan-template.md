# Implementation Plan: [FEATURE]

**Branch**: `[###-feature-name]` | **Date**: [DATE] | **Spec**: [link]

**Input**: Feature specification from `/specs/[###-feature-name]/spec.md`

**Note**: This template is filled in by the `/speckit-plan` command; its definition describes the execution workflow.

## Summary

[Extract from feature spec: primary requirement + technical approach from research]

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Java 17 and Spring Boot 3.4.5, verified against the effective `pom.xml`

**Primary Dependencies**: [existing Maven-managed project libraries used by this feature; justify additions]

**Storage**: [existing database and MyBatis paths used by this feature, or N/A]

**Testing**: JUnit 5, Mockito, Spring Boot Test, Maven Surefire, and applicable integration fixtures

**Target Platform**: Existing YUSP Plus deployment model, including TongWeb and supported databases

**Project Type**: Existing multi-module Maven Spring Boot application with Vue 2 client where applicable

**Performance Goals**: [domain-specific, e.g., 1000 req/s, 10k lines/sec, 60 fps or NEEDS CLARIFICATION]

**Constraints**: [domain-specific, e.g., <200ms p95, <100MB memory, offline-capable or NEEDS CLARIFICATION]

**Scale/Scope**: [domain-specific, e.g., 10k users, 1M LOC, 50 screens or NEEDS CLARIFICATION]

**Affected Modules**: [existing Maven modules and packages; do not introduce new structure implicitly]

**Compatibility Baseline**: [public REST/event contracts, schema, configuration, and deployment behavior preserved]

**Assumptions and Deviations**: [document assumptions; cite explicit spec authorization for every deviation, or "None"]

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Existing-system inspection**: Affected code, configuration, tests, contracts, schema scripts,
  security controls, and deployment patterns have been inspected and are listed above.
- **Architecture and injection**: Existing modules and controller/service/domain/persistence/integration
  boundaries are preserved. New or materially changed Spring components use constructor injection.
- **Compatibility**: REST, event, schema, configuration, and deployment contracts remain backward
  compatible, or the spec explicitly authorizes the change with rollout and rollback details.
- **Validation and transactions**: External inputs have Bean Validation coverage, and state-changing
  workflows define service-layer transaction and rollback behavior.
- **Database safety**: Changes use `yusp-plus-dbinit/src/main/resources/sql` conventions. Destructive
  changes include an explicitly approved staged rollout, data protection, and rollback plan.
- **Testing and verification**: Unit, applicable persistence/REST/integration, regression, and edge-case
  tests are planned, followed by the applicable Maven `verify` command.
- **Security and operations**: Authentication, authorization, sensitive data, and external input have
  been reviewed. Structured logging, metrics, meaningful errors, and operator diagnosis are planned.
- **Dependency and scope discipline**: Added dependencies are minimal and justified; existing libraries
  are reused; unrelated refactoring is excluded; assumptions and deviations are documented.

## Project Structure

### Documentation (this feature)

```text
specs/[###-feature]/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code (repository root)
<!-- Replace placeholders with the affected existing modules and packages. -->

```text
[affected-maven-module]/
├── src/main/java/cn/com/yusys/yusp/[existing-package]/
│   ├── controller/
│   ├── service/
│   ├── domain/
│   ├── dao/
│   └── integration/
├── src/main/resources/
└── src/test/java/cn/com/yusys/yusp/[existing-package]/

yusp-plus-dbinit/src/main/resources/sql/[supported-database]/  # when schema/data changes
yusp-plus-oca-web2.0/src/                                      # when the Vue client changes
```

**Structure Decision**: [Document the selected structure and reference the real
directories captured above]

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Explicit Spec Authorization | Why Needed | Rollout / Rollback |
|-----------|-----------------------------|------------|--------------------|
| [constitution rule] | [requirement or decision reference] | [why compliant alternative fails] | [bounded adoption and reversal plan] |

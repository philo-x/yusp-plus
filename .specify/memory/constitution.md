<!--
Sync Impact Report
- Version change: unversioned template -> 1.0.0
- Modified principles:
  - Placeholder Principle 1 -> I. Preserve the Existing System
  - Placeholder Principle 2 -> II. Enforce Boundaries and Explicit Dependencies
  - Placeholder Principle 3 -> III. Protect Contracts, Data, and Transactions
  - Placeholder Principle 4 -> IV. Verify Behavior at the Appropriate Level
  - Placeholder Principle 5 -> V. Build Secure, Observable, Operable Changes
- Added sections:
  - Engineering Constraints
  - Delivery Workflow and Quality Gates
- Removed sections: none; placeholder sections were concretized
- Templates requiring updates:
  - ✅ .specify/templates/plan-template.md
  - ✅ .specify/templates/spec-template.md
  - ✅ .specify/templates/tasks-template.md
  - ✅ .agents/skills/speckit-tasks/SKILL.md
  - ✅ AGENTS.md
- Follow-up TODOs: none
-->
# YUSP Plus Constitution

## Core Principles

### I. Preserve the Existing System
Every implementation MUST inspect the affected code, configuration, tests, and documentation
before design or modification. The Maven reactor architecture, module boundaries, package
structure rooted at `cn.com.yusys.yusp`, established naming conventions, public APIs, database
schema, deployment model, and compatibility constraints MUST remain unchanged unless the feature
specification explicitly authorizes a deviation. Any authorized deviation MUST state its scope,
compatibility impact, migration approach, and rollback path. Feature work MUST NOT include unrelated
refactoring. This protects consumers and operators of an established enterprise platform from
unplanned change.

The Java and Spring Boot versions MUST remain aligned with the effective Maven model. At
ratification, that baseline is Java 17 and Spring Boot 3.4.5. Maven MUST remain the build,
dependency-management, test, and verification system. Version changes require explicit feature
authorization and compatibility evidence; documentation MUST refer to the POM as the source of
truth rather than allowing a second version baseline to drift.

### II. Enforce Boundaries and Explicit Dependencies
Code MUST preserve clear controller, application/service, domain, persistence, and integration
boundaries. Controllers translate and validate transport concerns, then delegate. Application and
service code coordinates use cases and transaction boundaries. Domain types and rules express
business behavior without depending on transport details. DAOs, mappers, and repository code own
persistence access. Integration adapters isolate calls to external systems and translate their
failures and contracts. A layer MUST NOT bypass an intervening boundary merely for convenience.

New Spring components and components materially changed by a feature MUST use constructor injection
for required dependencies; field injection is prohibited in that scope. Existing field injection
outside the feature scope MAY remain to honor the ban on unrelated refactoring. New abstractions or
dependencies MUST solve a demonstrated need. Existing project libraries, utilities, error models,
and integration patterns MUST be reused when suitable.

### III. Protect Contracts, Data, and Transactions
REST endpoints, request and response shapes, status and error semantics, event names, payloads,
ordering expectations, and other externally observable contracts MUST remain backward compatible by
default. A breaking change requires explicit feature authorization, consumer impact analysis,
versioning or coexistence strategy, rollout steps, and rollback steps. Bean Validation MUST be
applied at system boundaries to all applicable external input, including request bodies, parameters,
messages, files, and integration payloads. Validation failures MUST use the repository's established
error-handling contract.

State-changing workflows MUST have explicit transaction boundaries at the application/service
layer, with rollback behavior and external side effects considered deliberately. Controllers MUST
NOT own database transactions. Database changes MUST use the existing repository mechanism under
`yusp-plus-dbinit/src/main/resources/sql`, follow its database-specific organization and sequencing
conventions, and preserve existing data and supported database compatibility. Destructive schema
migrations are prohibited unless the feature specification includes an explicit staged rollout,
data protection or backfill plan, rollback plan, and approval criteria.

### IV. Verify Behavior at the Appropriate Level
Business logic MUST have focused unit tests. Persistence behavior, REST endpoints, and external
integrations MUST have integration tests whenever they are added or materially changed and the
boundary is applicable. Every defect fix MUST include a regression test. Tests MUST cover relevant
edge cases, validation failures, authorization failures, transaction rollback behavior, and external
dependency failures. A test may be omitted only when it is genuinely inapplicable, with the reason
recorded in the plan or pull request.

The affected Maven module and its reactor dependencies MUST pass Maven verification before work is
declared complete. Repository-wide work MUST run `mvn clean verify`; narrower changes MAY run
`mvn -pl <module> -am verify` when the limited scope is documented. A verification failure caused by
an unavailable internal repository or external service MUST be reported with the command and error;
it MUST NOT be represented as a passing result.

### V. Build Secure, Observable, Operable Changes
Every implementation MUST review authentication, authorization, tenant and data access controls,
sensitive data handling, secrets, and all external input affected by the change. Access MUST be
denied by default where authorization is required. Sensitive values MUST NOT be logged or exposed in
errors. Inputs MUST be validated and safely handled at trust boundaries, including protection against
injection, path manipulation, unsafe deserialization, and unbounded resource use where relevant.

Production behavior MUST be observable through structured, contextual logging, meaningful error
handling, and metrics for material workflows and failures. Logs and errors MUST identify the
operation and outcome without exposing credentials, tokens, personal data, or cryptographic material.
New failure modes MUST define how operators detect and diagnose them. Existing project facilities for
logging, metrics, exception translation, correlation, and health reporting MUST be reused before new
infrastructure is introduced.

## Engineering Constraints

- Dependency additions MUST be minimal, justified in the implementation plan, compatible with the
  effective Maven dependency management, and preferred only when existing project libraries cannot
  meet the need. Duplicate frameworks and convenience-only libraries are prohibited.
- Existing Java naming, package layering, Maven module placement, MyBatis resource placement, REST
  wrappers, exception conventions, and deployment configuration MUST be followed in affected code.
- Public API, event, schema, security, or deployment deviations MUST be explicit requirements, never
  incidental implementation effects.
- Assumptions that affect behavior, compatibility, operations, security, or testing MUST be recorded
  in the feature specification or plan. Architectural deviations MUST include rationale, alternatives
  considered, affected consumers, and restoration or rollback steps.
- Generated output, credentials, environment-specific secrets, and local configuration MUST NOT be
  committed.

## Delivery Workflow and Quality Gates

1. Inspect the existing implementation and identify the affected modules, established patterns,
   public contracts, persistence paths, security controls, and deployment assumptions before planning.
2. Record scope, assumptions, compatibility effects, contract effects, schema effects, security
   concerns, observability needs, and any authorized architectural deviation in the spec and plan.
3. Pass the Constitution Check before design and repeat it after design. Unjustified violations block
   implementation.
4. Decompose work into implementation, mandatory applicable tests, validation, transaction, migration,
   security-review, observability, documentation, and Maven-verification tasks. Tasks MUST exclude
   unrelated refactoring.
5. During review, verify boundary discipline, constructor injection in new or materially changed
   components, backward compatibility, input validation, transaction ownership, migration safety,
   dependency necessity, and the absence of sensitive-data exposure.
6. Before completion, run the required unit and integration tests and the applicable Maven `verify`
   command. Record commands, results, known environmental blockers, rollout steps, and rollback steps
   in the delivery evidence.

## Governance

This constitution is the highest-priority engineering policy for feature specifications, plans,
tasks, implementation, and review in this repository. When another local guideline conflicts with a
MUST rule here, this constitution governs. Established code patterns remain constraints only where
they do not conflict with this constitution; conflicts MUST be contained to the feature scope and
documented rather than triggering unrelated cleanup.

Amendments require a documented proposal describing the changed rule, rationale, affected templates
and workflows, compatibility implications, and any adoption or migration plan. The amendment MUST
update this document and all dependent Spec Kit templates in the same change. Versioning follows
semantic versioning: MAJOR for incompatible governance changes or removal/redefinition of principles,
MINOR for new principles or materially expanded obligations, and PATCH for non-semantic clarification.

Every feature plan and pull request MUST demonstrate compliance with the Delivery Workflow and Quality
Gates. Reviewers MUST block undocumented deviations and unverified mandatory requirements. Exceptions
require explicit approval in the feature specification, a bounded duration or scope, named risks, and
remediation or rollback steps. Governance reviews MUST confirm that the version, ratification date,
last-amended date, and Sync Impact Report remain consistent.

**Version**: 1.0.0 | **Ratified**: 2026-07-22 | **Last Amended**: 2026-07-22

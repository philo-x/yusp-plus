---

description: "Task list template for feature implementation"
---

# Tasks: [FEATURE NAME]

**Input**: Design documents from `/specs/[###-feature-name]/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests are mandatory where applicable under the constitution. Include unit tests for
business logic; integration tests for changed persistence, REST, and external-integration boundaries;
and regression and edge-case tests for every affected behavior. Document genuinely inapplicable tests.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Backend module**: `[module]/src/main/java`, `[module]/src/main/resources`, and
  `[module]/src/test/java`
- **Database scripts**: `yusp-plus-dbinit/src/main/resources/sql/[database]/`
- **Vue client**: `yusp-plus-oca-web2.0/src/` and `yusp-plus-oca-web2.0/tests/`
- Paths MUST name existing modules and packages from plan.md.

<!--
  ============================================================================
  IMPORTANT: The tasks below are SAMPLE TASKS for illustration purposes only.

  The /speckit-tasks command MUST replace these with actual tasks based on:
  - User stories from spec.md (with their priorities P1, P2, P3...)
  - Feature requirements from plan.md
  - Entities from data-model.md
  - Endpoints from contracts/

  Tasks MUST be organized by user story so each story can be:
  - Implemented independently
  - Tested independently
  - Delivered as an MVP increment

  DO NOT keep these sample tasks in the generated tasks.md file.
  ============================================================================
-->

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inspect and prepare the existing project without changing its architecture

- [ ] T001 Inspect affected existing modules, packages, tests, and configuration named in plan.md
- [ ] T002 Confirm Java, Spring Boot, and dependency versions from pom.xml and document any justified addition in plan.md
- [ ] T003 [P] Inventory affected REST/event contracts, schema, security controls, and deployment behavior in plan.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

Examples of foundational tasks (adjust based on your project):

- [ ] T004 Add applicable additive migration in yusp-plus-dbinit/src/main/resources/sql/[database]/[script].sql with rollout and rollback notes
- [ ] T005 [P] Extend existing authentication/authorization controls in [module]/src/main/java/[package]/[SecurityConfig].java when required
- [ ] T006 [P] Define boundary validation and compatibility tests in [module]/src/test/java/[package]/[Boundary]Test.java
- [ ] T007 Update shared domain types in [module]/src/main/java/[package]/domain/[Type].java when required
- [ ] T008 Reuse or extend established error handling, structured logging, and metrics in [module]/src/main/java/[package]/[Component].java
- [ ] T009 Update feature configuration in [module]/src/main/resources/[configuration-file] without changing the deployment model

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - [Title] (Priority: P1) 🎯 MVP

**Goal**: [Brief description of what this story delivers]

**Independent Test**: [How to verify this story works on its own]

### Tests for User Story 1 *(mandatory where applicable)* ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T010 [P] [US1] Add business-logic unit test in [module]/src/test/java/[package]/[Service]Test.java
- [ ] T011 [P] [US1] Add applicable REST/persistence/integration and edge-case tests in [module]/src/test/java/[package]/[Boundary]Test.java

### Implementation for User Story 1

- [ ] T012 [P] [US1] Update [Entity/DTO] in [module]/src/main/java/[package]/domain/[Type].java
- [ ] T013 [P] [US1] Update persistence mapping in [module]/src/main/java/[package]/dao/[Dao].java
- [ ] T014 [US1] Implement [Service] with constructor injection and explicit transactions in [module]/src/main/java/[package]/service/impl/[Service]Impl.java
- [ ] T015 [US1] Implement backward-compatible endpoint behavior in [module]/src/main/java/[package]/controller/[Controller].java
- [ ] T016 [US1] Add Bean Validation and established error handling in [module]/src/main/java/[package]/controller/[Controller].java
- [ ] T017 [US1] Add structured logging and applicable metrics without sensitive data in [module]/src/main/java/[package]/service/impl/[Service]Impl.java

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - [Title] (Priority: P2)

**Goal**: [Brief description of what this story delivers]

**Independent Test**: [How to verify this story works on its own]

### Tests for User Story 2 *(mandatory where applicable)* ⚠️

- [ ] T018 [P] [US2] Add business-logic unit test in [module]/src/test/java/[package]/[Service]Test.java
- [ ] T019 [P] [US2] Add applicable boundary and edge-case test in [module]/src/test/java/[package]/[Boundary]Test.java

### Implementation for User Story 2

- [ ] T020 [P] [US2] Update [Entity/DTO] in [module]/src/main/java/[package]/domain/[Type].java
- [ ] T021 [US2] Implement [Service] with constructor injection and explicit transactions in [module]/src/main/java/[package]/service/impl/[Service]Impl.java
- [ ] T022 [US2] Implement backward-compatible endpoint behavior in [module]/src/main/java/[package]/controller/[Controller].java
- [ ] T023 [US2] Integrate through the existing boundary in [module]/src/main/java/[package]/integration/[Adapter].java

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - [Title] (Priority: P3)

**Goal**: [Brief description of what this story delivers]

**Independent Test**: [How to verify this story works on its own]

### Tests for User Story 3 *(mandatory where applicable)* ⚠️

- [ ] T024 [P] [US3] Add business-logic unit test in [module]/src/test/java/[package]/[Service]Test.java
- [ ] T025 [P] [US3] Add applicable boundary and edge-case test in [module]/src/test/java/[package]/[Boundary]Test.java

### Implementation for User Story 3

- [ ] T026 [P] [US3] Update [Entity/DTO] in [module]/src/main/java/[package]/domain/[Type].java
- [ ] T027 [US3] Implement [Service] with constructor injection and explicit transactions in [module]/src/main/java/[package]/service/impl/[Service]Impl.java
- [ ] T028 [US3] Implement backward-compatible endpoint behavior in [module]/src/main/java/[package]/controller/[Controller].java

**Checkpoint**: All user stories should now be independently functional

---

[Add more user story phases as needed, following the same pattern]

---

## Phase N: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] TXXX [P] Documentation updates in docs/
- [ ] TXXX Document assumptions, compatibility effects, and approved architectural deviations
- [ ] TXXX Validate only the performance requirements explicitly defined in spec.md
- [ ] TXXX [P] Complete required regression and edge-case unit/integration tests in affected module test paths
- [ ] TXXX Review authentication, authorization, sensitive data, secrets, and external input handling
- [ ] TXXX Verify REST/event backward compatibility and database rollout/rollback evidence
- [ ] TXXX Verify structured logging, metrics, and meaningful error handling
- [ ] TXXX Run `mvn -pl [affected-module] -am verify` or repository-wide `mvn clean verify`
- [ ] TXXX Run quickstart.md validation

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1 but should be independently testable
- **User Story 3 (P3)**: Can start after Foundational (Phase 2) - May integrate with US1/US2 but should be independently testable

### Within Each User Story

- Required applicable tests MUST be implemented and pass before story completion
- Models before services
- Services before endpoints
- Core implementation before integration
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- All tests for a user story marked [P] can run in parallel
- Models within a story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch independent tests for User Story 1 together:
Task: "Unit test for [service] in [module]/src/test/java/[package]/[Service]Test.java"
Task: "Integration test for [boundary] in [module]/src/test/java/[package]/[Boundary]Test.java"

# Launch all models for User Story 1 together:
Task: "Update [Entity1] in [module]/src/main/java/[package]/domain/[Entity1].java"
Task: "Update [Entity2] in [module]/src/main/java/[package]/domain/[Entity2].java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1
   - Developer B: User Story 2
   - Developer C: User Story 3
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence

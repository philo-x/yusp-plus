# Repository Guidelines

## Project Structure & Module Organization

This Java 17 Maven reactor contains common, UAA, OCA, single-server, extension, database-init, workboard, and file modules; several contain nested `*-core` or `*-starter` modules. Backend code follows Maven conventions: Java in `src/main/java`, configuration and MyBatis XML in `src/main/resources`, and tests in `src/test/java`. The runnable monolith is `yusp-plus-single/yusp-plus-single-starter`. The Vue 2 client lives in `yusp-plus-oca-web2.0`, with code in `src` and static files in `public`. Database scripts are under `yusp-plus-dbinit/src/main/resources/sql`; requirements are in `docs/requirements`.

## Build, Test, and Development Commands

- `mvn clean verify` — compile every Maven module and run the backend test suite.
- `mvn -pl yusp-plus-oca/yusp-plus-oca-core -am test` — test one module plus required reactor dependencies.
- `mvn -pl yusp-plus-single/yusp-plus-single-starter -am spring-boot:run` — start the backend locally; configure `application.yml` and required database/middleware first.
- `cd yusp-plus-oca-web2.0 && yarn install && yarn dev` — install frontend dependencies and launch the Vue development server.
- `yarn lint`, `yarn test:unit`, and `yarn build` — lint, run Jest tests, and create the production frontend bundle.

Internal dependencies may require access to the configured company Artifactory.

## Coding Style & Naming Conventions

Use UTF-8. Keep Java and Spring Boot aligned with the effective Maven model (currently Java 17 and Spring Boot 3.4.5), and use Maven for builds and dependency management. Follow existing Java formatting: four-space indentation, `PascalCase` types, `camelCase` methods and fields, and packages rooted at `cn.com.yusys.yusp`. Keep controller, application/service, domain, persistence, and integration responsibilities in their established package layers; name implementations `*ServiceImpl`. New or materially changed Spring components use constructor injection. Frontend files use the repository `.editorconfig`: two spaces, LF line endings, and trimmed trailing whitespace. Run ESLint before submitting changes; use `PascalCase` for Vue components and `camelCase` for JavaScript identifiers.

## Testing Guidelines

Backend tests use JUnit 5, Mockito, Spring Boot Test, and Maven Surefire. Name test classes `*Test` and methods after behavior, such as `getViewList_ValidInput_ReturnsExpectedOutput`. Add unit tests for business logic and applicable integration tests for persistence, REST endpoints, and external integrations; cover regressions and edge cases. Apply Bean Validation at external boundaries and explicit service-layer transaction boundaries. Run the affected reactor's Maven `verify` before completion; use `mvn clean verify` for repository-wide work. Frontend unit tests run with Jest through `yarn test:unit`.

## Commit & Pull Request Guidelines

Git history is too sparse to establish a reliable message convention. Use short, imperative subjects with an optional module scope, for example `oca: validate notice recipient`. Feature work and reviews must satisfy `.specify/memory/constitution.md`. Preserve public REST/event contracts, schema, deployment behavior, and established architecture unless the feature spec explicitly authorizes a deviation. Put database changes under `yusp-plus-dbinit/src/main/resources/sql` using existing conventions; destructive changes require rollout and rollback plans. Pull requests should explain assumptions, affected modules, verification commands, linked requirements, compatibility and schema effects, security review, and observability/error-handling changes. Include screenshots for UI updates; never commit credentials, generated output, logs, or environment-specific secrets.

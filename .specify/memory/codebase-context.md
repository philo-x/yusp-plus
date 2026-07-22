# YUSP Plus Codebase Context

> Repository snapshot inspected: 2026-07-22. This document describes the code and configuration present in this repository; it does not describe an intended future architecture.
>
> Evidence labels: **Verified** means directly supported by source, POM, configuration, SQL, test, or deployment files. **Uncertain** means the repository suggests a conclusion but does not contain enough executable or operational evidence to prove it. Secrets and credentials are intentionally not reproduced.

## 1. System purpose

YUSP Plus (also called `oca-plus` in `README.md`) is a lightweight enterprise administration and authentication platform. The repository supplies:

- a single deployable Spring Boot application that combines authentication, organization and access administration, notices, files, scheduling, audit trace, and a workboard;
- an OAuth 2 authorization server with an OCA-specific login grant;
- administration of users, institutions, organizations, departments, duties, roles, tenants, menus, control points, functional authorization, and data authorization;
- supporting capabilities for notices, file storage, database-backed Quartz jobs, audit/change trace, internationalized dictionaries/messages, cache-backed session state, and external ESB/HTTP/FTP examples;
- a Vue 2 administration client; and
- a separately executable database initialization utility plus a GoldenDB initialization script.

Evidence: `README.md`, `yusp-plus-oca/README.md`, `yusp-plus-uaa/README.md`, `yusp-plus-single/README.md`, and the module POMs.

The currently represented deployment is a modular monolith, despite README references to a wider microservice platform. `yusp-plus-single-starter` embeds the core modules and is the only Spring Boot production entry point in this repository. **Uncertain:** the README mentions gateway, workflow, generator, CRM, and standalone service variants that are absent or commented out, so those capabilities cannot be treated as part of this checkout.

## 2. Technology inventory

| Area | Repository evidence |
|---|---|
| Language/runtime | Java 17 (`pom.xml` compiler properties). The effective Maven property evaluates Spring Boot to 3.4.5; the same baseline is recorded in `.specify/memory/constitution.md`. |
| Build | Maven reactor, project version `V4.x.x-SNAPSHOT`, external parent `cn.com.yusys.yusp.common:yusp-commons-dependencies:V4.0.1`. No Maven wrapper is committed. |
| Web framework | Spring Boot, Spring MVC, Bean Validation/Jakarta Validation, Spring AOP, Spring asynchronous execution. |
| Authentication/security | Spring Security, Spring Authorization Server, JWT/Nimbus JOSE, JDBC registered OAuth clients, BCrypt, Redis-backed token activity state, company request-crypto/session/access/data-authority starters. |
| Persistence | MyBatis and MyBatis-Plus (`BaseMapper`, `ServiceImpl`, mapper XML), JDBC/JdbcTemplate, HikariCP, GoldenDB JDBC driver; MySQL and Oracle drivers are also dependencies of the database initializer. |
| Database schema | One GoldenDB/MySQL-style SQL initialization file with 67 tables. No Flyway or Liquibase dependency or changelog exists. |
| Cache/state | Spring Data Redis is the active custom cache and token store. A Caffeine implementation exists but is not wired. A company cache-dependency starter tracks table-to-cache dependencies. |
| Scheduling | Quartz 2.5.0 with a JDBC job store and clustered configuration. |
| Files/integrations | Company local-disk, SFTP, file, Excel/CSV, ESB, HTTP client, and FTP starters; Aliyun OSS SDK; plain `RestTemplate` beans. |
| Runtime/observability | TongWeb Spring Boot 3 starter and license, Log4j2 configuration, Spring Boot Actuator, Micrometer Prometheus registry, Springdoc and Knife4j. |
| Serialization/utilities | Jackson, Fastjson, Guava, Apache Commons Lang, Passay. |
| Backend tests | JUnit 5, Mockito, Spring Test/MockMvc, Surefire 3.1.2. H2, MyBatis-Plus test starter, and TestNG are declared at the root but have no active test usage found. |
| Frontend | Vue 2.6.10, Vue Router 3, Vuex 3, Axios 0.19, Vue CLI 3.5.3, YUWP UI, Jest, ESLint, Gulp; managed with Yarn/npm outside Maven. |

Version ownership is mostly delegated to the external parent POM. Explicit local versions include Java 17, Quartz 2.5.0, MyBatis-Plus test starter 3.5.12, Surefire 3.1.2, compiler plugin 3.8.1, release plugin 2.5.3, and Spring Boot Maven plugin 3.4.5 only in the `release` profile.

### Maven profiles and plugins

- Root plugins: `maven-release-plugin` (configured to skip tests during release), `maven-surefire-plugin` (adds Java module opens), and `maven-compiler-plugin` (`-parameters`, source/target 17).
- `yusp-plus-single-starter`: Spring Boot repackage runs by default; Maven deployment is skipped. The `boot` profile repeats the repackage setup. The `release` profile pins plugin version 3.4.5, names the main class, and supplies a restrictive `includes` entry.
- `yusp-plus-dbinit`: copies runtime dependencies to `target/lib` and builds a manifest-driven executable JAR with `YuspPlusDbinitApplication` as its main class.
- Assembly descriptors exist under the OCA and single modules, but no `maven-assembly-plugin` execution is declared in the checked-in POMs. **Uncertain:** they may be invoked externally, but the normal reactor build does not visibly bind them.
- Runtime Spring profile `workboard` is always activated by the main `application.yml`; this is distinct from Maven profiles.

## 3. Module and package map

The root reactor contains eight top-level modules:

| Module | Boundary and responsibility | Key dependencies/consumers |
|---|---|---|
| `yusp-plus-common` | Shared `cn.com.yusys.yusp.common` base entity, pagination/query utilities, enum conversion, exception advice, and optional metrics/logging aspect. | Used by OCA, jobs, trace, and workboard. |
| `yusp-plus-uaa/yusp-plus-uaa-core` | `cn.com.yusys.yusp.uaa`: OAuth authorization-server configuration, OCA grant/provider, JWT generation/decoding, Redis token store, session filter, captcha, logout, and token-check APIs. | Embedded by the single starter; calls an `OcaLoginUserInfo` implementation supplied by the single module. |
| `yusp-plus-oca/yusp-plus-oca-core` | `cn.com.yusys.yusp.oca` and `cn.com.yusys.yusp.notice`: the main administration domain, authorization/data-permission configuration, login/password policies, dictionaries, cache loaders, notices, and most REST APIs. | Depends on common, job core, trace core, Redis/session/file/security company starters. |
| `yusp-plus-single/yusp-plus-single-starter` | `cn.com.yusys.yusp.single`: runnable modular-monolith composition, CORS/static-resource configuration, error endpoint, and bridge from UAA login to OCA user validation. | Embeds OCA, UAA, file core, and workboard; receives job/trace transitively from OCA. |
| `yusp-plus-extend/yusp-plus-job-core` | `cn.com.yusys.yusp.job.core`: Quartz configuration, schedule/job-log entities and APIs, task interface, and scheduler coordination. | Used by OCA and the single runtime. |
| `yusp-plus-extend/yusp-plus-utrace-core` | `cn.com.yusys.yusp.utrace.core`: modification trace entity, mapper, service, and REST API. | Used by OCA and the single runtime. |
| `yusp-plus-file/yusp-plus-file-core` | `cn.com.yusys.yusp.file`: local/company file-provider REST API, file metadata, and rich-text file persistence. | Embedded by the single starter. |
| `yusp-plus-workboard` | `cn.com.yusys.yusp.workboard`: workboard CRUD plus example ESB, HTTP-client, and FTP endpoints/configuration. | Embedded by the single starter; activated through profile `workboard`. |
| `yusp-plus-dbinit` | `cn.com.yusys.yusp.dbinit`: standalone JDBC/MyBatis `ScriptRunner` database initializer and GoldenDB SQL. | Packaged separately; not a Spring Boot application. |

`yusp-plus-oca-web2.0` is a separate Vue project and is not a Maven module.

### Package conventions

The backend root package is `cn.com.yusys.yusp`. The dominant vertical layering is:

- `controller` or `web.rest`: HTTP transport;
- `service` and `service.impl`: use-case/business coordination;
- `domain.entity` or `entity`: persistence entities;
- `domain.bo`, `dto`, `form`, `query`, and `vo`: boundary- or use-case-specific models;
- `dao` or `repository.mapper`: MyBatis mapper interfaces;
- `resources/mapper/<area>/*.xml`: custom SQL;
- `config`, `interceptor`, `filter`, `handler`, `annotation`, `job`, and `utils`: cross-cutting/runtime concerns.

The single application explicitly scans the single, UAA, OCA, notice, common, file, job, trace, and workboard packages and explicitly scans their mapper packages. It also names absent `notice`, `flow`, and other package locations from the wider platform; harmless missing packages are tolerated by component scanning.

### Application entry points

- Production Spring Boot entry point: `cn.com.yusys.yusp.single.YuspPlusSingleStarterMicroserviceApp`. It enables the metrics aspect infrastructure, asynchronous proxies, component scanning, and MyBatis mapper scanning. There is no separate runnable UAA or OCA application class in this checkout.
- Standalone database entry point: `cn.com.yusys.yusp.dbinit.YuspPlusDbinitApplication`. It is a plain Java `main`, constructs `DbInitServiceImpl` directly, and does not start Spring.
- Test-only Spring Boot entry point: `cn.com.yusys.yusp.TestApplication` under the single module's test sources.
- Frontend entry point: `yusp-plus-oca-web2.0/src/main.js`, which creates the Vue instance with router, Vuex store, i18n, and YUWP UI.

## 4. Request and data flows

### Normal browser/API request

1. The Vue Axios interceptor selects the environment base URL, attaches `Authorization: Bearer ...`, wraps most POST bodies as `JClientReqEntity`-shaped `{head, body}`, and sends `Accept-Language` (`yusp-plus-oca-web2.0/src/config/interceptors/axios.js`).
2. The single runtime applies global CORS, company request crypto/replay/XSS/file filters, and `SingleSessionFilter` for `/api/*`. The filter decodes the JWT, constructs `UserContext`, and checks the access token's Redis record. Configured ignore lists bypass selected endpoints.
3. Company access-control and data-authority starters are enabled by configuration. Their implementations are external dependencies, so only their configuration and expected role are visible here.
4. A `@RestController` under `/api/...` unwraps `JClientReqEntity<T>`, optionally applies Bean Validation, and delegates to a service.
5. Services use MyBatis-Plus CRUD/query wrappers and custom mapper XML. Multi-table writes commonly use method-level `@Transactional(rollbackFor = Exception.class)`.
6. MyBatis meta-object handling populates audit fields. The tenant interceptor adds `data_tenant_id` conditions except for a five-table ignore set.
7. Responses normally use `JClientRspEntity.buildSuccess/buildFail`; the frontend maps `head.retCode`, `head.retMsg`, and `body` to its client result.

### Authentication/token flow

1. `/oauth2/token` is handled by Spring Authorization Server and the custom `oca` grant converter/provider.
2. The OAuth client is loaded from `oauth2_registered_client` through `JdbcRegisteredClientRepository`.
3. Optional captcha state is checked and consumed from Redis.
4. `OcaUserDetailsServiceImpl` delegates to the single module's `SingleLoginUserInfoImpl`, which calls OCA login/password or third-party-login services.
5. OCA loads `admin_sm_user`, applies account status/deadline/login-policy checks, and validates the password using request replay/decryption support plus BCrypt.
6. UAA signs a JWT from the classpath JKS key pair, returns access/refresh tokens, and records token presence and expiry in Redis. Login exclusivity can invalidate older access tokens.
7. Later API requests require both a valid JWT signature and a matching live Redis token entry. Logout removes the Redis access-token state.

### Scheduled job flow

1. `ScheduleJobServiceImpl` reads `schedule_job` at startup and reconciles those rows with Quartz triggers.
2. Quartz uses the application `DataSource`, `QRTZ_` tables, a 20-thread pool, clustering, and a 30-second startup delay.
3. A trigger executes `ScheduleJob`, which resolves the configured Spring bean name and reflectively invokes `run(String)`.
4. OCA supplies `ITask` beans for cache loading, organization sequence updates, and log cleanup; every execution writes `schedule_job_log`.

### File and cache flow

- File upload/download delegates to company `FileUtils` using a configured local-disk template, while metadata is persisted in `admin_file_upload_info` or rich-text tables. Multi-file download is assembled in memory as ZIP bytes.
- Redis is the default custom cache for dictionaries, authorization trees, login policy counters, captcha, and OAuth token state. Several job beans bulk-load database values into tenant-specific Redis hashes.
- The cache-dependency starter is configured to associate selected administration tables with cached values.

### Exception and response flow

- `OcaExceptionHandler` has the highest advice order and handles `BizException`. It resolves localized error text from Redis, can reload message data from the database on a cache miss, and returns a failed `JClientRspEntity`.
- `PlusExceptionHandler` then supplies fallback handling for `BizException`, XSS errors, body validation, constraint violations, workflow-by-class-name errors, and all other exceptions. Generic exceptions are logged and mapped to code `500` with a generic Chinese message.
- These advice methods return response envelopes without setting an HTTP error status, so business/validation/generic failures normally remain HTTP 200 unless another layer changes the status. UAA authentication handlers and token/logout controllers are exceptions and use OAuth failure handlers or explicit `ResponseEntity` statuses.
- Some controllers catch exceptions themselves and return `buildFail`, while file/ESB endpoints sometimes return streams, XML, strings, or `void`; error behavior is therefore not completely uniform.

## 5. Persistence model

### Access mechanism

- MyBatis-Plus is the primary ORM/data mapper: 39 interfaces extend `BaseMapper`, and service implementations commonly extend `ServiceImpl`.
- There are 33 mapper XML files for custom joins, projections, batches, and reports.
- UAA also uses Spring `JdbcTemplate` for Spring Authorization Server's registered-client repository.
- 38 Java classes carry `@TableName` (including a BO and a VO mapped to existing tables). UUID assignment is common; schedule IDs are input-generated longs and the workboard uses an auto-increment ID.
- `BaseEntity` supplies `lastChgUsr`, `lastChgDt`, and `dataTenantId`; `MybatisPlusConfig` fills audit fields on insert/update.
- The global configuration names logical-delete values, but no active `@TableLogic` annotation was found. Existing services call physical remove/delete operations. **Uncertain:** an external starter could add a global logical-delete field, but no such field is configured in this checkout.

### Schema domains

The GoldenDB script creates 67 tables, each preceded by `DROP TABLE IF EXISTS`, and contains seed data. Major groups are:

- 41 `admin_sm_*` tables: users, organizations, departments, institutions, tenants, roles, duties, menus, functional modules, business functions, control points, data-authorization templates/records, notices, messages, dictionaries, properties, audit/login/password logs, file metadata, and relationship tables;
- 11 `qrtz_*` Quartz tables plus `schedule_job` and `schedule_job_log`;
- `oauth2_registered_client` for OAuth client configuration;
- six `message_*` tables for a database message/subscribe model;
- `s_modify_trace`, `oca_workboard`, `sequence`, `sequence_config`, `admin_file_upload_info`, and `oca_test`.

The documented domain relationships are user-to-role many-to-many, user-to-department/duty organizational assignments, user-managed organizations, and business-function-to-menu/control/data-authority relationships (`yusp-plus-oca/README.md`). The DDL contains only five foreign-key declarations, so much referential integrity is enforced in application queries/services rather than by database constraints.

### Transactions and migration mechanism

- 81 method-level `@Transactional` annotations occur across 27 service implementation classes; all specify rollback for `Exception`, sometimes also `RuntimeException`. No controller transaction was found.
- Read methods generally have no explicit read-only transaction. Three methods explicitly set `readOnly = false`.
- No Flyway or Liquibase usage exists. The README instructs operators to execute `yusp-plus-dbinit/src/main/resources/sql/goldendb/oca-init-20250915.sql` manually.
- The standalone initializer searches for scripts whose filenames end in `ddl.sql`, then `dml.sql`. The only checked-in SQL filename does not match that convention. Therefore the current combined script is not selected by `DbInitServiceImpl`; this is verified code/configuration drift, not a migration-runner guarantee.

## 6. Integration points

| Integration | Direction and evidence |
|---|---|
| GoldenDB | Primary JDBC persistence. The runtime uses `com.goldendb.jdbc.Driver`, HikariCP, and a MySQL-style JDBC URL. |
| Redis | Bidirectional state/cache dependency for tokens, captcha, login policy, sessions, dictionaries, and authorization-related caches. Required by the active configuration. |
| TongWeb | Embedded/container runtime starter and classpath license configuration; backend Docker image uses an internal Java 17 base. |
| Local disk/SFTP/company file service | File upload/download through company file starters and `FileUtils`; configured local paths are under `/home/nginx/temp`. |
| Aliyun OSS | Controller generates a browser upload policy and service deletes objects. Endpoint and bucket are hard-coded; credentials are empty in source, so the feature is not operational without code/configuration change. |
| ESB | `EsbController` sends an XML web-service-style request to a configured internal ESB URL and exposes a provider endpoint that parses/builds ESB XML. |
| Generic HTTP | Workboard exposes synchronous and asynchronous HTTP-client examples targeting `example.com`. Two plain `RestTemplate` beans exist but no call site was found. |
| FTP | Workboard exposes single and concurrent upload examples through a pooled FTP starter; checked-in settings are placeholders. |
| Frontend | Vue client calls the backend, handles the common response envelope, token refresh/logout behavior, localization, optional crypto, and dynamic route/menu state. |
| Internal artifact infrastructure | Maven distribution and frontend dependency configuration point to company Artifactory/GitLab/Harbor endpoints. |

No Kafka, RabbitMQ, RocketMQ, JMS, Spring Cloud Stream, or other application message producer/consumer was found. Rabbit auto-configuration is explicitly excluded. The `message_*` schema and message-subscription controller are database-backed application features, not evidence of a broker. No Feign or SOAP framework client was found; the ESB starter handles XML over its own HTTP service abstraction.

## 7. Security model

### Verified controls

- Spring Authorization Server issues signed JWT access tokens using an RSA key pair loaded from `keystore.jks`.
- OAuth clients are database-backed. The custom `oca` grant validates username/password and optional captcha; refresh-token generation is supported by the token generator/provider code.
- Redis presence is authoritative for active access/refresh tokens and enables logout, sliding expiry, and optional single-login invalidation.
- Passwords are BCrypt encoded. User entities hide password and some policy fields from JSON.
- `SingleSessionFilter` populates the company `UserContext` before downstream access control.
- Company starters enable functional access control and data-authority filtering; control-point URLs and data-authority mappings are administered by OCA.
- MyBatis tenant filtering uses `data_tenant_id`; schedule, message, sequence, and tenant tables are explicitly exempted.
- Request encryption is enabled with SM2/SM4, and nonce/replay protection is enabled with a 300-second window. Per-control-point checks select which URLs require encoding/nonce behavior.
- XSS/access/file-upload filters are enabled; upload extensions are allow-listed.
- Sensitive response fields can be masked with the custom `@JsonEncrypt` serializer.
- CSRF and Spring Security CORS handling are disabled in the authorization filter chain, while a separate global CORS filter permits all origin patterns, headers, and methods with credentials.

### Security boundary caveats

`WebSecurityCustomizer` ignores `/api/**`, so those endpoints bypass Spring Security's filter chain and rely on servlet filters plus external company access/data-authority filters. `/oauth2/*` remains in the authorization-server chain. The configured API whitelist includes token/login support, file download, Swagger/Knife4j, health, all `/api/tc/**` workboard integration examples, and other selected routes.

**Uncertain:** the exact authorization decision algorithm, role/control-point matching, and data-authority SQL rewriting live in external `yusp-commons-*` dependencies and cannot be fully audited from this repository.

## 8. Testing strategy

### What exists

- 11 backend test files contain 82 active JUnit 5 test methods. They are concentrated in OCA/notice and two single-module classes.
- Tests primarily use Mockito with direct controller/service invocation. Two controller suites use standalone MockMvc.
- The active tests are unit/slice-style; no active `@SpringBootTest`, database integration test, container test, security filter-chain test, Quartz test, or external-integration test was found.
- The common module's only test class is fully commented out. No active tests exist in UAA, job, trace, file, workboard, or database-init modules.
- The test runtime POM supplies Spring Boot Test, MyBatis-Plus test support, H2, and TestNG, but current active tests use JUnit 5/Mockito. TestNG usage was not found.
- The single module's test `application.yml` points to network GoldenDB and Redis instances rather than an isolated H2/test profile, although the current unit tests do not start the Spring context.
- The Vue project has five Jest spec files with 23 test cases for two components and date/time/validation utilities.

### Conventions

- Backend classes use the `*Test` suffix and behavior-oriented method names in newer tests, for example `getViewList_ValidInput_ReturnsExpectedOutput`.
- Mockito uses `@Mock`/`@InjectMocks`; both `MockitoExtension` and manual `MockitoAnnotations` initialization are present.
- Controller tests assert the common `head.retCode`/`body` response contract.
- Surefire supplies the two required `--add-opens` flags for tests.

## 9. Coding conventions

- Java code is UTF-8, rooted at `cn.com.yusys.yusp`, normally uses four-space indentation, `PascalCase` types, and `camelCase` members. Some older job classes use tabs, so formatting is not completely uniform.
- Interfaces are named `*Service`; implementations are `*ServiceImpl`; persistence interfaces are usually `*Dao` (file uses `*Mapper`); persistence types are `*Entity`; transport/use-case models use `Bo`, `Dto`, `Form`, `Query`, and `Vo` suffixes.
- Controllers are action-oriented rather than strictly REST-resource-oriented. Almost every route is under `/api`; the snapshot has 254 `@PostMapping` uses, six `@GetMapping` uses, and no `@PutMapping`/`@DeleteMapping` uses. Create/update/delete are generally POST subpaths.
- Most JSON APIs use company `JClientReqEntity<T>` and `JClientRspEntity<T>` envelopes. File streams, OAuth/token endpoints, error handlers, ESB provider XML, and a few demo endpoints are exceptions.
- Bean Validation is applied selectively, including `Insert`/`Update` validation groups. Only 14 controller files visibly apply `@Valid`/`@Validated`; it is not a universal boundary convention in the existing code.
- Transaction annotations belong to service methods, not controllers.
- Custom SQL belongs under `src/main/resources/mapper/...` beside a DAO/mapper interface.
- Business errors use `BizException` and an error-code/i18n lookup model. Chinese and English message bundles and internationalized table variants are present.
- Existing code is dominated by field injection (279 `@Autowired` occurrences), with constructor injection appearing in newer/configuration code. Repository governance requires constructor injection for new or materially changed components; this describes the forward convention, not the legacy baseline.
- No Java formatter, Checkstyle, PMD, SpotBugs, JaCoCo, or Maven Enforcer configuration was found.
- Frontend `.editorconfig` requires UTF-8, two spaces, LF, final newlines, and trimmed trailing whitespace. ESLint uses Vue recommended and camel-case rules, with many legacy relaxations.

## 10. Operational constraints

- Building requires Java 17, Maven, and access to internal Maven artifacts inherited from `yusp-commons-dependencies`; the release repository URL is an internal HTTP Artifactory address.
- The running monolith requires GoldenDB/JDBC, Redis, a TongWeb license, writable log/temp/file directories, the JKS key store, and the active `workboard` integration configuration. Quartz startup also requires its database tables.
- The default backend configuration is environment-specific rather than portable: it contains fixed network endpoints, credentials, absolute Linux paths, and an always-active `workboard` profile. There are no checked-in `application-dev/test/prod` backend variants.
- `spring.main.allow-bean-definition-overriding` and `allow-circular-references` are enabled, permitting legacy bean conflicts/cycles.
- The server defaults to port 9009. Multipart request/file limits are 10 MB; the OSS policy separately permits up to roughly 1 GB. Hikari is configured with only two connections.
- Actuator enables all endpoints internally but exposes only `health`; health details are always shown. Swagger/Knife4j is enabled.
- Logging goes to `../udp-logs/<application>/appLogs` and includes MDC trace/span placeholders. The custom controller metrics aspect is dormant unless `application.metrics.enable=true`; that property is absent from the checked-in application configuration. Prometheus support is on the classpath but its endpoint is not exposed by the current management configuration.
- Quartz is configured for clustering, 20 worker threads, JDBC locks, and a 30-second delayed startup. Job invocation is reflective and depends on database `bean_name` values matching Spring bean names with a `run(String)` method.
- Root Docker packaging expects `target/yusp-plus-single-starter.jar` and an internal Java 17 image. Runtime tuning/configuration is passed through `JAVA_OPTS` and `RUN_ARGS`.
- Backend shell scripts expect an assembled `conf/lib/bin` layout. The checked-in `run.sh` contains ParNew/CMS JVM flags removed from Java 17 and does not itself add the README-required module-open flags; it is therefore not aligned with the declared runtime without external overrides or script repair.
- The Vue build requires Node/Yarn and internal npm packages. Its lockfile is intentionally ignored, reducing dependency reproducibility. Frontend production packaging uses an internal Nginx image; Kubernetes YAML is a placeholder-substituted one-replica NodePort deployment.
- The only Jenkinsfile is frontend-specific: it checks out from GitLab, runs Sonar, builds a ZIP, and deploys by SSH. No backend CI pipeline definition was found.

## 11. Known risks and technical debt

### High confidence / directly evidenced

1. **Secrets in source control.** Main and test YAML/properties contain database/Redis credentials and request-decryption private key material; JKS/license files are committed. Values are not repeated here. This conflicts with the repository constitution and creates credential-rotation/exposure risk.
2. **Very broad API security bypass.** Spring Security ignores `/api/**`; protection depends on custom/external servlet filters and large string-based allowlists. The allowlist makes all `/api/tc/**` demo integration endpoints unauthenticated at the token filter/data-authority layers.
3. **Permissive CORS.** Credentials are allowed with every origin pattern, header, and method.
4. **Public sensitive surfaces.** File download is whitelisted; health details are always shown; API documentation is enabled and whitelisted. Whether file IDs are unguessable or downstream access controls compensate is **uncertain**.
5. **Non-isolated configuration/tests.** Runtime and test configuration reference fixed infrastructure. There is no backend environment-profile separation, and no active integration-test harness validates database, Redis, security, migrations, Quartz, or external adapters.
6. **Migration drift and destructiveness.** No versioned migration framework exists. The sole SQL script drops all 67 tables before recreation, while the Java initializer ignores its filename. Operational use is manual and destructive.
7. **Java 17 deployment-script drift.** Legacy CMS/ParNew JVM flags and missing explicit module-open flags conflict with the declared Java 17 startup requirements.
8. **Demo/incomplete integrations in the production scan.** Workboard HTTP/FTP/ESB controllers are component-scanned and `/api/tc/**` is whitelisted. HTTP and FTP values are examples/placeholders. OSS credentials are empty and endpoint/bucket values are hard-coded.
9. **Limited automated quality gates.** No coverage threshold or Java static-analysis plugin exists. The Maven release plugin explicitly skips tests; the only Jenkinsfile covers the frontend.
10. **Thin test distribution.** UAA, job, trace, file, workboard, database initialization, filter/security behavior, persistence, and external integrations have no active tests.
11. **Legacy dependency/build reproducibility.** Dependency versions are largely controlled by an internal external parent; internal Artifactory/Harbor access is required; there is no Maven wrapper; the frontend lockfile is ignored; distribution uses HTTP.
12. **Large legacy coupling.** Bean overriding and circular references are enabled, and field injection is pervasive. OCA core contains most domains and cross-cutting behavior, making change impact broad.
13. **Database/application consistency around external side effects.** Some transactions coordinate database writes with Quartz scheduling or file deletion, but those external actions do not participate in the database transaction. Partial success is possible if a later step fails.
14. **In-memory file aggregation.** Multi-file download reads complete files and the final ZIP into heap memory, making resource usage scale with total download size.
15. **Potential sensitive request/response logging.** If `application.metrics.enable` is turned on, the metrics aspect serializes all non-multipart controller arguments and return values. It has no visible field-level redaction before logging.

### Drift or uncertainty requiring confirmation

- README material describes modules, paths, OAuth modes, and microservice deployment variants not present in this checkout. Treat it as historical unless source/configuration confirms it.
- The `release` Spring Boot plugin includes only a non-existent `nothing` artifact. It may intentionally create a thin artifact for an external assembly, but no bound assembly plugin proves that packaging path.
- MySQL and Oracle dependencies remain in `yusp-plus-dbinit`, but only a GoldenDB property set and GoldenDB SQL directory exist. Current multi-database support is therefore unproven.
- Prometheus is a dependency, but only health is exposed; no scrape endpoint or deployment scrape configuration was found.
- Data-authority, access filtering, session semantics, file backends, ESB transport, and several response wrapper types come from closed/internal dependencies. Their failure behavior and guarantees cannot be established solely from this repository.

## 12. Commands for building and testing

Run from the repository root unless noted.

### Backend

```bash
# Full reactor compile and tests
mvn clean verify

# Focused module plus required reactor dependencies
mvn -pl yusp-plus-oca/yusp-plus-oca-core -am test
mvn -pl yusp-plus-oca/yusp-plus-oca-core -am verify

# Build the runnable monolith
mvn -pl yusp-plus-single/yusp-plus-single-starter -am package

# Start through Spring Boot (requires configured GoldenDB, Redis, TongWeb license,
# file paths, workboard dependencies, and the Java module-open flags noted in README.md)
mvn -pl yusp-plus-single/yusp-plus-single-starter -am spring-boot:run

# Optional POM profiles defined by the starter
mvn -pl yusp-plus-single/yusp-plus-single-starter -am -Pboot package
mvn -pl yusp-plus-single/yusp-plus-single-starter -am -Prelease package

# Build the standalone database initializer artifact; do not run it against a
# populated database without reviewing the destructive SQL and filename mismatch
mvn -pl yusp-plus-dbinit -am package
```

The root README says the GoldenDB SQL should currently be executed manually. Because it begins with `DROP TABLE IF EXISTS` for every table, it is initialization/destructive reset material, not a safe incremental migration.

### Frontend

```bash
cd yusp-plus-oca-web2.0
yarn install
yarn dev
yarn lint
yarn test:unit
yarn build
```

### Repository documentation utility

```bash
# Regenerates the database schema reference from the GoldenDB script
ruby yusp-plus-dbinit/scripts/generate_schema_doc.rb
```

The generated target is currently staged as deleted in the inspected worktree; this document does not alter or restore that pre-existing user change.

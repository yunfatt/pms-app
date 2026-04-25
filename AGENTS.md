# PMS Agent Guidelines

## Architecture
- **Multi-tenant Jmix 2.7.6 app**: Master PostgreSQL DB stores `User`/`AppCompany` (system entities). Per-company tenant DBs store business entities (`Customer`, `Phase`, `Property`, etc.).
- **Multi-tenancy**: `TenantContext` (ThreadLocal) set by `CompanyContextFilter` from HTTP request. `CompanyRoutingDataSource` routes queries to tenant DB.
- **UI**: Vaadin Flow views via Java `@ViewController` + XML descriptors. No REST API; communication via Vaadin RPC.
- **Schema**: `@DdlGeneration(DbScriptGenerationMode.DISABLED)` on entities; all changes via Liquibase changelogs in `src/main/resources/com/company/pmsmain/liquibase/changelog.xml`.

## Entity Patterns
- Business entities: `@JmixEntity`, `@Store(name = "tenant")`, `@Table(name = "entity")`.
- Composite keys: `<Entity>CompKey` with `@EmbeddedId` (e.g., `compno` + entity-specific field).
- Examples: `Vendor.java`, `VendorCompKey.java`.

## View Patterns
- List views extend `StandardListView<Entity>`, XML in `resources/view/entity/entity-list-view.xml`.
- Search: `TypedTextField<String> searchField` + `searchButton`, triggers on Enter/clear. `advancedSearchToggle` switches to `GenericFilter`.
- Print: Button opens PDF/Excel dialog, calls `exportReport()` with `StiReport` from `.mrt` template.
- `buildReport()`: Gets `TenantContext.getCompanyCode()`, loads `AppCompany`, builds PostgreSQL connection string, sets variables (`SystemName`, `CompanyName`, `UserId`, entity ranges).
- Route: lowercase plural (e.g., `phases`).
- Examples: `PhaseListView.java`, `phase-list-view.xml`.

## Reporting
- Stimulsoft Reports: Templates in `src/main/resources/com/company/pmsmain/Reports/` (dev) or `/app/reports` (prod).
- `.mrt` format: `StiSerializer version="1.02"`, `StiPostgreSQLDatabase` "pms" (connection injected), `StiPostgreSQLSource` named after table, variables for ranges.
- Load via `ClassPathResource` (dev) or `Paths.get()` (prod).

## Key Packages
- `entity/`: JPA entities.
- `view/`: Vaadin views (Java + XML pairs).
- `multicompany/`: Routing datasource, context, filter, registry.
- `security/`: Spring Security, roles.
- `listener/`: Entity listeners (e.g., auto-numbering).

## Commands
- **Run dev**: `./gradlew bootRun` (port 8100).
- **Build JAR**: `./gradlew bootJar -Pprod`.
- **Tests**: `./gradlew test`.
- **GENVIEW <Entity>**: Generates 4 files matching `PhaseListView` structure.
- **DEPLOY**: Builds Docker image, transfers to server, deploys via `docker compose`.

## Dependencies
- Jmix 2.7.6 BOM.
- PostgreSQL driver.
- Stimulsoft Reports 2026.1.7.
- Spring Boot Web.

## Conventions
- Use `UnconstrainedDataManager` for cross-tenant queries.
- `@Value("${app.reports.path}")` for template paths.
- Always check `TenantContext.getCompanyCode()` for tenant-specific logic.

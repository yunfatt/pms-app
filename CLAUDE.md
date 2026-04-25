[# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Start development server (port 8100)
./gradlew bootRun

# Build JAR
./gradlew build

# Build with Vaadin production optimizations
./gradlew bootRun -Pprod

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests com.company.pmsmain.user.UserTest
```

**Prerequisites:** Java 17 or 21, PostgreSQL running locally with databases `pms-main` (master) and `pms76` (tenant). Default dev credentials are `admin`/`admin`.

## Architecture Overview

This is a **Jmix 2.7.6 + Spring Boot** Property Management System (PMS) with a multi-tenant, multi-company architecture.

### Multi-Tenancy Pattern

The app uses two PostgreSQL stores:

- **Master store** (`pms-main` DB): Stores system entities — `User`, `AppCompany`, security roles. `AppCompany` holds per-tenant DB connection info (host, port, DB name, credentials).
- **Tenant store** (one DB per company, e.g., `pms76`): Stores business entities — `Customer`, `Invhdr`/`Invdtl`, `Phase`, `Property`, `Ardoc`, `Bank`, `Charge`.

At startup, `TenantSchemaInitializer` reads all `AppCompany` records, creates a `DataSource` for each, and runs Liquibase migrations on each tenant DB. At runtime, `CompanyContextFilter` extracts the company from the HTTP request and sets `TenantContext` (ThreadLocal), which `CompanyRoutingDataSource` uses to route queries.

### UI Layer

Views are defined as **Java classes** (`@ViewController`) paired with **XML descriptors** (`.xml` files in `resources`). The XML declares components, data loaders, and bindings. Vaadin Flow bridges these to browser-side Web Components (React + Lit). There is no separate REST API — all UI/server communication is via Vaadin's RPC protocol.

`BaseListView.java` is an abstract base class that all list views extend for common styling and title handling.

### Key Packages

| Package | Purpose |
|---|---|
| `entity/` | JPA entities (`@JmixEntity`). Business entities go to tenant store; `AppCompany`/`User` go to master store. |
| `view/` | Vaadin Flow views (Java + XML pairs). One sub-package per entity/feature. |
| `multicompany/` | All multi-tenancy infrastructure: routing datasource, HTTP filter, tenant context, company registry. |
| `tenant/` | Jmix data store configuration for tenant DBs and schema initialization. |
| `security/` | Spring Security config, RBAC roles (`FullAccessRole`, `UiMinimalRole`), user repository. |
| `listener/` | Entity listeners — e.g., `DocumentNumberGenerator` for auto-numbering. |

### Schema Management

Liquibase handles migrations for both stores. The master changelog is at `src/main/resources/com/company/pmsmain/liquibase/changelog.xml`. DDL generation on entity changes is **disabled** (`@DdlGeneration(DbScriptGenerationMode.DISABLED)`); all schema changes must go through Liquibase changelogs.

### Reporting

Stimulsoft Reports (`stimulsoft-reports-report:2026.1.7`) is used for document generation. Report templates live in `src/main/resources/com/company/pmsmain/Reports` (dev) or `/app/reports` (prod volume mount).

### Production Configuration

Production uses `application-prod.properties` activated via the `prod` Spring profile. It reads DB credentials from environment variables (`MAIN_DATASOURCE_URL`, `MAIN_DATASOURCE_USERNAME`, `MAIN_DATASOURCE_PASSWORD`) and sets Vaadin to production mode. The Docker image uses Liberica OpenJDK 21 with fonts pre-installed for reporting.

## GENVIEW Command

When I type `GENVIEW <EntityName>`, generate these 4 files:

### 1. `<Entity>ListView.xml`
- Match phaselist-view.xml structure exactly
- Search bar: searchField (TypedTextField) + searchButton +
  advancedSearchToggle + genericFilter (hidden by default)
- DataGrid with resizable columns
- Print button in buttonsPanel
- JPQL loader with searchText parameter
- Route: lowercase plural of entity name

### 2. `<Entity>ListView.java`
- Match PhaseListView.java structure exactly
- searchField triggers on Enter key and on clear
- advancedSearchToggle switches simple/advanced filter mode
- Print button opens PDF/Excel format dialog
- exportReport() builds StiReport from <entity>.mrt
- buildReport() uses TenantContext.getCompanyCode(), loads AppCompany,
  sets StiPostgreSQLDatabase connection string, sets variables:
  SystemName, CompanyName, UserId, and entity-specific range variables
- @Value("${app.reports.path}") for template path
- @Value("${app.system-name}") and @Value("${app.company-name}")
- Inject: UnconstrainedDataManager, CurrentAuthentication,
  Notifications, Downloader

### 3. `<Entity>DetailView.xml` + `<Entity>DetailView.java`
- Standard Jmix detail view
- FormLayout with fields grouped logically by category
- All fields from entity class

### 4. `<entity>.mrt`
- StiSerializer version="1.02" — match pm_custtrxn.mrt format exactly
- StiPostgreSQLDatabase named "pms", connection string empty (injected at runtime)
- StiPostgreSQLSource named "vendor" (or entity table name)
- Variables: SystemName, CompanyName, UserId, plus range variables
- Bands: PageHeaderBand, PageFooterBand, HeaderBand, DataBand, ReportSummaryBand
- Blue Steel style collection
- ReportUnit: Inches, A4 Landscape (PageWidth=11.69, PageHeight=8.27)
- Margins: 0.39,0.39,0.39,0.39
- All component element tags use "pms" as tag name: <pms Ref="N" ...> </pms>
- Script block and ReferencedAssemblies block required
- n tag (lowercase) for all component names, not Name tag

### Constraints
- @DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
- @Store(name = "tenant") on entity
- TenantContext.getCompanyCode() for multi-tenancy
- Composite key pattern: <Entity>CompKey with @EmbeddedId

### Reference files (read before generating)
- src/main/resources/**/phaselist-view.xml
- src/main/java/**/PhaseListView.java
- src/main/resources/**/Reports/pm_custtrxn.mrt
- src/main/java/**/entity/Vendor.java
- src/main/java/**/entity/key/VendorCompKey.java
- src/main/java/**/view/vendor/VendorListView.java
- src/main/resources/**/view/vendor/vendor-list-view.xml
- src/main/resources/**/Reports/supplierlist.mrt]()

## DEPLOY Command

When I type `DEPLOY`, execute the full deployment pipeline below step by step,
confirming success at each phase before proceeding.

### Phase 1 — Build (Windows PC)

Run these commands in the IntelliJ Terminal from the project root:

**Step 1 — Package the JAR:**
```bash
./gradlew clean bootJar -Pprod
```

**Step 2 — Ensure Dockerfile exists in project root.**
Create it if missing with this exact content:
```dockerfile
FROM bellsoft/liberica-openjdk-debian:21
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Step 3 — Build the Docker image:**
```bash
docker build -t pms-app:latest .
```

**Step 4 — Export image to tar:**
```bash
docker save -o pms-app.tar pms-app:latest
```

### Phase 2 — Transfer (Windows → Zorin OS)

**Step 5 — Transfer the image:**
```bash
scp pms-app.tar zorinpc@192.168.0.159:/home/zorinpc/
```

**Step 6 — Transfer docker-compose.yml:**
```bash
scp docker-compose.yml zorinpc@192.168.0.159:/opt/pms-app/
```

### Phase 3 — Launch (Zorin Server)

SSH into the server and run:

**Step 7 — Load the image:**
```bash
sudo docker load -i /home/zorinpc/pms-app.tar
```

**Step 8 — Start the stack:**
```bash
cd /opt/pms-app
sudo docker compose up -d
```

### Phase 4 — Verify

**Step 9 — Check all containers are running:**
```bash
sudo docker compose ps
```

**Step 10 — Tail logs to confirm startup:**
```bash
sudo docker compose logs -f jmix-app
```

**Step 11 — Confirm app is reachable at:** `http://192.168.0.159:8080`

### DEPLOY Constraints
- Always run Phase 1 fully before Phase 2
- If `docker build` fails, check that `build/libs/*.jar` exists first
- If `scp` fails, verify SSH access: `ssh zorinpc@192.168.0.159`
- If `docker compose up` fails, run `sudo docker compose logs jmix-app`
  and report the last 50 lines
- Never skip the verify phase — confirm HTTP 200 before declaring success
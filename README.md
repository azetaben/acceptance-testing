# Acceptance Testing Framework (SauceDemo)

GitHub repository: `https://github.com/azetaben/acceptance-testing`

## Why this framework exists

This project is a **Java-based acceptance test automation framework** for validating user journeys on SauceDemo (
`https://www.saucedemo.com`) using:

- **Selenium 4** for browser automation
- **Cucumber 7** for business-readable BDD scenarios
- **TestNG** for orchestration and suite control
- **Maven** for dependency/build/run lifecycle

### Importance of this framework

- Converts business-critical flows (login, cart, checkout, e2e) into executable specifications.
- Reduces release risk by catching regressions early in CI/CD.
- Improves collaboration across QA/Dev/Product through Gherkin features.
- Produces multiple report types for fast triage and auditability.

## Framework nature and design

This is a **hybrid BDD + POM + utility-driven framework**:

- **BDD layer**: feature files and Cucumber step definitions
- **Page Object layer**: page classes encapsulating UI actions/selectors
- **Execution layer**: Cucumber TestNG runner classes and TestNG XML suites
- **Cross-cutting utilities**: WebDriver lifecycle, waits, config, logging, performance/report helpers

## High-level structure

```text
acceptance-testing/
├── src/test/java/com/saucedemo/
│   ├── pages/                 # Page Object Model classes
│   ├── steps/                 # Cucumber step definitions
│   ├── runners/               # Cucumber + TestNG runners
│   ├── webdriverutilities/    # Driver lifecycle/config
│   ├── utils/                 # Helpers (files, perf, etc.)
│   └── constants/             # Constants (RunnerConstants, etc.)
├── src/test/resources/
│   ├── features/              # Gherkin feature files
│   ├── config/                # Runtime/environment configs
│   ├── extent.properties      # Extent report settings
│   └── log4j2.properties      # Logging settings
├── testNG-Cucumber/           # TestNG suite XML files
├── cucumber_report/           # Cucumber HTML report output
├── extent-reports/            # Timestamped Extent report outputs
├── logs/                      # Runtime logs
├── report/                    # Performance/lighthouse JSON outputs
├── docker-compose.yaml        # Selenium Grid + Elastic/Kibana stack
└── pom.xml                    # Maven dependencies/profiles/plugins
```

## Core components brief explanation

- **Feature files** (`src/test/resources/features`): business-readable scenarios.
- **Step definitions** (`src/test/java/com/saucedemo/steps`): binds Gherkin to Java logic.
- **Page Objects** (`src/test/java/com/saucedemo/pages`): reusable, maintainable UI abstraction.
- **Hooks** (`com.saucedemo.steps.Hooks`): scenario setup/teardown, screenshots, lifecycle.
- **PageManager/WebDrv**: object caching and browser session management.
- **Runners** (`src/test/java/com/saucedemo/runners`): tag/scope-specific execution entrypoints.
- **TestNG suites** (`testNG-Cucumber/*.xml`): suite-level orchestration for CI/profile runs.

## Setup

### Prerequisites

- Java 17+
- Maven 3.8+
- Git
- Chrome/Firefox installed
- (Optional) Docker Desktop for Selenium Grid + Elastic/Kibana stack

### Clone

```powershell
git clone https://github.com/azetaben/acceptance-testing.git
Set-Location acceptance-testing
```

### Optional infrastructure (Grid + Elastic + Kibana)

```powershell
docker-compose up -d
```

Stop services:

```powershell
docker-compose down
```

### Build/compile

```powershell
mvn clean test-compile
```

## How to execute tests (Maven)

### 1) Run all tests

```powershell
mvn test
```

### 2) Run by runner class (`com/saucedemo/runners`)

Examples:

```powershell
mvn "-Dtest=RegressionRunner" test
mvn "-Dtest=SmokeRunner" test
mvn "-Dtest=E2ERunner" test
mvn "-Dtest=MyAdd2CartRunnerTest" test
mvn "-Dtest=CheckoutRunner" test
```

### 3) Run by Cucumber tag

```powershell
mvn "-Dtest=E2ERunner" "-Dcucumber.filter.tags=@complete_order" test
mvn "-Dtest=RegressionRunner" "-Dcucumber.filter.tags=@regression and not @wip" test
```

### 4) Run by Maven profile (from `pom.xml`)

```powershell
mvn test -PRegression
mvn test -PSanityTest
mvn test -PSmoke
mvn test -PPerformanceTests
mvn test -PcheckoutProcessTests
mvn test -PLoginTests
mvn test -PCucumberTests
```

> Note: profile `ErrorValidationTests` currently references `testNG-Cucumber/errorValidationTests.xml` in `pom.xml`. In
> this repository, the suite file present is `testNG-Cucumber/errorValidation.xml`.

### 5) Run a specific TestNG suite XML directly

```powershell
mvn "-Dsurefire.suiteXmlFiles=testNG-Cucumber/smoke-testng.xml" test
mvn "-Dsurefire.suiteXmlFiles=testNG-Cucumber/regression-testng.xml" test
mvn "-Dsurefire.suiteXmlFiles=testNG-Cucumber/checkoutProcessTests.xml" test
```

## CI-ready execution matrix (Jenkins + GitHub Actions)

Use this matrix to standardize CI jobs regardless of platform.

| Use case                | Jenkins parameters                   | GitHub Actions inputs/env              | Copy-paste Maven command                                                               |
|-------------------------|--------------------------------------|----------------------------------------|----------------------------------------------------------------------------------------|
| Default full run        | `MAVEN_PROFILE=''`, `BROWSER=chrome` | `maven_profile: ''`, `browser: chrome` | `mvn -B -ntp clean test -DbrowserType=chrome`                                          |
| Regression suite        | `MAVEN_PROFILE=Regression`           | `maven_profile: Regression`            | `mvn -B -ntp clean test -DbrowserType=chrome -PRegression`                             |
| Smoke suite             | `MAVEN_PROFILE=Smoke`                | `maven_profile: Smoke`                 | `mvn -B -ntp clean test -DbrowserType=chrome -PSmoke`                                  |
| Sanity suite            | `MAVEN_PROFILE=SanityTest`           | `maven_profile: SanityTest`            | `mvn -B -ntp clean test -DbrowserType=chrome -PSanityTest`                             |
| Checkout process        | `MAVEN_PROFILE=checkoutProcessTests` | `maven_profile: checkoutProcessTests`  | `mvn -B -ntp clean test -DbrowserType=chrome -PcheckoutProcessTests`                   |
| Login tests             | `MAVEN_PROFILE=LoginTests`           | `maven_profile: LoginTests`            | `mvn -B -ntp clean test -DbrowserType=chrome -PLoginTests`                             |
| Cucumber suite          | `MAVEN_PROFILE=CucumberTests`        | `maven_profile: CucumberTests`         | `mvn -B -ntp clean test -DbrowserType=chrome -PCucumberTests`                          |
| Tag-filtered run        | `CUCUMBER_TAGS=@complete_order`      | `cucumber_tags: '@complete_order'`     | `mvn -B -ntp clean test -DbrowserType=chrome "-Dcucumber.filter.tags=@complete_order"` |
| Firefox run             | `BROWSER=firefox`                    | `browser: firefox`                     | `mvn -B -ntp clean test -DbrowserType=firefox`                                         |
| Compile-only (no tests) | `SKIP_TESTS=true`                    | `skip_tests: true`                     | `mvn -B -ntp -DskipTests test-compile`                                                 |
| Custom runner           | `EXTRA_MAVEN_ARGS=-Dtest=E2ERunner`  | `extra_maven_args: '-Dtest=E2ERunner'` | `mvn -B -ntp clean test -DbrowserType=chrome -Dtest=E2ERunner`                         |

> Note: `ErrorValidationTests` profile in `pom.xml` points to `testNG-Cucumber/errorValidationTests.xml`, while the
> repository currently contains `testNG-Cucumber/errorValidation.xml`.

### Jenkins job command template

```powershell
mvn -B -ntp clean test -DbrowserType=<chrome|firefox|edge> -P<optional_profile> "-Dcucumber.filter.tags=<optional_tags>" <optional_extra_maven_args>
```

### GitHub Actions workflow snippet (copy-paste)

```yaml
name: acceptance-tests

on:
  workflow_dispatch:
    inputs:
      browser:
        description: Browser type
        type: choice
        options: [chrome, firefox, edge]
        default: chrome
      maven_profile:
        description: Optional Maven profile
        type: choice
        options: ['', Regression, SanityTest, Smoke, PerformanceTests, checkoutProcessTests, LoginTests, CucumberTests]
        default: ''
      cucumber_tags:
        description: Optional Cucumber tag filter
        required: false
        default: ''
      extra_maven_args:
        description: Optional extra Maven args (example: -Dtest=E2ERunner)
        required: false
        default: ''

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
      - name: Run acceptance tests
        shell: bash
        run: |
          CMD="mvn -B -ntp clean test -DbrowserType=${{ inputs.browser }}"
          if [ -n "${{ inputs.maven_profile }}" ]; then CMD="$CMD -P${{ inputs.maven_profile }}"; fi
          if [ -n "${{ inputs.cucumber_tags }}" ]; then CMD="$CMD \"-Dcucumber.filter.tags=${{ inputs.cucumber_tags }}\""; fi
          if [ -n "${{ inputs.extra_maven_args }}" ]; then CMD="$CMD ${{ inputs.extra_maven_args }}"; fi
          echo "$CMD"
          eval "$CMD"
```

## Runners and TestNG-Cucumber suites

### `com/saucedemo/runners`

This folder contains purpose-specific entrypoints, such as:

- `RegressionRunner`, `SanityRunner`, `SmokeRunner`
- `E2ERunner`, `CheckoutRunner`, `PerformanceRunner`, `SecurityRunner`
- `MyAdd2CartRunnerTest`, `MyLoginRunnerTest`, `ParallelRunRunner`

Use these when you want tag-based, domain-based, or CI-job-specific execution control.

### `testNG-Cucumber`

This folder contains suite XML files for suite-level orchestration, including:

- `smoke-testng.xml`, `sanity-testng.xml`, `regression-testng.xml`
- `checkoutProcessTests.xml`, `loginTests.xml`, `performanceTests.xml`
- `parallel-testng.xml`, `cross-browsers-testng.xml`, `securityTests.xml`
- `cucumber-testng.xml`, `testng-firefox.xml`, `verification-helper-testng.xml`

Use these when your pipeline/team executes by TestNG suite contract.

## Reports and logs

After execution, use these outputs:

- **Extent reports**: `extent-reports/`
    - Time-stamped folders with Spark/HTML output (configured by `extent.properties`)
- **Execution logs**: `logs/automation.out`
- **Performance/lighthouse artifacts**: `report/`
- **Cucumber HTML report**: `cucumber_report/cucumber.html`
- **Additional Cucumber output**: `target/cucumber.html`, `target/cucumber.json`
- **Surefire/TestNG artifacts**: `target/surefire-reports/`

## Configuration notes

- Base app URL and default test settings: `src/test/resources/config/config.properties`
- Default browser: `browserType=chrome`
- Logging config: `src/test/resources/log4j2.properties`
- Extent config: `src/test/resources/extent.properties`

Credentials can be overridden via environment variables:

- `TEST_USERNAME`
- `TEST_PASSWORD`

## Recommended workflow

1. Write/adjust Gherkin scenarios in `src/test/resources/features`.
2. Implement/reuse step definitions in `src/test/java/com/saucedemo/steps`.
3. Keep UI logic in page objects under `src/test/java/com/saucedemo/pages`.
4. Execute via runner/profile/suite based on your use case.
5. Review `extent-reports`, `cucumber_report`, `logs`, and `report` outputs.

## Troubleshooting quick checks

- **Undefined steps**: confirm step regex text exactly matches feature steps.
- **Flaky UI timing**: prefer explicit waits in page objects.
- **Report not generated**: confirm execution completed and check `extent.properties` paths.
- **Grid issues**: verify `docker-compose` services are healthy and port `4444` is reachable.
- **Profile/suite mismatch**: validate suite filenames referenced in `pom.xml` exist under `testNG-Cucumber`.


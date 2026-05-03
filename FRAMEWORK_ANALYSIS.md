# Acceptance Testing Framework Analysis

## Tech Stack

| Technology | Version |
|---|---|
| Java | 17 |
| Maven | 3.8.1 |
| Selenium | 4.32.0 |
| TestNG | 7.11.0 |
| Cucumber | 7.22.0 |
| Lombok | 1.18.38 |
| ExtentReports | 5.1.0 |
| Log4j2 | 2.25.4 |
| Apache POI | 5.4.0 |
| JavaFaker | 1.0.2 |
| Axe-Core | 4.6.0 |

---

## Project Structure

```
acceptance-testing/
├── src/test/java/com/saucedemo/
│   ├── configReader/          # Config management
│   ├── constants/             # App & framework constants
│   ├── enums/                 # WaitStrategy, Browsers, CategoryType, etc.
│   ├── exceptions/            # Custom exception classes
│   ├── factories/             # ExplicitWaitFactory
│   ├── helperUtilities/       # 83 helper classes across 50+ packages
│   ├── pages/                 # 40 Page Object classes
│   ├── runners/               # 16 Cucumber test runners
│   ├── steps/                 # Cucumber step definitions
│   ├── webdriverutilities/    # WebDriver management & listeners
│   └── annotations/          # @FrameworkAnnotation
├── src/test/resources/
│   ├── features/              # 32 Cucumber feature files
│   ├── config.properties      # Base config
│   ├── dev/qa/uat configs     # Environment-specific configs
│   ├── extent.properties      # Reporting config
│   └── log4j2.properties      # Logging config
├── testNG-Cucumber/           # 15 TestNG XML suite configurations
├── docker-compose.yaml        # Selenium Grid + ELK stack
├── Dockerfile                 # Maven test container
├── Jenkinsfile                # CI/CD pipeline
└── pom.xml                    # Maven build config
```

**Scale:**
- 237 Java classes
- 32 Cucumber feature files
- 40 Page Object Model classes
- 83 Helper utility classes
- 15 TestNG suite XML configurations

---

## Architecture & Design Patterns

| Pattern | Implementation | Location |
|---|---|---|
| **Singleton + ThreadLocal** | Thread-safe WebDriver per test thread | `WebDrv` |
| **Factory** | Dynamic wait strategy dispatch | `ExplicitWaitFactory` |
| **Page Object Model** | 40 pages with interface segregation | `pages/` |
| **Strategy** | 12+ wait strategies via enum | `WaitStrategy` |
| **Observer** | Selenium 4 WebDriver event hooks | `DebugWebDriverListener` |
| **Registry** | Thread-safe page object caching | `PageManager` (ConcurrentHashMap) |
| **Decorator/Wrapper** | Config access with null-checking | `ConfigReader` → `FrameworkConfig` |
| **Fluent Interface** | Action chaining | `Actions` API helpers |
| **Data Provider** | Parameterized test data supply | TestNG `@DataProvider` |

---

## Core Components

### Configuration Management (`configReader/`)

Cascading priority (highest → lowest):
1. System Properties
2. Environment Variables
3. `.properties` files
4. Default values

Supports environment-specific configs: `config.properties`, `dev.config.properties`, `qa.config.properties`, `uat.config.properties`

**Key config properties:**
- Browser type, base URLs, timeouts
- Test user credentials (standard, locked_out, problem, performance_glitch users)
- Screenshot capture flags (passed/failed steps)
- Run mode (local / grid)
- Selenium Grid URL, Elasticsearch URL

---

### WebDriver Management (`webdriverutilities/WebDrv`)

- ThreadLocal pattern for parallel test isolation
- Supports: Chrome, Firefox, Edge, Internet Explorer, Safari
- ChromeOptions configured for CI: no sandbox, 1920×1080, GPU disabled, anti-automation-detection flags
- Session reuse: navigates to URL without reopening browser when possible

---

### Wait Handling (`factories/ExplicitWaitFactory`)

Available `WaitStrategy` values:

| Strategy | Description |
|---|---|
| `CLICKABLE` | Element is clickable |
| `PRESENCE` | Element present in DOM |
| `VISIBLE` | Element visible on screen |
| `NONE` | No wait applied |
| `HANDLE_STALE_ELEMENT` | Auto page refresh on stale element |
| `URL_CONTAINS` | Wait for URL to contain string |
| `TITLE_CONTAINS` | Wait for page title to match |
| `INVISIBILITY` | Element becomes invisible |
| `VISIBILITY_OF_ALL_ELEMENTS` | All elements visible |
| `ELEMENT_TO_BE_ENABLED` | Element becomes enabled |
| `PRESENCE_OF_ALL_ELEMENTS_LOCATED` | All elements in DOM |
| `ELEMENT_TO_BE_INVISIBLE` | Alias for invisibility |

Timeouts: Implicit 10s · Explicit 15s · Page Load 30s  
Page readiness: waits for `document.readyState === "complete"`

---

### Page Object Model (`pages/`)

**Concrete Pages:**
- `LoginPage`, `ProductsPage`, `CartPage`
- `CheckoutYourInformationPage`, `CheckoutOverviewPage`, `CheckoutFinishPage`

**General Page Interfaces (`pages/general/`):**
- `TypedFieldInputPage`, `MessageBelowHeadingPage`, `UserNamePage`
- `ButtonPage`, `BlankFieldPage`, `ElementPage`, `ClickableElementPage`
- `EditablePage`, `ExpandableLinkPage`, `FieldInputPage`, `ApplicationFlowPage`

**PageManager** caches initialized page objects in a `ConcurrentHashMap`. Clears cache when WebDriver instance changes. Lazy initialization via Selenium `PageFactory`.

---

### Helper Utilities (`helperUtilities/`)

| Package | Key Classes |
|---|---|
| `Actions` | `ActionClass`, `ActionHelper`, `SeleniumActions` |
| `alert` | `PopUpUrlCheckUtils` |
| `assertion` | `AssertionHelper`, `VerificationHelper`, `TextVerificationHelper` |
| `button` | `ButtonHelper`, `GenericWaitAndClickMethods` |
| `checkBox` | `CheckBoxUtils`, `CheckBoxOrRadioButtonHelper` |
| `database` | `ApplicationDBQuery` (MongoDB + PostgreSQL) |
| `date` | Date/time manipulation helpers |
| `dropdown` | Select element interaction utilities |
| `excel` | Apache POI read/write integration |
| `faker` | JavaFaker dynamic data generation |
| `file` | File upload/download helpers |
| `javaScript` | JS executor utilities |
| `json` | Jackson, Gson, SnakeYAML integration |
| `logger` | `LoggerHelper` via Log4j2 |
| `navigation` | URL navigation helpers |
| `pdf` | PDFBox 2.0.27 integration |
| `wait` | Explicit and implicit wait helpers |
| `webElement` | Generic element interaction |
| `window` | Window switching and management |

---

## Test Layer

### Cucumber Feature Files (32)

Located in `src/test/resources/features/`, organized by business flow:

```
features/
├── add-to-cart/
├── checkout-process/
├── end-to-end-and-miscellaneous/
├── login-logout/
├── login-security-checks/
└── product-display-page-with-different-sort-options/
```

### Test Runners (16)

All extend `AbstractTestNGCucumberTests`. One runner per business flow:
`LoginRunner`, `Add2CartRunner`, `CheckoutRunner`, `E2ERunner`, `RegressionRunner`, etc.

### Step Definitions

Separated by concern: `CommonSteps`, `LoginSteps`, `CartSteps`, `CheckoutSteps`, `AssertionSteps`

### Custom Annotation

```java
@FrameworkAnnotation(author = "...", category = CategoryType.REGRESSION)
```
Attaches author, tags, and category metadata to test methods for reporting.

### Data-Driven Testing

- TestNG `@DataProvider` for parameterization
- Excel files via Apache POI (`LoginExternalDataDrivenTest`)
- JSON file readers
- JavaFaker for dynamic runtime data generation

---

## Reporting

### ExtentReports 5

- Generates Spark (modern) and HTML (classic) reports
- Base64 screenshot embedding
- Author/device tracking with prefix `"Mr. B A"`
- Datetime-stamped report folders
- Screenshot dir: `extent-report/reports_/screenshot/`
- Config: `src/test/resources/extent.properties`

### Logging — Log4j2

- Rolling file appender: `logs/automation.out`
- Max file size: 10 MB with rotation
- Console + file output with timestamps and log levels
- Config: `src/test/resources/log4j2.properties`

### Additional Reports

| Report | Plugin |
|---|---|
| Cucumber HTML | Built-in |
| Cluecumber | 2.9.4 |
| Maven Cucumber Reporting | 5.11.0 |
| JUnit XML (Surefire) | For CI integration |
| TestNG HTML + Emailable | Built-in |

---

## Maven Profiles & Test Execution

### 8 Maven Profiles

| Profile | Suite |
|---|---|
| `Regression` | Full regression suite |
| `SanityTest` | Sanity checks |
| `Smoke` | Smoke tests |
| `ErrorValidationTests` | Error/validation scenarios |
| `PerformanceTests` | Performance scenarios |
| `checkoutProcessTests` | Checkout flow |
| `LoginTests` | Login scenarios |
| `CucumberTests` | All Cucumber scenarios |

### Run Commands

```bash
# By profile
mvn test -PRegression
mvn test -PSmoke
mvn test -PLoginTests

# With browser override
mvn test -PRegression -DbrowserType=firefox
mvn test -PRegression -DbrowserType=edge

# With Cucumber tag filter
mvn test -Dcucumber.filter.tags="@login"
mvn test -Dcucumber.filter.tags="@regression and @smoke"

# Parallel execution
mvn test -Dtest.threadcount=4

# Skip tests
mvn test -DskipTests=true
```

---

## Infrastructure

### Docker Compose (`docker-compose.yaml`)

| Service | Image | Port |
|---|---|---|
| Selenium Hub | selenium/hub:4.32.0 | 4444 |
| Chrome Node (latest) | selenium/node-chrome:4.32.0 | — |
| Chrome Node (v79) | Legacy version | — |
| Firefox Node | selenium/node-firefox:4.32.0 | — |
| Elasticsearch | elasticsearch:7.17.25 | 9200 |
| Kibana | kibana:7.17.25 | 5601 |

Chrome/Firefox nodes configured with 2 GB shared memory.

### Dockerfile

```dockerfile
FROM maven:3.9.9-eclipse-temurin-17
# Copies pom.xml, src/, testNG-Cucumber/
ENTRYPOINT ["mvn", "clean", "test"]
```

### Jenkinsfile — Pipeline Stages

1. **Checkout** — pull source
2. **Validate** — Maven validate
3. **Compile Test Sources** — compile
4. **Run Acceptance Tests** — execute with selected profile/browser/tags
5. **Report Summary** — archive artifacts + collect JUnit XML

**Build parameters:** Maven profile · Browser · Cucumber tags · Extra Maven args · Skip tests flag

---

## Notable Features

| Feature | Technology |
|---|---|
| Accessibility testing | Axe-Core 4.6.0 |
| CDP / DevTools | Selenium 4 (v102 + v147) |
| Database queries | MongoDB + PostgreSQL (JDBC) |
| PDF parsing | PDFBox 2.0.27 |
| Test data generation | JavaFaker |
| Multi-format data reading | Jackson · Gson · SnakeYAML · Apache POI |
| Stale element recovery | Auto page refresh in `ExplicitWaitFactory` |
| Cross-browser grid | Chrome (latest + v79) · Firefox · Edge |
| Elasticsearch indexing | ELK stack via docker-compose |
| Parallel execution | ThreadLocal WebDriver + TestNG thread count |

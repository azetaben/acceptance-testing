============================================================
# **Overview of Project acceptance-testing:**
============================================================
Technologies:

| Name                            | Version    |
|---------------------------------|------------|
| Java                            | 17         |
| Maven                           | 3.8.1      |
| TestNG                          | 7.11.0     |
| Selenium                        | 4.32.0     |
| Cucumber                        | 7.22.0     |
| Lombok                          | 1.18.38    |
| PDFBox                          | 2.0.27     |
| XChart                          | 3.8.0      |
| SnakeYAML                       | 2.0        |
| JSoup                           | 1.15.3     |
| Gson                            | 2.10.1     |
| MongoDB Java Driver             | 3.0.4      |
| Commons IO                      | 2.16.1     |
| Commons Lang3                   | 3.18.0     |
| JavaFaker                       | 1.0.2      |
| Log4j2                          | 2.25.4     |
| JSON                            | 20231013   |
| Log4j Core                      | 2.25.4     |
| Logback Classic                 | 1.5.25     |
| Log4j Slf4j Impl                | 2.25.4     |
| Apache POI                      | 5.4.0      |
| Dom4j                           | 2.1.4      |
| ExtentReports Cucumber7 Adapter | 1.14.0     |
| Cluecumber Report Plugin        | 2.9.4      |
| Plexus Utils                    | 4.0.3      |
| Awaitility Proxy                | 3.1.1      |
| PostgreSQL Driver               | 42.7.2     |
| Jackson Core                    | 2.18.6     |
| Jackson Annotations             | 2.18.6     |
| Jackson Databind                | 2.18.6     |
| Mockito All                     | 1.9.5      |
| Guava                           | 32.0.1-jre |
| ExtentReports                   | 5.1.0      |
| Maven Cucumber Reporting        | 5.11.0     |
| Axe-Core Selenium               | 4.6.0      |
| Selenium Devtools v102          | 4.4.0      |
| Selenium Devtools v147          | 4.43.0     |
| Cucumber Picocontainer          | 7.22.0     |
| Docker Compose                  | 3          |
| Selenium Hub                    | 4.32.0     |
| Selenium Node Chrome            | 4.32.0     |
| Selenium Node Firefox           | 4.32.0     |
| Elasticsearch                   | 7.17.25    |
| Kibana                          | 7.17.25    |

============================================================
## **Detailed project folders description:**
============================================================

```
acceptance-testing/
├── .idea/                        - IntelliJ IDEA project settings
│   └── inspectionProfiles/       - IntelliJ IDEA inspection profiles
├── chromedriver/                 - ChromeDriver binaries for different platforms
│   ├── linux/                    - Linux ChromeDriver
│   ├── mac/                      - macOS ChromeDriver
│   └── win/                      - Windows ChromeDriver
├── testNG-Cucumber/              - TestNG and Cucumber configuration XML files
│   ├── checkoutProcessTests.xml
│   ├── cross-browsers-testng.xml
│   ├── cucumber-testng.xml
│   ├── errorValidation.xml
│   ├── login-datadriven-testng.xml
│   ├── loginTests.xml
│   ├── pararrel-testng.xml
│   ├── performanceTests.xml
│   ├── regression-testng.xml
│   ├── sanity-testng.xml
│   ├── selenium4-feature-demo-testng.xml
│   ├── smoke-testng.xml
│   └── testng-firefox.xml
└── src/
    └── test/
        ├── resources/            - Test resources
        │   ├── config/           - Configuration files for different environments
        │   ├── features/         - Cucumber feature files
        │   │   ├── add-to-cart/                                    - Add items to cart scenarios
        │   │   ├── checkout-process/                               - Checkout process scenarios
        │   │   ├── end-to-end-and-miscellaneous/                   - E2E and miscellaneous scenarios
        │   │   ├── login-logout/                                   - Login and logout scenarios
        │   │   ├── login-security-checks/                          - Login security validation scenarios
        │   │   └── product-display-page-with-different-sort-options/ - Product display & sorting scenarios
        │   ├── testData/         - Test data files
        │   │   ├── excelFiles/   - Excel files containing test data
        │   │   └── jsonFiles/    - JSON files containing test data
        │   ├── extent.properties
        │   ├── html-config.xml
        │   ├── log4j2.properties
        │   └── spark-config.xml
        └── java/
            └── com/
                └── saucedemo/
                    ├── annotations/          - Custom annotations for test methods
                    ├── configReader/         - Configuration file readers
                    │   └── DataProviders/    - TestNG data providers
                    ├── constants/            - Project-wide constants
                    ├── enums/                - Enumerations
                    ├── exceptions/           - Custom exceptions
                    ├── factories/            - Object factories
                    ├── helperUtilities/      - Helper utility classes
                    │   ├── Actions/          - Action driver and interfaces
                    │   ├── alert/            - Alert handling
                    │   ├── assertion/        - Assertion helpers
                    │   ├── assertors/        - Condition assertors
                    │   ├── button/           - Button interaction helpers
                    │   ├── checkBox/         - Checkbox interaction helpers
                    │   ├── database/         - Database interaction helpers
                    │   ├── date/             - Date/time helpers
                    │   ├── dropdown/         - Dropdown interaction helpers
                    │   ├── elements/         - Element interaction helpers
                    │   ├── emailGen/         - Email generation helpers
                    │   ├── excel/            - Excel file helpers
                    │   ├── extractor/        - Data extraction helpers
                    │   ├── faker/            - Fake data generation
                    │   ├── file/             - File operation helpers
                    │   ├── frame/            - Frame handling helpers
                    │   ├── generic/          - Generic helpers
                    │   ├── get/              - Value retrieval helpers
                    │   ├── globalVar/        - Global variable management
                    │   ├── grid/             - Grid interaction helpers
                    │   ├── HyperLink/        - Hyperlink interaction helpers
                    │   ├── inputFields/      - Input field helpers
                    │   ├── javaScript/       - JavaScript execution helpers
                    │   ├── json/             - JSON file helpers
                    │   ├── links/            - Link helpers
                    │   ├── logger/           - Logging helpers
                    │   ├── navigation/       - Navigation helpers
                    │   ├── number_StringGen/ - Number/string generation helpers
                    │   ├── pageException/    - Page exception handlers
                    │   ├── pageLoad/         - Page load check helpers
                    │   ├── pdf/              - PDF file helpers
                    │   ├── processFiles/     - File processing helpers
                    │   ├── radioButton/      - Radio button interaction helpers
                    │   ├── resource/         - Resource helpers
                    │   ├── select/           - Select element helpers
                    │   ├── string/           - String helpers
                    │   ├── textBox/          - Text box interaction helpers
                    │   ├── wait/             - Wait helpers
                    │   ├── webElement/       - WebElement helpers
                    │   └── window/           - Window handling helpers
                    ├── interfaces/           - Project interfaces
                    ├── model/                - Model classes
                    │   └── employer/         - Employer-related models
                    ├── pages/                - Page Object Models
                    │   └── general/          - General page objects
                    ├── reportingTestData/    - Test data for reporting
                    ├── runners/              - Cucumber test runners
                    ├── steps/                - Cucumber step definitions
                    ├── tests/                - TestNG test classes
                    ├── userTestData/         - User test data
                    ├── utils/                - Utility classes
                    ├── webdriverutilities/   - WebDriver utilities
                    └── webelementdata/       - WebElement data classes
```

============================================================
**Full Business logic:**
============================================================

1. **Login-Logout:** Tests the login and logout functionality of the application.
2. **Login Security Checks:** Tests security validations on the login page (invalid credentials, locked users, etc.).
3. **Add-To-Cart:** Tests the functionality of adding items to the shopping cart.
4. **CheckoutProcess:** Tests the full checkout process of the application.
5. **Product-Display-Page-With-Different-Sort-Options:** Tests the product display page and its various sorting options.
6. **End-To-End-And-Miscellaneous:** Tests end-to-end flows and miscellaneous scenarios.

============================================================
**Dependency management:**
============================================================
Maven is used for dependency management.

Notes:
The project uses a combination of TestNG and Cucumber for testing. It also includes a number of helper utilities for
various tasks, such as handling alerts, assertions, and working with elements.

All endpoints/All events the project exposes and listens:
The project does not expose any endpoints or events. It is a test automation project that interacts with an external
application.

============================================================
**All downstream services**
============================================================

- **Sauce Demo Application:** The application under test — https://www.saucedemo.com
- **PostgreSQL Database:** Used for storing and verifying test data (AWS RDS, eu-west-2).
- **Elasticsearch 7.17.25:** Used for indexing and searching test data (port 9200).
- **Kibana 7.17.25:** Used for visualizing and analyzing test data (port 5601).
- **Chrome Browser:** Used for running tests on Chrome (via Selenium Grid node).
- **Firefox Browser:** Used for running tests on Firefox (via Selenium Grid node).
- **Selenium Grid Hub:** Manages browser nodes (port 4444).
- **Git Repository:** https://github.com/azetaben/acceptance-testing.git

## All upstream services

- Frontend Application: The application that uses the test automation project.

### Key Testing Dependencies

| Dependency                        | Version    |
|-----------------------------------|------------|
| TestNG                            | 7.11.0     |
| Cucumber (Java/TestNG/Core)       | 7.22.0     |
| Selenium                          | 4.32.0     |
| Lombok                            | 1.18.38    |
| PDFBox                            | 2.0.27     |
| XChart                            | 3.8.0      |
| SnakeYAML                         | 2.0        |
| JSoup                             | 1.15.3     |
| Gson                              | 2.10.1     |
| MongoDB Java Driver               | 3.0.4      |
| Commons IO                        | 2.16.1     |
| Commons Lang3                     | 3.18.0     |
| JavaFaker                         | 1.0.2      |
| Log4j2                            | 2.25.4     |
| JSON                              | 20231013   |
| Logback Classic                   | 1.5.25     |
| **Apache POI**                    | **5.4.0**  |
| Dom4j                             | 2.1.4      |
| ExtentReports Cucumber7 Adapter   | 1.14.0     |
| Cluecumber Report Plugin          | 2.9.4      |
| Plexus Utils                      | 4.0.3      |
| Awaitility Proxy                  | 3.1.1      |
| PostgreSQL Driver                 | 42.7.2     |
| Jackson Core/Annotations/Databind | 2.18.6     |
| Mockito All                       | 1.9.5      |
| Guava                             | 32.0.1-jre |
| **ExtentReports**                 | **5.1.0**  |
| Maven Cucumber Reporting          | 5.11.0     |
| Axe-Core Selenium                 | 4.6.0      |
| Selenium Devtools v102            | 4.4.0      |
| Selenium Devtools v147            | 4.43.0     |
| Cucumber Picocontainer            | 7.22.0     |

**Summary of Found Test Flows:**

- Login and Logout
- Login Security Checks
- Add items to the cart
- Checkout process
- Product display and sorting
- End-to-end testing
- Miscellaneous tests

**Fixtures / Storage:**

- Excel files (`src/test/resources/testData/excelFiles/`)
- JSON files (`src/test/resources/testData/jsonFiles/`)

**Useful to know:**
The project uses a data-driven approach to testing, where test data is stored in Excel and JSON files.
The project uses a number of helper utilities to make testing easier, such as classes for handling alerts, assertions,
and working with elements.
The project uses ExtentReports and Cluecumber for reporting test results.

## Setting Up the Development Environment

1. **Install Java 17:** https://www.oracle.com/java/technologies/javase-downloads.html
2. **Install Maven:** https://maven.apache.org/
3. **Install IntelliJ IDEA:** https://www.jetbrains.com/idea/
4. **Install Docker:** https://www.docker.com/products/docker-desktop
5. **Install Git:** https://git-scm.com/downloads
6. **Clone the project:**
   ```bash
   git clone https://github.com/azetaben/acceptance-testing.git
   ```
7. **Import the project** into IntelliJ IDEA and configure Maven.
8. **Start Docker services** (Selenium Grid, Elasticsearch, Kibana):
   ```bash
   docker-compose up -d
   ```

## Running the Project

### Build the project

```bash
mvn clean install
```

### Run all tests

```bash
mvn test
```

### Run tests in parallel

```bash
mvn test -Dtest.threadcount=2
```

### Run with a specific Maven profile

| Profile          | Command                           |
|------------------|-----------------------------------|
| Regression       | `mvn test -PRegression`           |
| Sanity           | `mvn test -PSanityTest`           |
| Smoke            | `mvn test -PSmoke`                |
| Error Validation | `mvn test -PErrorValidationTests` |
| Performance      | `mvn test -PPerformanceTests`     |
| Checkout Process | `mvn test -PcheckoutProcessTests` |
| Login Tests      | `mvn test -PLoginTests`           |
| Cucumber Tests   | `mvn test -PCucumberTests`        |

### Run with a specific Cucumber tag

```bash
mvn test -Dcucumber.options="--tags @Smoke"
```

### Run with Docker Compose

```bash
docker-compose up -d
```

### Stop Docker Compose containers

```bash
docker-compose down
```

## Viewing Logs and Reports

| Report                  | Path                                                   |
|-------------------------|--------------------------------------------------------|
| Automation log          | `logs/automation.out`                                  |
| Cucumber HTML report    | `cucumber_report/cucumber.html`                        |
| Extent HTML report      | `extent-report/reports_/<Date>/Extent.html`            |
| Extent Spark report     | `extent-report/reports_/<Date>/Spark/ExtentSpark.html` |
| Cluecumber report       | `target/site/cluecumber-report/`                       |
| Surefire report         | `target/surefire-reports/index.html`                   |
| TestNG emailable report | `target/test-output/emailable-report.html`             |
| TestNG HTML report      | `target/test-output/html/index.html`                   |
| TestNG JUnit report     | `target/test-output/junitreports/`                     |
| TestNG log              | `target/test-output/logs/log.txt`                      |
| Screenshots             | `target/test-output/screenshots/`                      |


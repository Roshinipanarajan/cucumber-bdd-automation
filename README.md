# Cucumber BDD Automation – Books to Scrape

End-to-end UI automation framework built with **Java, Maven, Selenium 4, TestNG and Cucumber BDD**.
Application under test: <https://books.toscrape.com/> (public practice site – no login required).

## Concepts demonstrated

| # | Concept | Where |
|---|---------|-------|
| 1 | Maven project & dependency management | `pom.xml`, standard `src/test/java` + `src/test/resources` layout |
| 2 | BDD with Cucumber (Gherkin) | `src/test/resources/features/*.feature` |
| 3 | TestNG XML + Surefire plugin | `testng.xml`, `maven-surefire-plugin` in `pom.xml` |
| 4 | Cucumber Test Runner | `runners/TestRunner.java` |
| 5 | Feature files under `src/test/resources` | `src/test/resources/features` |
| 6 | Step definitions under `src/test/java` | `stepdefinitions/` |
| 7 | Inheritance – reusable static Driver | `base/Driver.java` → `base/BasePage.java` → every page object |
| 8 | Encapsulation – Page Object Model | `pages/` (private locators, public behaviour methods) |
| 9 | Hooks | `hooks/Hooks.java` (`@Before`, `@After`) |
| 10 | Selenium Grid & parallel execution | `docker-compose.yml`, `testng.xml`, `@DataProvider(parallel = true)`, `ThreadLocal<WebDriver>` |
| 11 | Log4j 2 logging | `src/test/resources/log4j2.xml`, loggers in every class → `target/logs/automation.log` |
| 12 | Allure reports & screenshots | `allure-cucumber7-jvm`, `utils/ScreenshotUtil.java` |
| 13 | Try-catch | `Driver`, `BasePage`, `ScreenshotUtil`, `ConfigReader` |
| 14 | Explicit waits | `BasePage` (`WebDriverWait` + `ExpectedConditions`) – no `Thread.sleep` |
| 15 | Assertions | TestNG `Assert` in all step definitions |

## Project structure

```
cucumber-bdd-automation
├── pom.xml
├── testng.xml
├── docker-compose.yml               # Selenium Grid (hub + Chrome + Firefox)
└── src/test
    ├── java/com/bookstore
    │   ├── base            Driver.java, BasePage.java
    │   ├── pages           HomePage, CategoryPage, BookDetailsPage
    │   ├── stepdefinitions HomeSteps, CategorySteps, BookDetailsSteps
    │   ├── hooks           Hooks.java
    │   ├── runners         TestRunner.java
    │   └── utils           ConfigReader.java, ScreenshotUtil.java
    └── resources
        ├── features        home.feature, category.feature, book_details.feature
        ├── config          config.properties
        ├── log4j2.xml
        ├── allure.properties
        └── cucumber.properties
```

## Prerequisites

* JDK 17+
* Maven 3.8+
* Google Chrome (Selenium Manager downloads the matching driver automatically)
* Docker (only for the Selenium Grid run)
* Optional: Allure CLI (`mvn allure:serve` works without it)

## How to run

All commands run from the project root.

```bash
# 1. Run the whole suite locally (headless Chrome, 3 scenarios in parallel)
mvn clean test

# 2. Watch the browser (not headless)
mvn clean test -Dheadless=false

# 3. Another browser
mvn clean test -Dbrowser=firefox

# 4. Run only some scenarios by tag
mvn clean test -Dcucumber.filter.tags="@smoke"
mvn clean test -Dcucumber.filter.tags="@category"

# 5. Demonstrate screenshot on failure (deliberately failing scenario)
mvn clean test -Dcucumber.filter.tags="@demo-failure"
```

### Parallel execution

Every Cucumber scenario is a row of the TestNG `@DataProvider(parallel = true)` in `TestRunner`.
The number of scenarios running at once is `data-provider-thread-count` in `testng.xml` (default `3`).
Each thread owns its browser because `Driver` keeps the `WebDriver` in a `ThreadLocal`.

### Selenium Grid

```bash
docker compose up -d                       # Grid UI: http://localhost:4444
mvn clean test -Dexecution=grid -Dgrid.url=http://localhost:4444
mvn clean test -Dexecution=grid -Dbrowser=firefox
docker compose down
```

While the suite runs, open <http://localhost:4444> to see several sessions executing in parallel.

### Reports

```bash
mvn allure:serve         # builds and opens the Allure report in the browser
mvn allure:report        # writes the static report to target/site/allure-maven-plugin
```

* Raw Allure results: `target/allure-results`
* Cucumber HTML report: `target/cucumber-reports/cucumber.html`
* Screenshots (also attached inside Allure): `target/screenshots`
* Log file: `target/logs/automation.log`

A screenshot is always captured when a scenario fails. Set `screenshot.on.pass=false` in
`config.properties` to stop capturing on passing scenarios.

## Configuration

`src/test/resources/config/config.properties` holds the defaults. Any key can be overridden with `-D`:

| Key | Default | Meaning |
|-----|---------|---------|
| `base.url` | `https://books.toscrape.com/` | Application URL |
| `browser` | `chrome` | `chrome`, `firefox` or `edge` |
| `headless` | `true` | Run without a visible browser |
| `execution` | `local` | `local` or `grid` |
| `grid.url` | `http://localhost:4444` | Selenium Grid hub |
| `explicit.wait` | `15` | Explicit wait timeout (seconds) |
| `screenshot.on.pass` | `true` | Attach a screenshot to passing scenarios too |

## Test scenarios

| Feature | Scenarios |
|---------|-----------|
| Home page | page loads with 20 books, category sidebar, pagination to page 2 |
| Category | Scenario Outline: Travel, Mystery, Poetry, Humor – heading and result count |
| Book details | title, price, stock, rating, description; plus an excluded `@demo-failure` scenario |

## Pushing to GitHub

```bash
git init
git add .
git commit -m "Cucumber BDD automation framework"
git branch -M main
git remote add origin https://github.com/<your-username>/<repo-name>.git
git push -u origin main
```

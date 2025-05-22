# Tipico Careers Job Listings – QA Automation Project

## Project Description

This project is a QA automation suite for validating the job listings page at [Tipico Careers](https://www.tipico-careers.com/en/jobs/). It covers the following user scenario:

> **As a user, I want to see all the job openings under https://www.tipico-careers.com/en/jobs/ and be able to filter them by country and add/remove them to/from my favourites. Team name, position name and location should be present for each job and no more than 20 jobs should be listed per page.**

### Automated Test Scenarios

The following scenarios are implemented using Cucumber and Playwright:

- **Job Card Fields:** Each job card displays the team name, position name, and location.
- **Pagination:** No more than 20 jobs are listed per page.
- **Filtering:** Jobs can be filtered by country (e.g., Germany).
- **Favourites:** Jobs can be added to and removed from favourites, with UI feedback.

Feature file: [`src/test/resources/features/jobs.listing.feature`](src/test/resources/features/jobs.listing.feature)

---

## Technologies Used

- **Java 17**
- **Maven** (build and dependency management)
- **Playwright** (browser automation)
- **Cucumber** (BDD, feature files)
- **JUnit 5** (test runner)

Key dependencies (see [`pom.xml`](pom.xml)):

- `com.microsoft.playwright:playwright`
- `io.cucumber:cucumber-java`
- `io.cucumber:cucumber-junit-platform-engine`
- `org.junit.platform:junit-platform-suite`
- `org.junit.jupiter:junit-jupiter`

---

## Setup & Running the Tests

### Prerequisites

- Java 17+
- Maven 3.8+
- Internet connection (for browser download and accessing Tipico Careers)

### 1. Clone the repository

```sh
git clone https://github.com/AlekseyPudlo/tipico-job-tests.git
cd tipico-job-tests
```

### 2. Install dependencies & Playwright browsers

Maven will automatically install Playwright and required browsers:

```sh
mvn validate
```

### 3. Configure test settings (optional)

Edit [`src/test/resources/config.properties`](src/test/resources/config.properties) to adjust:

- `baseUrl` (default: https://www.tipico-careers.com/en/jobs/)
- `headless` (true/false)
- `timeout` (ms)
- `browserType` (chromium, firefox, webkit)

### 4. Run the tests

```sh
mvn clean test
```

### 5. View the reports

- HTML report: `target/cucumber-reports/cucumber.html`

---

## Extending the Project

- Add new feature files in `src/test/resources/features/`
- Implement step definitions in `src/test/java/com/tipico/features/`
- Add/update page objects in `src/test/java/com/tipico/common/pages/`

---

## Notes

- The project uses the Page Object Model for maintainability.
- Assertions and validations are implemented for all key UI elements and user actions.
- The test runner is located at [`src/test/java/com/tipico/runners/TestRunner.java`](src/test/java/com/tipico/runners/TestRunner.java).

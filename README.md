# Bit - TestNG Test Project

A Maven-based Java test project using TestNG and Java 21.

## Project Structure

```
src/
├── main/java/com/example/bit/
│   └── model/
│       ├── PaymentRequest.java        - Request payload model
│       └── PaymentResponse.java        - Response payload model
└── test/java/com/example/bit/
    ├── config/
    │   ├── SqsConfig.java              - AWS SQS configuration
    │   ├── TestApplicationConfig.java  - Test environment setup
    │   └── UITestConfig.java           - UI test configuration
    ├── listener/
    │   ├── SoftAssertListener.java     - TestNG listener for soft assertions
    │   └── UITestListener.java         - TestNG listener for UI tests
    ├── pages/
    │   ├── BasePage.java               - Base page object class
    │   ├── HomePage.java               - Home page object
    │   └── ApprovalMoneyPage.java      - Approval money page object
    ├── service/
    │   └── SqsService.java             - SQS message handling
    ├── tests/
    │   ├── api/
    │   │   ├── BasePaymentTest.java    - Base API test class with fixtures
    │   │   └── BitApplicationTests.java - Payment API flow test cases
    │   └── ui/
    │       ├── BaseUITest.java         - Base UI test class
    │       └── ApprovalMoneyUITest.java - Approval money UI test cases
    ├── utils/
    │   ├── DriverFactory.java          - WebDriver factory for browser initialization
    │   ├── HttpStatusCode.java         - HTTP status code utilities
    │   ├── PaymentSender.java          - HTTP payment request sender
    │   ├── RequestSender.java          - Generic HTTP request utility
    │   └── UITestMessages.java         - UI test message constants
    └── validators/
        └── PaymentValidator.java       - Payment response validators
```

## Prerequisites

- Java 21 or higher
- Maven 3.8.0 or higher

## Technology Stack

- **Java 21** - Latest LTS Java version
- **Spring Boot** - For test infrastructure and configuration management
- **TestNG** - Testing framework for both UI and API tests
- **Selenium** - WebDriver for UI automation
- **Maven** - Build and test automation tool

## Project Overview

This project demonstrates a unified testing approach where both **UI and API tests** are written in the same language (Java) with a consistent structure and shared utilities. The project includes:

- **Listeners** for cross-cutting concerns:
  - `SoftAssertListener` - Soft assertions for API tests to continue execution on assertion failures
  - `UITestListener` - Screenshot capture on UI test failures for debugging
- **Page Objects** - For maintainable UI test code
- **Shared Utilities** - Common HTTP request handlers, driver factory, and message constants
- **Environment Configuration** - Support for dev, test, staging, and production environments

## Test Execution

Tests are defined in `src/test/resources/testng.xml`. The file includes two test suites:

```xml
<!-- UI Tests -->
<test name="Money Approval UI Tests">
    <classes>
        <class name="com.example.bit.tests.ui.ApprovalMoneyUITest"/>
    </classes>
</test>

<!-- API Tests -->
<test name="Payment Request API Tests">
    <classes>
        <class name="com.example.bit.tests.api.BitApplicationTests"/>
    </classes>
</test>
```

To run tests using TestNG XML:

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=ApprovalMoneyUITest

# Run specific test method
mvn test -Dtest=BitApplicationTests#testPaymentFlow
```

### Test Suite Breakdown

- **UI Tests** (`src/test/java/com/example/bit/tests/ui/`)
  - `ApprovalMoneyUITest` - UI automation tests for the approval money flow
  - Uses `UITestListener` for automatic screenshot capture on failures
  - Page objects located in `src/test/java/com/example/bit/pages/`

- **API Tests** (`src/test/java/com/example/bit/tests/api/`)
  - `BitApplicationTests` - REST API tests for payment processing
  - Uses `SoftAssertListener` for soft assertions (non-blocking failures)
  - Integrates with AWS SQS for message validation

## Building the Project

```bash
mvn clean install
```

## Running Tests

```bash
mvn test
```

## Maven Commands

- `mvn clean` - Remove build directory
- `mvn compile` - Compile source code
- `mvn test` - Run tests
- `mvn package` - Create JAR file
- `mvn dependency:tree` - Display dependency tree

## CI/CD Pipeline (Jenkins)

The project includes a comprehensive Jenkinsfile for automated testing across multiple environments.

### Pipeline Features

- **Environment Selection** - Choose between dev, test, staging, and production
- **Test Suite Selection** - Run API tests, UI tests, or both
- **Browser Selection** - Choose browser for UI tests (CHROME, FIREFOX, EDGE)
- **Environment-Specific Configuration** - Automatic URL and log level configuration per environment
- **Test Report Generation** - HTML test reports and artifact archiving
- **Notifications** - Success/failure/unstable notifications (configurable)

### Running Pipeline

The Jenkins pipeline accepts the following parameters:
- `ENVIRONMENT` - Target environment (dev, test, staging, prod)
- `TEST_SUITE` - Which tests to run (api, ui, all)
- `BROWSER` - Browser for UI tests (default: CHROME)
- `GENERATE_REPORT` - Generate HTML reports (default: true)

### Pipeline Stages

1. **Checkout** - Source code checkout
2. **Validate Environment** - Environment configuration validation
3. **Build** - Maven clean compile
4. **Run API Tests** - Execute API tests with AWS credentials
5. **Run UI Tests** - Execute UI tests with browser configuration
6. **Generate Reports** - Archive logs and test reports
7. **Post Actions** - Cleanup and notifications

## Project Status

⚠️ **Note**: The current test implementations are **example/demo tests** designed to demonstrate the structure and approach. They are not fully functional and do not use mocking or actual backend implementations. These tests serve as templates showing:

- How to structure UI tests with page objects
- How to structure API tests with soft assertions
- How to use listeners for cross-cutting concerns (screenshots, assertions)
- How to organize tests for different layers (UI vs API)

To use these tests in production, you would need to:
- Implement actual test logic and assertions
- Set up proper test data and fixtures
- Integrate with real backend services or mocks
- Configure appropriate WebDriver settings and timeouts
- Set up environment-specific configurations in property files

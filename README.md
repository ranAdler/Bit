# Bit - TestNG Test Project

A Maven-based Java test project using TestNG and Java 21.

## Project Structure

```
src/
├── main/java/com/example/bit/
│   └── model/
│       ├── PaymentRequest.java      - Request payload model
│       └── PaymentResponse.java      - Response payload model
└── test/java/com/example/bit/
    ├── config/
    │   ├── SqsConfig.java            - AWS SQS configuration
    │   └── TestApplicationConfig.java - Test environment setup
    ├── listener/
    │   └── SoftAssertListener.java    - TestNG listener for soft assertions
    ├── service/
    │   └── SqsService.java            - SQS message handling
    ├── tests/
    │   ├── BasePaymentTest.java       - Base test class with fixtures
    │   └── BitApplicationTests.java   - Payment flow test cases
    ├── utils/
    │   ├── PaymentSender.java         - HTTP payment request sender
    │   └── RequestSender.java         - Generic HTTP request utility
    └── validators/
        └── PaymentValidator.java      - Payment response validators
```

## Prerequisites

- Java 21 or higher
- Maven 3.8.0 or higher

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

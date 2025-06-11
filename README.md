# 🏦 Dynamic Queries with JPA Specification

A Spring Boot application demonstrating the power of JPA Specification API for building dynamic, type-safe database queries using a Bank Account management system.

## 🎯 Overview

This project showcases how to implement **JPA Specification** to create flexible, dynamic database queries that adapt to user search criteria without the need for multiple repository methods. Instead of creating dozens of query methods for different filter combinations, we use a single specification-based approach.

### What is JPA Specification?

JPA Specification is a programmatic way to build database queries using the Criteria API. It allows you to:

- ✅ Create **type-safe** queries at compile time
- ✅ Build **dynamic** queries based on runtime conditions
- ✅ **Compose** and **reuse** query logic
- ✅ Avoid **query method explosion** in repositories
- ✅ Handle **complex search scenarios** elegantly

## 🚀 Features

- **Dynamic Account Search**: Filter accounts by multiple criteria simultaneously
- **Type-Safe Queries**: Compile-time validation of entity properties
- **Flexible Filtering**: Support for partial matches, range queries, and multiple selections
- **Pagination Support**: Efficient handling of large datasets
- **RESTful API**: Clean REST endpoints for account management
- **H2 Database**: In-memory database for easy testing and development

## 🔧 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

## 🏃‍♂️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/martourez21/dynamic-queries-with-jpa-specification.git
cd dynamic-queries-with-jpa-specification
```

### 2. Build the Project

```bash
mvn clean compile
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8089`

### 4. Access H2 Console (Optional)

Navigate to `http://localhost:8089/h2-console`

- **JDBC URL**: `jdbc:h2:file:./data/accountdb`
- **Username**: `sa`
- **Password**: *(leave empty)*

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/customers/queries/
│   │   ├── entity/
│   │   │   ├── AccountEntity.java          # Account entity
│   │   │   └── BankBranch.java            # Bank branch entity
│   │   ├── dto/
│   │   │   ├── AccountSearchCriteria.java  # Search criteria DTO
│   │   │   ├── AccountRequestDto.java      # Request DTO
│   │   │   └── AccountResponseDto.java     # Response DTO
│   │   ├── service/
│   │   │   └── AccountSpecification.java   # 🎯 JPA Specification logic
│   │   ├── repository/
│   │   │   └── AccountEntityRepository.java # Repository with specification support
│   │   └── controller/
│   │       ├── AccountApi.java             # API interface
│   │       └── AccountApiHandler.java      # API implementation
│   └── resources/
│       └── application.yml                 # Application configuration
└── pom.xml                                # Maven dependencies
```

## 💡 JPA Specification Explained

### The Problem
Traditional repository approaches require multiple methods for different filter combinations:

```java
// ❌ This approach doesn't scale!
List<Account> findByAccountHolderName(String name);
List<Account> findByAccountHolderNameAndAccountType(String name, AccountType type);
List<Account> findByAccountHolderNameAndAccountTypeAndStatus(String name, AccountType type, AccountStatus status);
// ... dozens more methods needed
```

### The Solution
JPA Specification allows dynamic query building:

```java
// ✅ One method to rule them all!
public static Specification<AccountEntity> withFilter(AccountSearchCriteria criteria) {
    return (root, query, cb) -> {
        List<Predicate> predicates = new ArrayList<>();

        // Add conditions only if criteria is provided
        if (criteria.getAccountHolderName() != null) {
            predicates.add(cb.like(cb.lower(root.get("accountHolderName")),
                    "%" + criteria.getAccountHolderName().toLowerCase() + "%"));
        }

        if (criteria.getAccountType() != null) {
            predicates.add(cb.equal(root.get("accountType"), criteria.getAccountType()));
        }

        // ... more conditions

        return cb.and(predicates.toArray(new Predicate[0]));
    };
}
```

### Key Components

1. **Root**: Represents the entity being queried (`AccountEntity`)
2. **CriteriaQuery**: Defines the query structure
3. **CriteriaBuilder**: Builds predicates and expressions
4. **Predicate**: Represents WHERE clause conditions

## 🔗 API Endpoints

### Account Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/v1.0/bank/{branchCode}/accounts` | Create new account |
| `GET` | `/v1.0/bank/accounts/{accountId}` | Get account details |
| `PUT` | `/v1.0/bank/accounts/{accountId}` | Update account |
| `DELETE` | `/v1.0/bank/accounts/{accountId}` | Delete account |
| `GET` | `/v1.0/bank/accounts` | **Search accounts with filters** |

### Dynamic Search Endpoint

```http
GET /v1.0/bank/accounts?accountHolderName=John&accountType=SAVINGS&minBalance=1000&page=0&size=10
```

**Supported Query Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `accountHolderName` | String | Partial name search (case-insensitive) |
| `accountType` | Enum | Account type (SAVINGS, CHECKING, etc.) |
| `status` | Enum | Account status (ACTIVE, INACTIVE, etc.) |
| `minBalance` | BigDecimal | Minimum balance filter |
| `maxBalance` | BigDecimal | Maximum balance filter |
| `branchCode` | String | Branch code filter |
| `createdAfter` | DateTime | Created after date |
| `createdBefore` | DateTime | Created before date |
| `minInterestRate` | BigDecimal | Minimum interest rate |
| `maxInterestRate` | BigDecimal | Maximum interest rate |
| `page` | Integer | Page number (default: 0) |
| `size` | Integer | Page size (default: 10) |
| `sort` | String | Sort criteria |

## 🎯 Usage Examples

### 1. Search by Account Holder Name

```bash
curl -X GET "http://localhost:8089/v1.0/bank/accounts?accountHolderName=john"
```

### 2. Filter by Account Type and Status

```bash
curl -X GET "http://localhost:8089/v1.0/bank/accounts?accountType=SAVINGS&status=ACTIVE"
```

### 3. Balance Range Query

```bash
curl -X GET "http://localhost:8089/v1.0/bank/accounts?minBalance=1000&maxBalance=50000"
```

### 4. Complex Multi-Filter Search

```bash
curl -X GET "http://localhost:8089/v1.0/bank/accounts?accountHolderName=smith&accountType=CHECKING&minBalance=5000&status=ACTIVE&page=0&size=5"
```

### 5. Date Range Search

```bash
curl -X GET "http://localhost:8089/v1.0/bank/accounts?createdAfter=2024-01-01T00:00:00&createdBefore=2024-12-31T23:59:59"
```

## 🗄️ Database Schema

### Account Entity
```sql
CREATE TABLE account_entity (
    account_id UUID PRIMARY KEY,
    account_number VARCHAR(255) UNIQUE,
    account_holder_name VARCHAR(255),
    account_type VARCHAR(50),
    balance DECIMAL(19,2),
    status VARCHAR(50),
    created_date TIMESTAMP,
    last_transaction_date TIMESTAMP,
    interest_rate DECIMAL(5,2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    bank_branch_id UUID
);
```

### Sample Data
The application automatically creates sample data on startup for testing purposes.

## ⚙️ Configuration

### Application Properties (`application.yml`)

```yaml
server:
  port: 8089

spring:
  datasource:
    url: jdbc:h2:file:./data/accountdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  h2:
    console:
      enabled: true
      path: /h2-console

account:
  api:
    version: v1.0
```

### Maven Dependencies

Key dependencies for JPA Specification:

```xml
<dependencies>
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok for reducing boilerplate -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## 🧪 Testing the Application

### 1. Create a New Account

```bash
curl -X POST "http://localhost:8089/v1.0/bank/CM01/accounts" \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "John Doe",
    "accountType": "SAVINGS",
    "balance": 5000.00,
    "status": "ACTIVE",
    "interestRate": 2.5
  }'
```

### 2. Search for the Account

```bash
curl -X GET "http://localhost:8089/v1.0/bank/accounts?accountHolderName=John"
```

## 🎓 Learning Objectives

After exploring this project, you'll understand:

- **JPA Specification API**: How to build type-safe, dynamic queries
- **Criteria API**: Working with Root, CriteriaQuery, and CriteriaBuilder
- **Predicate Composition**: Combining multiple search conditions
- **Repository Pattern**: Extending JpaSpecificationExecutor
- **REST API Design**: Building flexible search endpoints
- **Spring Boot Best Practices**: Project structure and configuration

## 🔧 Extending the Project

Want to add more features? Try implementing:

- **Advanced Sorting**: Multiple sort criteria
- **OR Conditions**: Alternative to AND-only filtering
- **Join Queries**: Search across related entities
- **Custom Specifications**: Reusable specification components
- **Caching**: Add Redis for query result caching
- **Security**: JWT authentication and authorization

## 📚 Further Reading

- [Spring Data JPA Specification Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#specifications)
- [JPA Criteria API Guide](https://docs.oracle.com/javaee/7/tutorial/persistence-criteria.htm)
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 🌟 Key Takeaways

**JPA Specification is powerful because it:**

- **Eliminates query method explosion** - One specification handles all combinations
- **Provides type safety** - Catch errors at compile time, not runtime
- **Enables dynamic queries** - Build queries based on runtime conditions
- **Improves maintainability** - Centralized query logic in specification classes
- **Supports complex scenarios** - Joins, subqueries, and advanced filtering

**Happy coding! 🚀**
# Mining Asset & Maintenance Management System

A Java console application for managing mining equipment assets and their maintenance records, backed by PostgreSQL.

This is a portfolio project built to practice Java backend development with a layered architecture. The plan is to evolve it step by step into a REST API and web application.

## Tech Stack

- Java 25 (Temurin)
- Maven
- PostgreSQL 18
- JDBC (`org.postgresql:postgresql:42.7.13`)
- JUnit (unit tests with fake repositories)

## Features

- Asset management: create, read, update, delete, and search
- Maintenance record management with history per asset
- CSV import for assets
- Statistics, financial summary, and dashboard
- Maintenance cost analysis

## Architecture

The project uses a layered architecture:

```
Controller -> Service -> Repository -> PostgreSQL
```

```
src/main/java/com/mining/maintenance/
├── config/        Database connection
├── controller/    Console input/output
├── exception/     Custom exceptions
├── importer/      CSV import
├── model/         Domain models
├── repository/    Database access (JDBC)
├── service/       Business logic
└── util/          Helpers
```

## Getting Started

### Prerequisites

- JDK 25
- Maven
- PostgreSQL 18

### 1. Clone the repository

```
git clone https://github.com/ilhamgustipratama-dev/mining-maintenance-system.git
cd mining-maintenance-system
```

### 2. Create the database

Create an empty database named `mining_maintenance`, then run `schema.sql` on it (pgAdmin Query Tool or `psql`).

### 3. Configure the connection

The database connection is configured with environment variables:

| Variable | Required | Default |
|---|---|---|
| `DB_PASSWORD` | Yes | none |
| `DB_USER` | No | `postgres` |
| `DB_URL` | No | `jdbc:postgresql://localhost:5432/mining_maintenance` |

PowerShell example:

```
$env:DB_PASSWORD = "your_password"
```

### 4. Run the application

Run `Main.java` from your IDE, or build with Maven:

```
mvn compile
```

## Running Tests

```
mvn test
```

The test suite has 28 tests (15 for `AssetService`, 13 for `MaintenanceService`). Tests use in-memory fake repositories, so no database is needed to run them.

## Roadmap

- [ ] REST API
- [ ] Spring Boot
- [ ] Authentication and authorization
- [ ] API testing
- [ ] Web frontend
- [ ] Deployment
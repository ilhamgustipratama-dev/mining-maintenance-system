# Mining Asset & Maintenance Management System

A Java console application for managing mining equipment assets and their maintenance records, backed by PostgreSQL. Also exposes a REST API built with Spring Boot.

This is a portfolio project built to practice Java backend development with a layered architecture, from a JDBC-based console app to a REST API.

## Tech Stack

- Java 25 (Temurin)
- Maven
- PostgreSQL 18
- JDBC (`org.postgresql:postgresql:42.7.13`)
- Spring Boot 4.0.5
- JUnit (unit tests with fake repositories)

## Features

- Asset management: create, read, update, delete, and search
- Maintenance record management with history per asset
- CSV import for assets
- Statistics, financial summary, and dashboard
- Maintenance cost analysis
- REST API for assets and maintenance records (see API Documentation below)

## Architecture

The project uses a layered architecture:

Controller -> Service -> Repository -> PostgreSQL

```
src/main/java/com/mining/maintenance/
├── config/        Database connection
├── controller/    Console I/O and REST controllers
├── exception/     Custom exceptions
├── importer/      CSV import
├── model/         Domain models
├── repository/    Database access (JDBC)
├── service/       Business logic
└── util/          Helpers
```

The application has two entry points:
- `Main.java` — the console (CLI) application
- `Application.java` — the Spring Boot REST API

Both share the same Service and Repository layers.

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

### 4. Run the console application

Run `Main.java` from your IDE, or build with Maven:

```
mvn compile
```

### 5. Run the REST API

Run `Application.java` from your IDE, or:

```
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Running Tests

```
mvn test
```

The test suite has 28 tests (15 for `AssetService`, 13 for `MaintenanceService`). Tests use in-memory fake repositories, so no database is needed to run them.

## API Documentation

The REST API runs alongside the console app, sharing the same Service and Repository layers.

### Assets

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/assets` | Get all assets |
| GET | `/api/assets/{assetCode}` | Get a single asset by code |
| POST | `/api/assets` | Create a new asset |
| PUT | `/api/assets/{id}` | Update an asset |
| DELETE | `/api/assets/{id}` | Delete an asset |

### Maintenance Records

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/maintenance` | Get all maintenance records |
| GET | `/api/maintenance/{id}` | Get a single maintenance record by ID |
| GET | `/api/maintenance/asset/{assetId}` | Get maintenance history for a specific asset |
| POST | `/api/maintenance` | Create a new maintenance record |
| PUT | `/api/maintenance/{id}` | Update a maintenance record |
| DELETE | `/api/maintenance/{id}` | Delete a maintenance record |

## Roadmap

- [x] REST API
- [x] Spring Boot
- [ ] Authentication and authorization
- [ ] API testing
- [ ] Web frontend
- [ ] Deployment
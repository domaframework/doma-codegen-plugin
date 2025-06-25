# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Doma CodeGen Plugin is a Gradle plugin that generates Java, Kotlin, and SQL files from database schemas. It's part of the Doma framework ecosystem and supports Doma 3.

## Build Commands

```bash
# Build entire project with code formatting
./gradlew spotlessApply build

# Build only the plugin module
./gradlew :codegen:build

# Clean build
./gradlew clean build
```

## Testing Commands

```bash
# Run all tests
./gradlew test

# Run specific module tests
./gradlew :codegen:test
./gradlew :codegen-test:test

# Test code generation (from codegen-test directory)
cd codegen-test
./gradlew domaCodeGenJavaAll build
./gradlew domaCodeGenKotlinAll build
```

## Code Quality

```bash
# Apply code formatting (required before commits)
./gradlew spotlessApply

# Check code formatting
./gradlew spotlessCheck
```

## Architecture Overview

### Multi-Module Structure
- **codegen**: Main plugin implementation
  - Plugin entry point: `CodeGenPlugin.java`
  - Tasks defined in `org.seasar.doma.gradle.codegen.task` package
  - Code generators in `org.seasar.doma.gradle.codegen.generator` package
  - Database dialects in `org.seasar.doma.gradle.codegen.dialect` package

- **codegen-test**: Integration test project demonstrating plugin usage

### Key Components

1. **Code Generators**: Transform database metadata into Java/Kotlin code using FreeMarker templates
   - `JavaGenerator`: Generates Java entities and DAOs
   - `KotlinGenerator`: Generates Kotlin entities and DAOs
   - `SqlGenerator`: Generates SQL template files

2. **Database Dialects**: Database-specific implementations for metadata extraction
   - Supports: H2, MySQL, Oracle, PostgreSQL, SQL Server, DB2, HSQLDB

3. **Gradle Tasks**: Plugin provides tasks prefixed with `domaCodeGen`
   - Pattern: `domaCodeGen{Java|Kotlin|Sql}{EntityName|All}`

4. **Template System**: FreeMarker templates in `/codegen/src/main/resources/`
   - Customizable via `templateDir` configuration

### Development Notes

- Minimum Java version: 17
- Uses Gradle Kotlin DSL for build configuration
- JUnit 5 for testing
- Google Java Format for code style (enforced via Spotless)
- Plugin ID: `org.domaframework.doma.codegen`
- Current version: Check `gradle.properties`

### Database Connection

The plugin connects to databases to read metadata. Configure in build.gradle:
```kotlin
domaCodeGen {
    url = "jdbc:h2:mem:example"
    user = "sa"
    password = ""
}
```
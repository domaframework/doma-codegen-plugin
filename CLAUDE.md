# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Doma CodeGen Plugin is a Gradle plugin that generates Java, Kotlin, and SQL files from database schemas. It's part of the Doma framework ecosystem and supports Doma 3.

## Build Commands

```bash
# Build the codegen plugin
./gradlew build
```

## Testing Commands

```bash
# Run all tests
./gradlew test

# Run specific module tests
./gradlew :codegen:test
./gradlew :codegen-h2-test:test
./gradlew :codegen-tc-test:test
```

## Code Quality

```bash
# Apply code formatting (required before commits)
./gradlew :codegen:spotlessApply

# Check code formatting
./gradlew :codegen:spotlessCheck
```

## Architecture Overview

### Multi-Module Structure
This is a Gradle composite build with the following structure:

- **Root project** (`doma-codegen-plugin`): Contains build configuration and release management
  - Uses `pluginManagement` with `includeBuild("codegen")` to include the plugin project
  - Includes test modules: `codegen-h2-test` and `codegen-tc-test`

- **codegen**: Main plugin implementation (included build)
  - Plugin entry point: `CodeGenPlugin.java`
  - Tasks defined in `org.seasar.doma.gradle.codegen.task` package
  - Code generators in `org.seasar.doma.gradle.codegen.generator` package
  - Database dialects in `org.seasar.doma.gradle.codegen.dialect` package
  - Has its own `settings.gradle.kts` and `gradle.properties`
  - Uses Groovy for some components (see `src/main/groovy`)

- **codegen-h2-test**: Integration test module using H2 database
  - Tests code generation with in-memory H2 database
  - Uses the plugin via `id("org.domaframework.doma.codegen")`
  - Single configuration: `h2`

- **codegen-tc-test**: Integration test module using Testcontainers
  - Tests code generation with PostgreSQL via Testcontainers
  - Supports both Java and Kotlin code generation
  - Two configurations: `java` and `kotlin`

### Key Components

1. **Code Generators**: Transform database metadata into Java/Kotlin code using FreeMarker templates

2. **Database Dialects**: Database-specific implementations for metadata extraction
   - Supports: H2, MySQL, Oracle, PostgreSQL, SQL Server, DB2, HSQLDB

3. **Gradle Tasks**: Plugin provides tasks prefixed with `domaCodeGen`
   - Pattern: `domaCodeGen{ConfigName}{Java|Kotlin|Sql}{EntityName|All}`
   - Example: `domaCodeGenH2JavaAll`, `domaCodeGenKotlinEntityEmployee`

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

The plugin connects to databases to read metadata. Configure in build.gradle.kts:
```kotlin
domaCodeGen {
    register("myConfig") {
        url = "jdbc:h2:mem:example"
        user = "sa"
        password = ""
        entity {
            packageName = "com.example.entity"
        }
        dao {
            packageName = "com.example.dao"
        }
    }
}
```

### Important Notes

- The plugin is published to Gradle Plugin Portal (not Maven Central)
- In development, test modules apply the plugin using `id("org.domaframework.doma.codegen")` which resolves to the local included build
- Each configuration in `domaCodeGen` block creates its own set of tasks
- The `domaCodeGen` configuration is used for JDBC driver dependencies
- CI uses JDK 21 for builds, but the plugin targets Java 17 compatibility
Doma CodeGen Plugin
===================

Doma CodeGen Plugin is a gradle plugin.  
It generates Java source files and SQL files from Database.

How to use
----------

First, see [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.seasar.doma.codegen).

The basic usage example is as follows:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        // specify your JDBC driver
        classpath 'com.h2database:h2:1.3.175'
    }
}

plugins {
    id 'java'
    // specify the Doma CodeGen Plugin with correct version
    id 'org.seasar.doma.codegen' version 'x.x.x'
}

domaCodeGen {
    // make an arbitrary named block
    dev {
        // JDBC url
        url = '...'
        // JDBC user
        user = '...'
        // JDBC password
        password = '...'
        // configuration for generated entity source files
        entity {
          packageName = 'org.example.entity'
        }
        // configuration for generated DAO source files
        dao {
          packageName = 'org.example.dao'
        }
    }
}
```

To generate all files, run `domaCodeGenDevAll` task:

```bash
$ ./gradlew domaCodeGenDevAll
```

Gradle Tasks
------------

The Doma CodeGen Plugin provides following tasks:

- domaCodeGen*Name*All - Generates all.
- domaCodeGen*Name*Dao - Generates DAO source files.
- domaCodeGen*Name*Dto - Reads resultSet metadata and generate a DTO source file.
- domaCodeGen*Name*Entity - Generates entity source files.
- domaCodeGen*Name*Sql - Generates SQL files.
- domaCodeGen*Name*SqlTest - Generates SQL test source files.

Note that each *Name* part in the above task names is replaced with the block name defined under the `domaCodeGen` block.  
In the above usage example, the *Dev* part is corresponding to the `dev` block.

To check all defined task names, run the `tasks` task:

```bash
$ ./gradlew tasks
```

Config Options
--------------

### named config

A named config must be under the `domaCodeGen` block.  
The name of the config is arbitrary.  
You can make multiple configs under the `domaCodeGen` block.  

In the following example, we define two configs - `sales` and `account`:

```groovy
domaCodeGen {
  sales {   
    url = "jdbc:h2:mem:sales" 
  }
  account {
    url = "jdbc:h2:mem:account" 
  }
}
```

| Option | Description | Values | Default |
| :--- | :--- | :--- | :--- |
| url | JDBC url |  | |
| user | JDBC user  |  | |
| password | JDBC password | | |
| dataSource | database data source | | inferred by the url |
| codeGenDialect | database dialect | | inferred by the url |
| schemaName | database schema name | | |
| tableNamePattern | database table pattern (Regex) | | `.*` |
| ignoredTableNamePattern | database ignored table pattern (Regex) | | `.*\$.*` |
| tableTypes | database table type | such as TABLE, VIEW and so on | `TABLE` |
| versionColumnNamePattern | database version column pattern (Regex) | | `VERSION([_]?NO)?` |
| templateEncoding | encoding for freeMarker template files | | `UTF-8` |
| templateDir | directory for user customized template files | | |
| encoding | encoding for generated Java source files | | `UTF-8` |
| sourceDir | directory for generated Java source files | | `src/main/java` |
| testSourceDir | directory for generated Java test source files | | `src/test/java` |
| resourceDir | directory for generated SQL files | | src/main/resources |
| globalFactory | entry point to customize plugin behavior | | `new org.seasar.doma.gradle.codegen.GlobalFactory()` |

### entity

An `entity` block must be under a named config:

```groovy
domaCodeGen {
  sales {
    entity {
      useAccessor = false
    }
  }
}
```

| Option | Description | Values | Default |
| :--- | :--- | :--- | :--- |
| overwrite | where to overwrite generated entity files or not |  | `true` |
| overwriteListener | allow to overwrite listeners or not |  | `false` |
| superclassName | common superclass for generated entity classes |  | |
| listenerSuperclassName | common superclass for generated entity listener classes |  | |
| packageName | package name for generated entity class |  | `example.entity` |
| generationType | generation type for entity identities | enum value of `org.seasar.doma.gradle.codegen.desc.GenerationType` | |
| namingType | naming convention | enum value of org.`seasar.doma.gradle.codegen.desc.NamingType` | |
| initialValue | initial value for entity identities |  | |
| allocationSize | allocation size for entity identities |  | |
| showCatalogName | whether to show catalog names or not |  | `false` |
| showSchemaName | whether to show schema names or not |  | `false` |
| showTableName | whether to show table names or not |  | `true` |
| showColumnName | whether to show column names or not |  | `true` |
| showDbComment | whether to show database comments or not |  | `true` |
| useAccessor | whether to use accessors or not |  | `true` |
| useListener | whether to use listeners or not |  | `true` |
| originalStatesPropertyName | property to be annotated with `@OriginalStates` |  | |
| entityPropertyClassNamesFile | file used to resolve entity property classes |  | |
| prefix | prefix for entity classes |  | |
| suffix | suffix for entity classes |  | |

### dao

A `dao` block must be under a named config:

```groovy
domaCodeGen {
  sales {
    dao {
      packageName = 'org.example.sales.dao'
    }
  }
}
```

| Option | Description | Values | Default |
| :--- | :--- | :--- | :--- |
| overwrite | whether to overwrite generated DAO files or not |  | `false` |
| packageName | package name for generated DAO classes |  | `example.dao` |
| suffix | suffix for Dao classes |  | `Dao` |
| configClassName | `org.seasar.doma.jdbc.Config` implemented class name. Tha name is used at @Dao |  | `false` |

### sql

A `sql` block must be under a named config:

```groovy
domaCodeGen {
  sales {
    sql {
      overwrite = false
    }
  }
}
```

| Option | Description | Values | Default |
| :--- | :--- | :--- | :--- |
| overwrite | whether to overwrite generated sql files or not |  | `true` |


Customization
-------------

### Using custom template files

Default template files are located in the source code repository of the Doma CodeGen Plugin.  
They are listed as follows:

| Template File | Data Model Class | Generated Files |
| :--- | :--- | :--- |
| entity.ftl | org.seasar.doma.gradle.codege.desc.EntityDesc | entity source files|
| entityListener.ftl | org.seasar.doma.gradle.codege.desc.EntityListenerDesc | entity listener source files |
| dao.ftl | org.seasar.doma.gradle.codege.desc.DaoDesc | DAO source files |
| sqlTestCase.ftl | org.seasar.doma.gradle.codege.desc.SqlTestDesc | test source files for SQL |
| selectById.sql.ftl | org.seasar.doma.gradle.codege.desc.SqlDesc | SQL files |
| selectByIdAndVersion.sql.ftl | org.seasar.doma.gradle.codege.desc.SqlDesc | SQL files |

To create custom template files, copy them and modify their contents without changing file names.
Then put them in the directory which is specified to the `templateDir` option.

```groovy
domaCodeGen {
    dev {
        url = '...'
        user = '...'
        password = '...'
        // specify the directory including your custom template files
        templateDir = "$projectDir/template"
        entity {
          packageName = 'org.example.entity'
        }
        dao {
          packageName = 'org.example.dao'
        }
    }
}
```

The Doma CodeGen Plugin uses [Apache FreeMarker](https://freemarker.apache.org/) to process the template files.

Sample Project
--------------

- [codegen-sample](https://github.com/domaframework/codegen-sample)

License
-------

```
Copyright 2020 domaframework.org

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

import org.seasar.doma.gradle.codegen.desc.LanguageType

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
    dependencies {
        classpath("org.domaframework.doma:codegen")
    }
}

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("org.domaframework.doma.compile") version "4.0.0"
    id("org.domaframework.doma.codegen")
    id("com.nocwriter.runsql") version "1.0.3"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    val domaVersion: String by project
    val h2Version: String by project

    implementation("org.seasar.doma:doma-core:$domaVersion")
    annotationProcessor("org.seasar.doma:doma-processor:$domaVersion")

    // Use JUnit BOM for version management
    testImplementation(platform("org.junit:junit-bom:5.13.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("com.h2database:h2:$h2Version")

    // Add H2 to the domaCodeGen configuration
    domaCodeGen("com.h2database:h2:$h2Version")

    // Used by runsql-gradle-plugin
    runtimeOnly("com.h2database:h2:$h2Version")
}

val _url = "jdbc:h2:file:$projectDir/data/db"
val _user = ""
val _password = ""

domaCodeGen {
    register("java") {
        val basePackage = "codegen"
        url.set(_url.toString())
        user.set(_user)
        password.set(_password)
        entity {
            packageName = "${basePackage}.entity"
        }
        dao {
            packageName = "${basePackage}.dao"
        }
    }
    register("kotlin") {
        val basePackage = "codegen"
        url.set(_url.toString())
        user.set(_user)
        password.set(_password)
        languageType.set(LanguageType.KOTLIN)
        entity {
            packageName = "${basePackage}.entity"
        }
        dao {
            packageName = "${basePackage}.dao"
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    val deleteSrc = register("deleteSrc") {
        doLast {
            delete("src")
        }
    }

    val createDb = register<RunSQL>("createDb") {
        config {
            username = _user
            password = _password
            url = _url
            driverClassName = "org.h2.Driver"
            script = """
            drop all objects;

            create table department(
                department_id integer not null primary key, 
                department_no integer not null,
                department_name varchar(20),
                location varchar(20) default 'tokyo', 
                version integer
             );

            create table employee(
                employee_id integer not null primary key, 
                employee_no integer not null,
                employee_name varchar(20)
            );                
            """.trimIndent()
        }
    }

    getByName("domaCodeGenJavaDbMeta").dependsOn(deleteSrc, createDb)
    getByName("domaCodeGenKotlinDbMeta").dependsOn(deleteSrc, createDb)
}

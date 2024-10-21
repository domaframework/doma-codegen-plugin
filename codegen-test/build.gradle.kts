import org.h2.Driver
import org.seasar.doma.gradle.codegen.desc.LanguageType
import org.seasar.doma.gradle.codegen.jdbc.SimpleDataSource

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
    dependencies {
        val h2Version: String = properties["h2Version"] as String
        classpath("org.domaframework.doma:codegen")
        classpath("com.h2database:h2:$h2Version")
        classpath("mysql:mysql-connector-java:8.0.33")
    }
}

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.domaframework.doma.compile") version "3.0.1"
    id("org.domaframework.doma.codegen")
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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    testRuntimeOnly("com.h2database:h2:$h2Version")
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

    val createDb = register("createDb") {
        doLast {
            val ds = SimpleDataSource()
            ds.setDriver(Driver())
            ds.setUrl(_url)
            ds.setUser(_user)
            ds.setPassword(_password)

            val sql = """
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
            """
            ds.connection.use {
                it.createStatement().use {
                    it.execute(sql)
                }
            }
        }
    }

    getByName("domaCodeGenJavaDbMeta").dependsOn(deleteSrc, createDb)
    getByName("domaCodeGenKotlinDbMeta").dependsOn(deleteSrc, createDb)
}

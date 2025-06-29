import org.seasar.doma.gradle.codegen.desc.LanguageType

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.doma.compile)
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
    implementation(libs.doma.core)
    annotationProcessor(libs.doma.processor)

    // Use JUnit BOM for version management
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    testRuntimeOnly(platform(libs.testcontainers.bom))
    testRuntimeOnly(libs.testcontainers.postgresql)
    testRuntimeOnly(libs.postgresql)

    domaCodeGen(platform(libs.testcontainers.bom))
    domaCodeGen(libs.testcontainers.postgresql)
    domaCodeGen(libs.postgresql)
}

val initScript = file("init_postgresql.sql")
val _url = "jdbc:tc:postgresql:13.21:///test?TC_INITSCRIPT=file:${initScript.absolutePath}"
val _user = ""
val _password = ""

domaCodeGen {
    register("java") {
        val basePackage = "codegen"
        url = _url
        user = _user
        password = _password
        entity {
            packageName = "${basePackage}.j.entity"
        }
        dao {
            packageName = "${basePackage}.j.dao"
        }
    }
    register("kotlin") {
        val basePackage = "codegen"
        url = _url
        user = _user
        password = _password
        languageType = LanguageType.KOTLIN
        entity {
            packageName = "${basePackage}.k.entity"
        }
        dao {
            packageName = "${basePackage}.k.dao"
        }
    }
}

tasks {
    compileJava {
        dependsOn("domaCodeGenJavaAll")
    }

    compileKotlin {
        dependsOn("domaCodeGenKotlinAll")
    }

    test {
        useJUnitPlatform()
    }

    val deleteSrc = register("deleteSrc") {
        doLast {
            delete("src/main/java/codegen")
            delete("src/main/kotlin/codegen")
            delete("src/main/resources/META-INF/codegen")
            delete("src/test/java/codegen")
            delete("src/test/kotlin/codegen")
        }
    }

    clean {
        dependsOn(deleteSrc)
    }
}
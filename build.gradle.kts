import org.seasar.doma.gradle.codegen.desc.LanguageType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("org.domaframework.doma.compile") version "4.0.0"
    id("org.domaframework.doma.codegen")
    id("com.nocwriter.runsql") version "1.0.3"
    id("net.researchgate.release") version "3.1.0"
}

configure<net.researchgate.release.ReleaseExtension> {
    newVersionCommitMessage.set("[Gradle Release Plugin] - [skip ci] new version commit: ")
    tagTemplate.set("v\$version")
    git {
        requireBranch.set("master")
    }
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

    implementation("org.seasar.doma:doma-core:$domaVersion")
    annotationProcessor("org.seasar.doma:doma-processor:$domaVersion")

    // Use JUnit BOM for version management
    testImplementation(platform("org.junit:junit-bom:5.13.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testRuntimeOnly(platform("org.testcontainers:testcontainers-bom:1.21.2"))
    testRuntimeOnly("org.testcontainers:postgresql")
    testRuntimeOnly("org.postgresql:postgresql:42.7.7")

    domaCodeGen(platform("org.testcontainers:testcontainers-bom:1.21.2"))
    domaCodeGen("org.testcontainers:postgresql")
    domaCodeGen("org.postgresql:postgresql:42.7.7")
}

val initScript = file("init_postgresql.sql")
val _url = "jdbc:tc:postgresql:13.21:///test?TC_INITSCRIPT=file:${initScript.absolutePath}"
val _user = ""
val _password = ""

domaCodeGen {
    register("java") {
        val basePackage = "codegen"
        url.set(_url)
        user.set(_user)
        password.set(_password)
        entity {
            packageName = "${basePackage}.j.entity"
        }
        dao {
            packageName = "${basePackage}.j.dao"
        }
    }
    register("kotlin") {
        val basePackage = "codegen"
        url.set(_url)
        user.set(_user)
        password.set(_password)
        languageType.set(LanguageType.KOTLIN)
        entity {
            packageName = "${basePackage}.k.entity"
        }
        dao {
            packageName = "${basePackage}.k.dao"
        }
    }
}

tasks {
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

    build {
        mustRunAfter("domaCodeGenJavaAll", "domaCodeGenKotlinAll")
    }
}
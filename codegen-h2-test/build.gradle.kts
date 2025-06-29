plugins {
    java
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

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(libs.h2)

    domaCodeGen(libs.h2)
}

val initScript = file("init_h2.sql")
val _url = "jdbc:h2:mem:tets;INIT=RUNSCRIPT FROM 'file:${initScript.absolutePath}'"
val _user = ""
val _password = ""

domaCodeGen {
    register("h2") {
        val basePackage = "codegen"
        url = _url
        user = _user
        password = _password
        schemaName = "PUBLIC"
        entity {
            packageName = "${basePackage}.entity"
        }
        dao {
            packageName = "${basePackage}.dao"
        }
    }
}

tasks {
    compileJava {
        dependsOn("domaCodeGenH2All")
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
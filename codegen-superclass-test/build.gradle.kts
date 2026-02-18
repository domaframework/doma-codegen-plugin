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
    // Add compiled classes to domaCodeGen classpath for superclass resolution
    domaCodeGen(sourceSets.main.get().output)
}

val initScript = file("init_h2.sql")
val _url = "jdbc:h2:mem:superclass_test;INIT=RUNSCRIPT FROM 'file:${initScript.absolutePath}'"
val _user = ""
val _password = ""

domaCodeGen {
    register("superclass") {
        val basePackage = "codegen"
        url = _url
        user = _user
        password = _password
        schemaName = "PUBLIC"
        entity {
            packageName = "${basePackage}.entity"
            // Use AbstractEntity as superclass
            superclassName = "base.AbstractEntity"
            // Use AbstractEntityListener as listener superclass
            listenerSuperclassName = "base.AbstractEntityListener"
        }
        dao {
            packageName = "${basePackage}.dao"
        }
    }
}

tasks {
    // Step 1: Create a task to compile only base classes first
    val compileBaseClasses = register<JavaCompile>("compileBaseClasses") {
        source = fileTree("src/main/java") {
            include("**/base/**")
        }
        classpath = configurations.compileClasspath.get()
        destinationDirectory.set(layout.buildDirectory.dir("classes/java/main"))
    }

    // Step 2: domaCodeGen depends on base classes being compiled
    matching { it.name.startsWith("domaCodeGen") }.configureEach {
        dependsOn(compileBaseClasses)
    }

    // Step 3: Full compilation depends on code generation
    compileJava {
        dependsOn("domaCodeGenSuperclassAll")
    }

    test {
        useJUnitPlatform()
    }

    val deleteSrc = register("deleteSrc") {
        doLast {
            // Delete only generated code, not base classes
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



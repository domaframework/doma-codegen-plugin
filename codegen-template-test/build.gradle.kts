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

domaCodeGen {
    register("customTemplate") {
        url = "jdbc:h2:mem:template_test;INIT=RUNSCRIPT FROM 'file:${initScript.absolutePath}'"
        user = "sa"
        password = ""
        schemaName = "PUBLIC"

        // Use custom templates
        templateDir = file("src/main/resources/custom-templates")
        
        // Generate both entity and dao
        entity {
            packageName = "com.example.template.entity"
        }
        dao {
            packageName = "com.example.template.dao"
        }
    }
}

tasks {
    compileJava {
        dependsOn("domaCodeGenCustomTemplateAll")
    }
    
    compileTestJava {
        dependsOn(compileJava)
    }
    
    test {
        useJUnitPlatform()
    }
    
    val deleteSrc = register("deleteSrc") {
        doLast {
            delete("src/main/java/com/example/template/entity")
            delete("src/main/java/com/example/template/dao")
            delete("src/main/resources/META-INF/com/example/template/dao")
            delete("src/test/java/com/example/template/dao")
        }
    }
    
    clean {
        dependsOn(deleteSrc)
    }
}
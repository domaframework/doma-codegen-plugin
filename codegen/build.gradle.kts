plugins {
    groovy
    `java-gradle-plugin`
    alias(libs.plugins.spotless)
    alias(libs.plugins.plugin.publish)
}

gradlePlugin {
    website.set("https://github.com/domaframework/doma-codegen-plugin")
    vcsUrl.set("https://github.com/domaframework/doma-codegen-plugin.git")
    plugins {
        create("codegenPlugin") {
            id = "org.domaframework.doma.codegen"
            displayName = "Doma Codegen Plugin"
            description = "Generates Java, Kotlin, and SQL files from Database"
            implementationClass = "org.seasar.doma.gradle.codegen.CodeGenPlugin"
            tags.set(listOf("doma", "generator"))
        }
    }
}

sourceSets {
    main {
        java {
            setSrcDirs(emptyList<String>()) 
        }
        groovy {
            setSrcDirs(listOf("src/main/groovy", "src/main/java"))
        }
    }
}

spotless {
    java {
        googleJavaFormat(libs.versions.googleJavaFormat.get())
    }
    groovy {
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of( 17))
}


repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(libs.freemarker)
    testImplementation(libs.doma.core)
    
    // Use JUnit BOM for version management
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    test {
        useJUnitPlatform()
    }
    
    javadoc {
        enabled = false
    }

    groovydoc {
        enabled = false
    }

    build {
        dependsOn(spotlessApply)
    }
}

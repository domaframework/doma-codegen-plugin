plugins {
    id("groovy")
    id("java-gradle-plugin")
    id("com.diffplug.spotless") version "7.0.3"
    id("com.gradle.plugin-publish") version "1.3.1"
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
        googleJavaFormat("1.23.0")
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
    implementation("org.freemarker:freemarker:2.3.34")
    testImplementation("org.seasar.doma:doma-core:3.7.0")
    
    // Use JUnit BOM for version management
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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

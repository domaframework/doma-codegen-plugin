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
    testImplementation("org.seasar.doma:doma-core:3.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
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

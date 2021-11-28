plugins {
    id("groovy")
    id("java-gradle-plugin")
    id("com.diffplug.spotless") version "5.15.0"
    id("com.gradle.plugin-publish") version "0.18.0"
}

gradlePlugin {
    plugins {
        create("codegenPlugin") {
            id = "org.seasar.doma.codegen"
            displayName = "Doma Codegen Plugin"
            description = "Generates Java, Kotlin, and SQL files from Database"
            implementationClass = "org.seasar.doma.gradle.codegen.CodeGenPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/domaframework/doma-codegen-plugin"
    vcsUrl = "https://github.com/domaframework/doma-codegen-plugin.git"
    tags = listOf("doma", "generator")
}

sourceSets {
    main {
        java {
            setSrcDirs(emptyList<String>())
        }
        withConvention(GroovySourceSet::class) {
            groovy {
                setSrcDirs(listOf("src/main/groovy", "src/main/java"))
            }
        }
    }
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
    groovy {
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("org.freemarker:freemarker:2.3.31")
    testImplementation("org.seasar.doma:doma-core:2.50.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks {
    test {
        useJUnitPlatform()
    }

    groovydoc {
        enabled = false
    }

    build {
        dependsOn(spotlessApply)
    }
}

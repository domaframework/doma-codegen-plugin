plugins {
    java
    id("net.researchgate.release") version "3.0.2"
}

configure<net.researchgate.release.ReleaseExtension> {
    newVersionCommitMessage.set("[Gradle Release Plugin] - [skip ci] new version commit: ")
    tagTemplate.set("v\$version")
    git {
        requireBranch.set("master")
    }
}

allprojects {
    apply(plugin = "java")
    tasks {
        test {
            useJUnitPlatform()
        }
    }
}

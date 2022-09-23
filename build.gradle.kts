plugins {
    java
    id("net.researchgate.release") version "3.0.2"
}

configure<net.researchgate.release.ReleaseExtension> {
    newVersionCommitMessage = "[Gradle Release Plugin] - [skip ci] new version commit: "
    tagTemplate = "v\$version"
}

allprojects {
    apply(plugin = "java")
    tasks {
        test {
            useJUnitPlatform()
        }
    }
}

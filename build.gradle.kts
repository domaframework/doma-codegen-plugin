plugins {
    java
    id("net.researchgate.release") version "2.8.1"
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

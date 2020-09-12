plugins {
    base
    id("net.researchgate.release") version "2.8.1"
}

configure<net.researchgate.release.ReleaseExtension> {
    newVersionCommitMessage = "[Gradle Release Plugin] - [skip ci] new version commit: "
}

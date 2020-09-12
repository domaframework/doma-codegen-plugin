# Release Operations

## Run the Gradle release task

The Gradle release task creates a release commit and push it to the origin/master branch.

```
$ git checkout master
$ git pull
$ ./gradlew release -Prelease.releaseVersion=1.0.0 -Prelease.newVersion=1.1.0-SNAPSHOT
```

The value of `release.releaseVersion` is decided by the draft name of
[Releases](https://github.com/domaframework/doma/releases).

## Build and Publish with GitHub Action

(No operation required)

The GitHub Action workflow [Java CI with Gradle](.github/workflows/ci.yml) handles the above push event
and publishes to the [Gradle Plugin Portal](https://plugins.gradle.org/).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma-compile-plugin/releases)
and publish release notes.

## Announce the release

Announce the release of new version using Twitter.
- [@domaframework](https://twitter.com/domaframework)

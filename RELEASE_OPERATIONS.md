# Release Operations

## Dispatch the release workflow

Dispatch the [release workflow](.github/workflows/release.yml) as follows:

```
$ gh api repos/domaframework/doma-codegen-plugin/actions/workflows/release.yml/dispatches -F ref='master'
```

## Build and Publish with GitHub Action

(No operation required)

The [CI](.github/workflows/ci.yml) workflow follows the above release workflow
and publishes the doma-codegen-plugin to the [Gradle Plugin Portal](https://plugins.gradle.org/).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma-compile-plugin/releases)
and publish release notes.

## Announce the release

Announce the release of new version using Twitter.
- [@domaframework](https://twitter.com/domaframework)

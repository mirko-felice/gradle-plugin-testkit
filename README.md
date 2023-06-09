# Gradle Plugin Testkit

### Code Quality

[![CI + CD](https://github.com/mirko-felice/gradle-plugin-testkit/actions/workflows/ci-and-cd.yml/badge.svg)](https://github.com/mirko-felice/gradle-plugin-testkit/actions/workflows/ci-and-cd.yml)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mirko-felice_gradle-plugin-testkit&metric=security_rating)](https://sonarcloud.io/summary/overall?id=mirko-felice_gradle-plugin-testkit)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=mirko-felice_gradle-plugin-testkit&metric=bugs)](https://sonarcloud.io/summary/overall?id=mirko-felice_gradle-plugin-testkit)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=mirko-felice_gradle-plugin-testkit&metric=code_smells)](https://sonarcloud.io/summary/overall?id=mirko-felice_gradle-plugin-testkit)
[![codecov](https://codecov.io/gh/mirko-felice/gradle-plugin-testkit/branch/master/graph/badge.svg?token=TCU6QY2RD5)](https://codecov.io/gh/mirko-felice/gradle-plugin-testkit)

### Modules

[![Latest Maven Core Release](https://img.shields.io/maven-central/v/io.github.mirko-felice.testkit/core?label=core&logo=apachemaven)](https://central.sonatype.com/artifact/io.github.mirko-felice.testkit/core)
[![Latest Gradle Plugin Release](https://img.shields.io/gradle-plugin-portal/v/io.github.mirko-felice.testkit?color=blue&label=gradle-plugin&logo=gradle)](https://plugins.gradle.org/plugin/io.github.mirko-felice.testkit)
#### Relative docs

[![Latest Core Doc](https://javadoc.io/badge2/io.github.mirko-felice.testkit/core/javadoc.svg?label=core-doc)](https://javadoc.io/doc/io.github.mirko-felice.testkit/core)
[![Latest Gradle Plugin Doc](https://javadoc.io/badge2/io.github.mirko-felice.testkit/gradle-plugin/javadoc.svg?label=gradle-plugin-doc)](https://javadoc.io/doc/io.github.mirko-felice.testkit/gradle-plugin)

## Core

### Purpose

This library aims to help users to test own Gradle plugins, providing a declarative way
to do that.

### Usage

This library provides one main _API_: [`Testkit`](https://github.com/mirko-felice/gradle-plugin-testkit/blob/master/core/src/main/kotlin/io/github/mirkofelice/api/Testkit.kt).

It can be used in your own tests like below.

```kotlin
class ExampleTest : StringSpec({
    
    "Example Test" {
        Testkit.test(projectName)
    }   
})
```

It uses **_yaml_** files to declare the tests. More info [below](#yaml-structure).

It uses a [`CheckerType`](https://github.com/mirko-felice/gradle-plugin-testkit/blob/master/src/main/kotlin/io/github/mirkofelice/api/CheckerType.kt) 
to know which subclass of [`TestkitChecker`](https://github.com/mirko-felice/gradle-plugin-testkit/blob/master/core/src/main/kotlin/io/github/mirkofelice/checkers/TestkitChecker.kt)
use to apply the various checks.

At the moment the project provides these types of checker:

- **KOTLIN**: refers to the [`KotlinChecker`](https://github.com/mirko-felice/gradle-plugin-testkit/blob/master/core/src/main/kotlin/io/github/mirkofelice/checkers/KotlinChecker.kt)
  which uses the basic Kotlin assertions.

#### Configuration

The core function `test()` provides four parameters to give the user the capability to 
configure the feature.

- **projectName**: parameter describing the name of the project used to search for plugin classpath.

- **projectFolderPath**: parameter describing the path of the folder containing the _yaml_ file.
  Default to `currentDirectory/src/main/resources/`.

- **checkerType**: parameter describing the _CheckerType_ to use.\
  Default to `CheckerType.KOTLIN`.

- **forwardOutput**: parameter describing if the user wants to see the Gradle build output or not.\
  Default to `false`.

#### Yaml Structure

Yaml files have to be structured in a specific way.

Below there is a complete example.

```yaml
tests:
  - description: "Example description"
    id: uniqueIdentifier
    requires: idOfAnotherTest
    configuration:
      tasks:
        - task1
        - task2
        - task3
      options:
        - option1
        - option2
    expectation:
      result: success
      outcomes:
        success:
          - successTask
        failed:
          - failedTask
        upToDate:
          - upToDateTask
        skipped:
          - skippedTask
        fromCache:
          - fromCacheTask
        noSource:
          - noSourceTask
        notExecuted:
          - notExecutedTask
      output:
        contains:
          - firstPartialOutput
          - secondPartialOutput
        doesntContain:
          - firstPartialOutput
          - secondPartialOutput
      files:
        existing:
          - name: "test.txt"
            content: "Example content"
            permissions:
              - R
              - W
              - X
            contentRegex: "$regex"
```

#### Class diagrams

[![PlantUML core class diagram](https://shorturl.at/dlqEV)](https://shorturl.at/fsTU4)

[![PlantUML plugin class diagram](https://shorturl.at/otzP6)](https://shorturl.at/qEHLY)

## Gradle-Plugin

### Usage

To facilitate the user to use this library, a Gradle Plugin has been created.

#### Apply

In order to use the plugin, you should apply it in your `build.gradle.kts`.

```kotlin
plugins {
    id("io.github.mirko-felice.testkit") version "<x.y.z>"
}
```

If not already applied, this plugin automatically applies [`java-gradle-plugin`](https://docs.gradle.org/current/userguide/java_gradle_plugin.html).

#### Configuration

To configure the plugin you can use the extension like below.

```kotlin
testkit {
    checkerType.set(CheckerType.KOTLIN)
    forwardOutput.set(true)
    folders {
        withMainDefault()
        withTestDefault()
        projectFolder("path/to/yaml")
    }
    tests {
        folder = file("path/to/folder")
        test("description") {
            ...
        }
    }
}
```

The plugin provides a set of properties/methods, using two main DSL.

##### Required

No required property/method. That means no tests will be run.

##### Optional

Two main properties:

- **checkerType**: property describing the _CheckerType_ to use for **ALL** tests.\
  Default to `CheckerType.KOTLIN`.

- **forwardOutput**: property describing if the user wants to see the Gradle build output or not.\
  Default to `false`.\
  **_BE CAREFUL_**: even if this is set to `true`, you should run the task with the
  appropriate option _-q_, in order to get the output sorted correctly.\
  Example: `./gradlew testkit -q`

Using the folder DSL, folders can be added inside `folders { }` block:

- **withMainDefault()**: method to add the sub-folders into the default main path (_src/main/resources_).

- **withTestDefault()**: method to add the sub-folders into the default test path (_src/test/resources_).

- **projectFolder(path: String)**: method to add a folder with the given path, always starting from the project directory.

- **genericFolder(path: String)**: method to add a folder with the given path, can be located anywhere.

- **subFoldersOfProject(path: String)**: method to add all the sub-folders in the given path, always starting from the project directory.

- **subFoldersOf(path: String)**: method to add all the sub-folders in the given path, can be located anywhere.

These folders must contain the _yaml_ file and the _build.gradle.kts_.

Using the test DSL, tests can be created inside `tests { }` block:

- **folder**: property to set the root folder containing the _build.gradle.kts_, always starting from the project directory.

- **test(description: String) { }**: method to add a new test with the given description, followed by its configuration.
  This provides a full DSL to mirror the yaml structure.

#### Tasks

This plugin creates the following tasks:

- **testkitFolders**: task able to run the testkit library using the _folder_ DSL configuration.

- **testkitDSL**: task able to run the testkit library using the _test_ DSL configuration.

- **testkit**: global task to execute all the testkit tasks.

## License

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fmirko-felice%2Fgradle-plugin-testkit-core.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fmirko-felice%2Fgradle-plugin-testkit-core)

Licensed under the [MIT License](LICENSE).

### License Compliance

<div>
This project uses <code>npm</code> to perform

<a href="https://github.com/semantic-release/semantic-release">
    <img src="https://img.shields.io/badge/semantic--release-angular-e10079?logo=semantic-release" 
      style="vertical-align:middle" alt="semantic-release: angular"/>
</a>
and does not modify its source code, so following authors' credits:

- `npm` created by The Perl Foundation <https://www.perlfoundation.org/>, licensed
  under an Artistic-2.0 <https://opensource.org/licenses/Artistic-2.0>
- `glob`'s logo created by Tanya Brassie <http://tanyabrassie.com/>, licensed
  under a Creative Commons Attribution-ShareAlike 4.0 International License
  <https://creativecommons.org/licenses/by-sa/4.0/> with copyright notice ->
  'Copyright (c) 2009-2022 Isaac Z. Schlueter and Contributors'
- `spdx-exceptions` created by Kyle E. Mitchell <https://kemitchell.com/>, licensed
  under a Creative Commons Attribution 3.0 <https://creativecommons.org/licenses/by/3.0/>
- `diff` uses [IPA Font License](licenses/IPA%20Font%20License%20(IPA))

</div>

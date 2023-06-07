/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class PluginTest {

    @TempDir
    lateinit var tempDir: File
    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        buildFile = tempDir.resolve("build.gradle.kts")
    }

    @Test
    fun completeConfiguration() {
        val buildContent = """
            import io.github.mirkofelice.structure.BuildResult
            import io.github.mirkofelice.structure.Permission
            
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
            file("emptyDir/build.gradle.kts").createNewFile()
            
            testkit {
                folders {
                    withMainDefault()
                    withTestDefault()
                    genericFolder(System.getProperty("user.dir") + "../tests/src/test/resources")
                    subFoldersOfProject("src/test/resources")
                    subFoldersOf(file(System.getProperty("user.dir")).parentFile.path + "/tests/src/test/resources/generateFile/basic")
                }
                tests {
                    folder = "emptyDir"
                    println(folder)
                    test("example") {
                        configuration {
                            tasks = emptyList()
                            println(tasks)
                            options = emptyList()
                            println(options)
                        }
                        expectation {
                            result = BuildResult.SUCCESS
                            println(result)
                            outcomes {
                                success = emptyList()
                                println(success)
                                failed = emptyList()
                                println(failed)
                                skipped = emptyList()
                                println(skipped)
                                noSource = emptyList()
                                println(noSource)
                                fromCache = emptyList()
                                println(fromCache)
                                upToDate = emptyList()
                                println(upToDate)
                                notExecuted = emptyList()
                                println(notExecuted)
                            }
                            output {
                                contains = emptyList()
                                println(contains)
                                doesntContain = emptyList()
                                println(doesntContain)
                            }
                            files {
                                existing {
                                    file("build.gradle.kts") {
                                        content = ""
                                        println(content)
                                        contentRegex = emptyList()
                                        println(contentRegex)
                                        permissions = listOf(Permission.R)
                                        println(permissions)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
        """.trimIndent()

        val settings = """
            rootProject.name = "tests"
        """.trimIndent()
        tempDir.resolve("settings.gradle.kts").writeText(settings)

        tempDir.resolve("emptyDir").mkdir()

        runGradleBuild(buildContent, false)
    }

    @Test
    fun withJavaGradlePlugin() {
        val buildContent = """
            plugins {
                `java-gradle-plugin`
                id("io.github.mirko-felice.testkit")
            }
        """.trimIndent()
        runGradleBuild(buildContent, false)
    }

    @Test
    fun wrongFolder() {
        val buildContent = """
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
            testkit {
                tests {
                    folder = System.getProperty("user.dir") + "/src/main"
                }
            }
        """.trimIndent()
        runGradleBuild(buildContent, false)
    }

    @Test
    fun wrongFolderAsFile() {
        val buildContent = """
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
            testkit {
                tests {
                    folder = System.getProperty("user.dir") + "/src/main/kotlin/io/github/mirkofelice/plugin/TestkitPlugin.kt"
                }
            }
        """.trimIndent()
        runGradleBuild(buildContent, false)
    }

    @Test
    fun wrongSubFolder() {
        val buildContent = """
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
             testkit {
                folders {
                    subFoldersOf(file(System.getProperty("user.dir")).parentFile.path + "/tests/src/test/resources/generateFile/basic")
                }
            }
        """.trimIndent()
        runGradleBuild(buildContent, true)
    }

    @Test
    fun wrongSubFolderAsFile() {
        val buildContent = """
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
             testkit {
                folders {
                    subFoldersOfProject("src/test/resources/build.gradle.kts")
                }
            }
        """.trimIndent()
        runGradleBuild(buildContent, true)
    }

    @Test
    fun wrongSubFolderMissingGradle() {
        val buildContent = """
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
             testkit {
                folders {
                    subFoldersOfProject("src/test/resources/missGradle")
                }
            }
        """.trimIndent()
        runGradleBuild(buildContent, true)
    }

    @Test
    fun wrongSubFolderMissingYaml() {
        val buildContent = """
            plugins {
                id("io.github.mirko-felice.testkit")
            }
            
             testkit {
                folders {
                    subFoldersOfProject("src/test/resources/missYaml")
                }
            }
        """.trimIndent()
        runGradleBuild(buildContent, true)
    }

    private fun runGradleBuild(buildContent: String, failing: Boolean) {
        buildFile.writeText(buildContent)

        val testkitProperties = javaClass.classLoader.getResource("testkit-gradle.properties")?.readText()
        if (testkitProperties != null) {
            tempDir.resolve("gradle.properties").writeText(testkitProperties)
        }
        val runner = GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("testkit")

        if (failing) runner.buildAndFail() else runner.build()
    }
}

/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.tasks

import io.github.mirkofelice.api.Testkit
import io.github.mirkofelice.dsl.test.TestkitTests
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import java.io.File

/**
 * Main [Task][org.gradle.api.Task] of the plugin able to run the testkit according to the provided configuration.
 */
open class TestkitTestsTask : TestkitTask() {

    /**
     * [Property] describing the [TestkitTests] to use.
     */
    @Input
    val tests: Property<TestkitTests> = project.objects.property()

    /**
     * Runs the tests from the specified configurations.
     */
    @TaskAction
    fun run() {
        val tests = tests.get().convert()
        Testkit.test(
            tests,
            project.projectDir.path + File.separator + this.tests.get().folder,
            project.buildDir.absolutePath,
            checkerType.get(),
            forwardOutput.get(),
        )
    }
}

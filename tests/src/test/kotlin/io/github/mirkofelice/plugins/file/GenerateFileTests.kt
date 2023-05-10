/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugins.file

import io.github.mirkofelice.api.TestkitRunner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class GenerateFileTests : StringSpec({

    "Basic Test" {
        TestkitRunner.runTests("generateFile/basic")
    }

    "Content Test" {
        TestkitRunner.runTests("generateFile/content")
    }

    "Permissions Test" {
        TestkitRunner.runTests("generateFile/permissions")
    }

    "Regex Test" {
        TestkitRunner.runTests("generateFile/regex")
    }

    "Wrong permissions Test" {
        shouldThrow<AssertionError> {
            TestkitRunner.runTests("generateFile/wrongPermissions")
        }
    }

    "Wrong content Test" {
        shouldThrow<AssertionError> {
            TestkitRunner.runTests("generateFile/wrongContent")
        }
    }

    "Wrong content regex Test" {
        shouldThrow<AssertionError> {
            TestkitRunner.runTests("generateFile/wrongContentRegex")
        }
    }

})
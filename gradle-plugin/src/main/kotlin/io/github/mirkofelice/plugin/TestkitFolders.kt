/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import java.io.File
import java.io.Serializable
import javax.inject.Inject

/**
 * Represents the folders containing the tests files.
 */
@TestkitFolderDSL
open class TestkitFolders @Inject constructor(private val objects: ObjectFactory) : Serializable {

    internal val folders: ListProperty<TestkitFolder> = objects.listProperty()

    /**
     * Adds a new folder.
     * @param path path of the folder containing the tests files
     */
    fun folder(path: String) {
        folders.add(objects.newInstance(TestkitFolder::class, objects.property(String::class).value(path)))
    }

    /**
     * Automatically adds the folder with the default path ('src/main/resources').
     */
    fun withDefault() {
        folder(DEFAULT_PATH)
    }

    private companion object {
        private const val serialVersionUID = 1L
        private val sep = File.separator
        private val DEFAULT_PATH = "src${sep}main${sep}resources$sep"
    }
}

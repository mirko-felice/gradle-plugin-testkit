/*
 * Copyright (c) 2023, Mirko Felice. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.mirkofelice.structure

import java.io.File

/**
 * Represents the permission of a file.
 * @property hasPermission check specific permission of the file
 */
enum class Permission(val hasPermission: File.() -> Boolean) {

    /**
     * Represents the READ permission.
     */
    R(File::canRead),

    /**
     * Represents the WRITE permission.
     */
    W(File::canWrite),

    /**
     * Represents the EXECUTE permission.
     */
    X(File::canExecute),
}

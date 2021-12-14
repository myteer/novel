/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.myteer.novel.db

import com.jfilegoodies.FileGoodies
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.util.*

open class DatabaseMeta(val name: String, val file: File? = null) {
    constructor(file: File) : this(FilenameUtils.getBaseName(file.name), file)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (null == other || javaClass != other.javaClass) return false
        return file == (other as DatabaseMeta).file
    }

    override fun hashCode(): Int {
        return Objects.hashCode(file?.toString() ?: name)
    }

    override fun toString(): String {
        return String.format("%s (%s)", name, file?.let { FileGoodies.shortenedFilePath(it, 1) } ?: "null")
    }
}
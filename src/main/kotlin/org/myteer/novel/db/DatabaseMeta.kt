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
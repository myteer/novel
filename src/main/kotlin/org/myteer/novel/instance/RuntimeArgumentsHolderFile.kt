package org.myteer.novel.instance

import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.StandardCharsets

object RuntimeArgumentsHolderFile : File(FileUtils.getTempDirectory(), "novel.runtime.params") {
    init {
        createNewFile()
    }

    fun clear() {
        try {
            FileUtils.writeStringToFile(this, "", StandardCharsets.UTF_8, false)
        } catch (ignored: Exception) { }
    }
}
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
package org.myteer.novel.gui.dbcreator

import javafx.stage.FileChooser
import javafx.stage.Window
import org.apache.commons.collections4.CollectionUtils
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.main.PropertiesSetup
import java.util.stream.Collectors

class DatabaseOpener {
    fun showOpenMultipleDialog(owner: Window?): List<DatabaseMeta> {
        val files = createFileChooser().showOpenMultipleDialog(owner)
        return if (CollectionUtils.isNotEmpty(files)) {
            files.stream().map { DatabaseMeta(it) }.collect(Collectors.toList())
        } else listOf()
    }

    fun showOpenDialog(owner: Window?): DatabaseMeta? {
        return createFileChooser().showOpenDialog(owner)?.let { DatabaseMeta(it) }
    }

    private fun createFileChooser(): FileChooser = FileChooser().also {
        val filters = getExtensionFilters()
        it.extensionFilters.addAll(filters)
        it.selectedExtensionFilter = filters[0]
    }

    private fun getExtensionFilters(): List<FileChooser.ExtensionFilter> = listOf(
        FileChooser.ExtensionFilter("Novel database files", "*." + System.getProperty(PropertiesSetup.APP_FILE_EXTENSION)),
        FileChooser.ExtensionFilter("All files", "*")
    )
}
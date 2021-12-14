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
package org.myteer.novel.export.api

import javafx.concurrent.Task
import javafx.scene.Node
import org.myteer.novel.db.data.Book
import org.myteer.novel.gui.export.ConfigurationDialog
import java.io.OutputStream

interface BookExporter<C : BookExportConfiguration> {
    val name: String

    val icon: Node

    val configurationDialog: ConfigurationDialog<C>

    val contentType: String

    val contentTypeDescription: String

    fun write(items: List<Book>, output: OutputStream, config: C, observer: ExportProcessObserver)

    fun task(items: List<Book>, output: OutputStream, config: C): Task<Unit> = object : Task<Unit>() {
        override fun call() {
            let { taskObj ->
                write(items, output, config, object : ExportProcessObserver {
                    override fun updateMessage(message: String?) = taskObj.updateMessage(message)
                    override fun updateProgress(workDone: Double, max: Double) = taskObj.updateProgress(workDone, max)
                    override fun updateTitle(title: String?) = taskObj.updateTitle(title)
                })
            }
        }
    }
}
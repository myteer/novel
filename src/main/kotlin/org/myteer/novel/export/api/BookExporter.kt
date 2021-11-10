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
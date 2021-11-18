package org.myteer.novel.gui.volume

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.scene.layout.BorderPane
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.db.repository.ChapterRepository
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.runOutsideUI
import org.myteer.novel.gui.utils.scrollPane
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class VolumeView(
    private val context: Context,
    private val database: NitriteDatabase,
    private val bookId: String
) : BorderPane() {
    private val baseItems: ObservableList<Chapter> = FXCollections.observableArrayList()
    private val volumeViewBase = VolumeViewBase(baseItems)
    private val toolBar = BiToolBar()

    init {
        buildUI()
        loadRecords(300)
    }

    private fun buildUI() {
        top = toolBar
        center = scrollPane(volumeViewBase, fitToWidth = true)
    }

    private fun loadRecords(delayMillis: Long? = null) {
        runOutsideUI(ChaptersLoadTask(delayMillis))
    }

    fun refresh() {
        loadRecords()
    }

    private inner class ChaptersLoadTask(private val delayMillis: Long? = null) : Task<List<Chapter>>() {
        init {
            setOnRunning {
                context.showIndeterminateProgress()
            }
            setOnFailed {
                context.stopProgress()
                logger.error("Failed to load chapters", it.source.exception)
                context.showErrorDialog(
                    i18n("chapter.load.failed.title"),
                    i18n("chapter.load.failed.message"),
                    it.source.exception as? Exception
                )
            }
            setOnSucceeded {
                context.stopProgress()
                baseItems.setAll(value)
            }
        }

        override fun call(): List<Chapter> {
            delayMillis?.let { Thread.sleep(it) }
            return ChapterRepository(database).selectByBookId(bookId)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(VolumeView::class.java)
    }
}
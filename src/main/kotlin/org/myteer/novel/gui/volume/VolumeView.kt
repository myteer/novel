package org.myteer.novel.gui.volume

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.concurrent.WorkerStateEvent
import javafx.scene.layout.BorderPane
import javafx.util.Duration
import org.myteer.novel.config.Preferences
import org.myteer.novel.crawl.task.ChapterQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.db.repository.BookRepository
import org.myteer.novel.db.repository.ChapterRepository
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.bookmanager.task.ChapterListRefreshTask
import org.myteer.novel.gui.utils.runOutsideUI
import org.myteer.novel.gui.utils.scrollPane
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean

class VolumeView(
    private val context: Context,
    preferences: Preferences,
    private val database: NitriteDatabase,
    private val bookId: String
) : BorderPane() {
    private val baseItems: ObservableList<Chapter> = FXCollections.observableArrayList()
    private val volumeViewBase = VolumeViewBase(context, preferences, database, baseItems)
    private val toolBar = VolumeToolBar(context, this, baseItems)
    private val cacheRunning = AtomicBoolean(false)

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

    fun cacheAll() {
        when {
            cacheRunning.get().not() -> {
                cacheRunning.set(true)
                runOutsideUI(ClearAllTask())
            }
        }
    }

    fun clearCache() {
        when {
            cacheRunning.get().not() -> {
                cacheRunning.set(true)
                runOutsideUI(ClearCacheTask())
            }
        }
    }

    fun syncChapterListFromCloud() {
        runOutsideUI(ChapterListRefreshTask(database, bookId).apply {
            addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED) {
                refresh()
            }
        })
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
                    i18n("chapters.load.failed.title"),
                    i18n("chapters.load.failed.message"),
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

    private inner class ClearAllTask : Task<Unit>() {
        init {
            setOnRunning {
                context.showIndeterminateProgress()
            }
            setOnFailed {
                cacheRunning.set(false)
                context.stopProgress()
                logger.error("Failed to cache book[id=$bookId]", it.source.exception)
                refresh()
                BookRepository(database).selectById(bookId)?.name?.let { bookName ->
                    context.showInformationNotification(
                        i18n("chapters.cache.error.title"),
                        i18n("chapters.cache.error.message", bookName),
                        Duration.seconds(10.0)
                    )
                }
            }
            setOnSucceeded {
                cacheRunning.set(false)
                context.stopProgress()
                refresh()
                BookRepository(database).selectById(bookId)?.name?.let { bookName ->
                    context.showInformationNotification(
                        i18n("chapters.cache.successful.title"),
                        i18n("chapters.cache.successful.message", bookName),
                        Duration.seconds(10.0)
                    )
                }
            }
        }

        override fun call() {
            val repository = ChapterRepository(database)
            repository.selectByBookId(bookId).filter { it.content.isNullOrBlank() }.forEach { chapter ->
                ChapterQueryTask(bookId, chapter.id!!).apply { run() }.get()!!.let {
                    chapter.previousId = it.previousId
                    chapter.nextId = it.nextId
                    chapter.hasContent = it.hasContent
                    chapter.content = it.content
                    repository.save(chapter)
                }
            }
        }
    }

    private inner class ClearCacheTask : Task<Unit>() {
        init {
            setOnRunning {
                context.showIndeterminateProgress()
            }
            setOnFailed {
                cacheRunning.set(false)
                context.stopProgress()
                logger.error("Failed to clear book[id=$bookId] cache", it.source.exception)
                context.showErrorDialog(
                    i18n("chapters.cache.clear.error.title"),
                    i18n("chapters.cache.clear.error.message"),
                    it.source.exception as? Exception
                )
            }
            setOnSucceeded {
                cacheRunning.set(false)
                context.stopProgress()
                refresh()
            }
        }

        override fun call() {
            ChapterRepository(database).clearContentCacheByBookId(bookId)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(VolumeView::class.java)
    }
}
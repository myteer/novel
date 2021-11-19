package org.myteer.novel.gui.volume.overlay

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.StringProperty
import javafx.concurrent.Task
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import org.myteer.novel.crawl.task.ChapterQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.db.repository.ChapterRepository
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.I18NButtonType
import org.myteer.novel.gui.utils.runOutsideUIAsync
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class ChapterLoadPane(
    private val context: Context,
    private val database: NitriteDatabase,
    private val overlayTitle: StringProperty,
    bookId: String,
    chapterId: String
) : StackPane() {
    private val loading: BooleanProperty = SimpleBooleanProperty(false)
    private val containerPane: StackPane = StackPane()
    private val loadingPane: StackPane = buildLoadingPane()

    init {
        styleClass.add("chapter-load-pane")
        setMinSize(300.0, 300.0)
        setPrefSize(600.0, 600.0)
        buildUI()
        loadData(bookId, chapterId)
    }

    private fun buildUI() {
        children.add(containerPane)
        children.add(loadingPane)
        loadingPane.visibleProperty().bind(loading)
    }

    @Synchronized
    fun loadData(bookId: String, chapterId: String) {
        when {
            loading.get().not() -> {
                loading.set(true)
                runOutsideUIAsync(LoadTask(bookId, chapterId))
            }
        }
    }

    private fun buildLoadingPane() = StackPane(ImageView("/org/myteer/novel/image/other/loading.gif")).apply {
        styleClass.add("loading-pane")
    }

    private inner class LoadTask(private val bookId: String, private val chapterId: String) : Task<Chapter>() {
        init {
            setOnRunning { onRunning() }
            setOnFailed { onFailed(it.source.exception) }
            setOnSucceeded { onSucceeded(value) }
        }

        private fun onRunning() {
            context.showIndeterminateProgress()
        }

        private fun onFailed(t: Throwable?) {
            loading.set(false)
            context.stopProgress()
            logger.error("Failed to load chapter", t)
            context.showErrorDialog(
                i18n("chapter.load.failed.title"),
                i18n("chapter.load.failed.message")
            ) {
                when (it) {
                    I18NButtonType.RETRY -> loadData(bookId, chapterId)
                }
            }.apply { getButtonTypes().add(I18NButtonType.RETRY) }
        }

        private fun onSucceeded(chapter: Chapter) {
            loading.set(false)
            context.stopProgress()
            overlayTitle.set(chapter.name)
            containerPane.children.setAll(buildChapterShowPane(chapter))
        }

        override fun call(): Chapter {
            val repository = ChapterRepository(database)
            val chapter = repository.selectById(chapterId)!!
            if (null == chapter.content) {
                ChapterQueryTask(bookId, chapterId).apply { run() }.get()!!.let {
                    chapter.previousId = it.previousId
                    chapter.nextId = it.nextId
                    chapter.hasContent = it.hasContent
                    chapter.content = it.content
                    repository.save(chapter)
                }
            }
            return chapter
        }
    }

    private fun buildChapterShowPane(chapter: Chapter) = ChapterShowPane(chapter, this)

    companion object {
        private val logger = LoggerFactory.getLogger(ChapterLoadPane::class.java)
    }
}
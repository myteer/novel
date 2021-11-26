package org.myteer.novel.gui.volume.chapter

import javafx.beans.property.*
import javafx.concurrent.Task
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import jfxtras.styles.jmetro.JMetroStyleClass
import org.myteer.novel.config.Preferences
import org.myteer.novel.crawl.task.ChapterQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.db.repository.ChapterContentRepository
import org.myteer.novel.db.repository.ChapterRepository
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.I18NButtonType
import org.myteer.novel.gui.utils.runOutsideUIAsync
import org.myteer.novel.gui.volume.chapter.ChapterShowConfiguration.Companion.CHAPTER_SHOW_CONFIG_KEY
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class ChapterLoadPane(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase,
    private val titleProperty: StringProperty,
    bookId: String,
    chapterId: String
) : StackPane() {
    private val loading: BooleanProperty = SimpleBooleanProperty(false)
    private val configuration: ChapterShowConfiguration = preferences.get(CHAPTER_SHOW_CONFIG_KEY)
    private val chapterProperty: ObjectProperty<Chapter> = SimpleObjectProperty()
    private val containerPane: BorderPane = buildContainerPane()
    private val loadingPane: StackPane = buildLoadingPane()

    init {
        styleClass.add(JMetroStyleClass.BACKGROUND)
        styleClass.add("chapter-load-pane")
        setMinSize(650.0, 300.0)
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

    private fun buildContainerPane() = BorderPane().also {
        it.top = ChapterShowToolBar(preferences, configuration, chapterProperty, this)
    }

    private fun buildLoadingPane() = StackPane(ImageView("/org/myteer/novel/image/other/loading.gif")).apply {
        styleClass.add("loading-pane")
    }

    private inner class LoadTask(private val bookId: String, private val chapterId: String) : Task<ChapterContainer>() {
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

        private fun onSucceeded(chapterContainer: ChapterContainer) {
            loading.set(false)
            context.stopProgress()
            titleProperty.set("${chapterContainer.chapter.bookName} - ${chapterContainer.chapter.name}")
            chapterProperty.set(chapterContainer.chapter)
            containerPane.center = buildChapterShowPane(chapterContainer.content)
        }

        override fun call(): ChapterContainer {
            val chapterRepository = ChapterRepository(database)
            val chapterContentRepository = ChapterContentRepository(database)
            val chapter = chapterRepository.selectById(chapterId)!!
            val content: String?
            if (true != chapter.contentCached) {
                ChapterQueryTask(bookId, chapterId).apply { run() }.get()!!.let {
                    // save chapter-content
                    chapterContentRepository.save(it.toLocalChapterContent())
                    // save chapter
                    chapter.previousId = it.previousId
                    chapter.nextId = it.nextId
                    chapter.contentCached = true
                    chapterRepository.save(chapter)
                    // init content
                    content = it.content
                }
            } else {
                content = chapterContentRepository.selectById(chapterId)?.content
            }
            return ChapterContainer(chapter, content)
        }
    }

    private fun buildChapterShowPane(content: String?) = ChapterShowPane(configuration, content)

    private class ChapterContainer(val chapter: Chapter, val content: String?)

    companion object {
        private val logger = LoggerFactory.getLogger(ChapterLoadPane::class.java)
    }
}
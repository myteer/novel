package org.myteer.novel.gui.crawl.details

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.crawl.task.BookQueryTask
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.I18NButtonType
import org.myteer.novel.gui.utils.runOutsideUIAsync
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class CrawlBookQueryPane(private val context: Context, bookId: String) : StackPane() {
    private val loading: BooleanProperty = SimpleBooleanProperty(false)
    private val containerPane: StackPane = StackPane()
    private val loadingPane: StackPane = buildLoadingPane()

    init {
        styleClass.add("crawl-book-query-pane")
        setMinSize(300.0, 300.0)
        setPrefSize(600.0, 300.0)
        buildUI()
        loadData(bookId)
    }

    private fun buildUI() {
        children.add(containerPane)
        children.add(loadingPane)
        loadingPane.visibleProperty().bind(loading)
    }

    @Synchronized
    fun loadData(bookId: String) {
        when {
            loading.get().not() -> {
                loading.set(true)
                runOutsideUIAsync(QueryTask(bookId))
            }
        }
    }

    private fun buildLoadingPane() = StackPane(ImageView("/org/myteer/novel/image/other/loading.gif")).apply {
        styleClass.add("crawl-book-details-loading-pane")
    }

    private inner class QueryTask(private val bookId: String) : BookQueryTask(bookId) {
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
            logger.error("Couldn't execute query task.", t)
            context.showErrorDialog(
                i18n("crawl.book.search.failed.title"),
                i18n("crawl.book.search.failed.message"),
                t as Exception?
            ) {
                when (it) {
                    I18NButtonType.RETRY -> loadData(bookId)
                }
            }.apply { getButtonTypes().add(I18NButtonType.RETRY) }
        }

        private fun onSucceeded(book: Book?) {
            loading.set(false)
            context.stopProgress()
            if (null != book) {
                containerPane.children.setAll(buildCrawlBookDetailsPane(book))
            } else {
                context.showErrorDialog(
                    i18n("crawl.book.search.failed.title"),
                    i18n("crawl.book.search.failed.message")
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun buildCrawlBookDetailsPane(book: Book) = ScrollPane().also { s ->
        s.isFitToWidth = true
        s.isFitToHeight = true
        s.content = CrawlBookDetailsPane(this, book).also { c ->
            c.heightProperty().addListener(object : ChangeListener<Double> {
                override fun changed(observable: ObservableValue<out Double>?, oldValue: Double?, newValue: Double?) {
                    newValue?.let { h ->
                        if (h > 0) {
                            observable?.removeListener(this)
                            runOutsideUIAsync {
                                Thread.sleep(50)
                                this@CrawlBookQueryPane.prefHeight = c.height.coerceAtMost(500.0) + s.padding.top + s.padding.bottom
                            }
                        }
                    }
                }
            } as ChangeListener<in Number>)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CrawlBookQueryPane::class.java)
    }
}
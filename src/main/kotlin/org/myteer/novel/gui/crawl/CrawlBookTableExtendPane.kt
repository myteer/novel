package org.myteer.novel.gui.crawl

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Group
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.image.ImageView
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.crawl.task.BookSearchTask
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.crawl.details.CrawlBookDetailsOverlay
import org.myteer.novel.gui.crawl.details.CrawlBookDetailsPane
import org.myteer.novel.gui.utils.*
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class CrawlBookTableExtendPane(
    private val context: Context,
    private val request: BookSearchRequest
) : StackPane() {
    private val loading: BooleanProperty = SimpleBooleanProperty(false)
    private val hasMore: BooleanProperty = SimpleBooleanProperty(true)
    private val loadingPane: Region = buildLoadingPane()
    val table: CrawlBookTable = buildTable()

    init {
        buildUI()
        loadData()
    }

    private fun buildUI() {
        children.add(table)
        loading.addListener { _, _, value ->
            if (value) {
                context.showOverlay(loadingPane, true)
            } else {
                context.hideOverlay(loadingPane)
            }
        }
    }

    private fun buildLoadingPane() = StackPane(ImageView("/org/myteer/novel/image/other/loading.gif")).apply {
        isPickOnBounds = false
    }

    private fun buildTable() = CrawlBookTable().apply {
        buildDefaultColumns()
        setRowContextMenu(ContextMenu(
            MenuItem(
                i18n("crawl.book.details.title"),
                icon("info-outline-icon")
            ).apply {
                setOnAction { showSelectedBookInfo() }
            }
        ))
        setOnItemDoubleClicked(::showBookInfo)
        setOnScrolledToBottom { loadData() }
    }

    fun showSelectedBookInfo() {
        showBookInfo(table.selectionModel.selectedItem)
    }

    private fun showBookInfo(book: Book) {
        context.showOverlay(CrawlBookDetailsOverlay(context, book.id!!))
    }

    @Synchronized
    private fun loadData() {
        when {
            hasMore.get() && loading.get().not() -> {
                loading.set(true)
                runOutsideUIAsync(SearchTask(request))
            }
        }
    }

    fun clearCache() {
        table.clearCache()
    }

    private inner class SearchTask(request: BookSearchRequest) : BookSearchTask(request) {
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
            logger.error("Couldn't execute search task.", t)
            context.showErrorDialog(
                i18n("crawl.book.search.failed.title"),
                i18n("crawl.book.search.failed.message"),
                t as Exception?
            ) {
                when (it) {
                    I18NButtonType.RETRY -> loadData()
                }
            }.apply { getButtonTypes().add(I18NButtonType.RETRY) }
        }

        private fun onSucceeded(books: List<Book>) {
            loading.set(false)
            context.stopProgress()
            if (books.isNotEmpty()) {
                val targetValue = (table.verticalScrollValueProperty?.value ?: 0.0) * table.items.size
                table.items.addAll(books)
                table.verticalScrollValueProperty?.value = targetValue / table.items.size
                request.apply { page += 1 }
            } else {
                hasMore.set(false)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CrawlBookTableExtendPane::class.java)
    }
}
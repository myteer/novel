package org.myteer.novel.gui.crawl

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.crawl.task.BookSearchTask
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.I18NButtonType
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.runOutsideUIAsync
import org.myteer.novel.gui.utils.setOnScrolledToBottom
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class CrawlBookTableExtendPane(
    private val context: Context,
    private val request: BookSearchRequest
) : VBox() {
    private val loading: BooleanProperty = SimpleBooleanProperty(false)
    private val hasMore: BooleanProperty = SimpleBooleanProperty(true)
    private val table: CrawlBookTable = buildTable()
    private val loadingIcon = buildLoadingIcon()

    init {
        buildUI()
        loadData()
    }

    private fun buildUI() {
        children.add(table)
        children.add(loadingIcon)
    }

    private fun buildLoadingIcon() = Label("正在加载...").apply {
        visibleProperty().bind(loading)
        managedProperty().bind(visibleProperty())
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
        setOnScrolledToBottom {
            println("******")
        }
        setVgrow(this, Priority.ALWAYS)
    }

    private fun showSelectedBookInfo() {
        showBookInfo(table.selectionModel.selectedItem)
    }

    private fun showBookInfo(book: Book) {
        context.showOverlay(Label(book.name))
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
                table.items.addAll(books)
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
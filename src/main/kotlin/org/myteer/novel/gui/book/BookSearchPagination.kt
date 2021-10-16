package org.myteer.novel.gui.book

import javafx.scene.control.Pagination
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.crawl.task.BookSearchTask
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.utils.I18NButtonType
import org.myteer.novel.gui.utils.runOutsideUIAsync
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class BookSearchPagination(
    private val context: Context,
    private val request: BookSearchRequest
) : Pagination() {
    private val table: CrawlBookTable = buildTable()

    init {
        styleClass.add("book-search-pagination")
        setPageFactory {
            runOutsideUIAsync(SearchTask(request.apply { page = it + 1 }))
            table
        }
    }

    private fun buildTable() = CrawlBookTable(context)

    private fun refresh() {
        runOutsideUIAsync(SearchTask(request))
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
            table.items.clear()
            context.showIndeterminateProgress()
        }

        private fun onFailed(t: Throwable?) {
            context.stopProgress()
            logger.error("Couldn't execute search task.", t)
            context.showErrorDialog(
                i18n("book.search.failed.title"),
                i18n("book.search.failed.message"),
                t as Exception?
            ) {
                when (it) {
                    I18NButtonType.RETRY -> refresh()
                }
            }.apply { getButtonTypes().add(I18NButtonType.RETRY) }
        }

        private fun onSucceeded(books: List<Book>) {
            context.stopProgress()
            table.items.setAll(books)
            table.refresh()
        }

    }

    companion object {
        private val logger = LoggerFactory.getLogger(BookSearchPagination::class.java)
    }
}
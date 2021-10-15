package org.myteer.novel.gui.book

import javafx.scene.layout.BorderPane
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.gui.api.Context

class BookSearchResultView(
    context: Context,
    request: BookSearchRequest,
    onPreviousPageRequest: () -> Unit
) : BorderPane() {
    private val pagination = BookSearchPagination(context, request)
    private val toolBar = BookSearchToolBar(onPreviousPageRequest)

    init {
        styleClass.add("book-search-result-view")
        buildUI()
    }

    private fun buildUI() {
        top = toolBar
        center = pagination
    }

    fun clearCache() {
        pagination.clearCache()
    }
}
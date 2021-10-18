package org.myteer.novel.gui.crawl

import javafx.scene.layout.BorderPane
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.gui.api.Context

class CrawlBookSearchResultView(
    context: Context,
    request: BookSearchRequest,
    onPreviousPageRequest: () -> Unit
) : BorderPane() {
    private val content = CrawlBookTableExtendPane(context, request)
    val toolBar = CrawlBookSearchToolBar(this, onPreviousPageRequest)
    val table: CrawlBookTable
        get() = content.table

    init {
        styleClass.add("crawl-book-search-result-view")
        buildUI()
    }

    private fun buildUI() {
        top = toolBar
        center = content
    }

    fun clearCache() {
        content.clearCache()
    }

    fun showSelectedBookInfo() {
        content.showSelectedBookInfo()
    }
}
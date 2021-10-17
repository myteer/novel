package org.myteer.novel.gui.crawl

import javafx.scene.Node
import javafx.scene.layout.StackPane
import org.myteer.novel.gui.api.Context

class CrawlBookSearchView(private val context: Context) : StackPane() {
    private val searchForm: CrawlBookSearchForm = buildSearchForm()

    private var content: Node?
        get() = children[0]
        set(value) {
            clearCache()
            children.setAll(value)
        }

    init {
        styleClass.add("crawl-book-search-view")
        buildUI()
    }

    private fun buildUI() {
        content = searchForm
    }

    private fun buildSearchForm() = CrawlBookSearchForm {
        content = CrawlBookSearchResultView(context, it) { home() }
    }

    private fun home() {
        content = searchForm
    }

    fun clearCache() {
        children.getOrNull(0).takeIf { it is CrawlBookSearchResultView }?.let {
            (it as CrawlBookSearchResultView).clearCache()
        }
    }
}
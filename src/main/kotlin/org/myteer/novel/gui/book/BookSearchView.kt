package org.myteer.novel.gui.book

import javafx.scene.Node
import javafx.scene.layout.StackPane
import org.myteer.novel.gui.api.Context

class BookSearchView(private val context: Context) : StackPane() {
    private val searchForm: BookSearchForm = buildSearchForm()

    private var content: Node?
        get() = children[0]
        set(value) {
            clearCache()
            children.setAll(value)
        }

    init {
        styleClass.add("book-search-view")
        buildUI()
    }

    private fun buildUI() {
        content = searchForm
    }

    private fun buildSearchForm() = BookSearchForm(context) {
        content = BookSearchResultView(context, it) { home() }
    }

    private fun home() {
        content = searchForm
    }

    fun clearCache() {
        children.getOrNull(0).takeIf { it is BookSearchResultView }?.let {
            (it as BookSearchResultView).clearCache()
        }
    }
}
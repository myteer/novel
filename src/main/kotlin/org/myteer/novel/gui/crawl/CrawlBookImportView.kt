package org.myteer.novel.gui.crawl

import javafx.beans.binding.Bindings
import javafx.scene.control.*
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.bookmanager.BookManagerModule
import org.myteer.novel.gui.main.MainView
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class CrawlBookImportView(private val context: Context) : CrawlBookSearchView(context) {
    override fun buildSearchResultView(request: BookSearchRequest): CrawlBookSearchResultView {
        return CrawlBookSearchResultView(context, request) { home() }.apply {
            expandToolBar()
            expandTableContextMenu()
        }
    }

    private fun CrawlBookSearchResultView.expandToolBar() {
        toolBar.rightItems.add(0, Separator())
        toolBar.rightItems.add(0, buildImportButton())
    }

    private fun CrawlBookSearchResultView.expandTableContextMenu() {
        table.getRowContextMenu().items.add(
            MenuItem(
                i18n("crawl.book.import.title"),
                icon("plus-icon")
            ).apply {
                disableProperty().bind(Bindings.isEmpty(table.selectionModel.selectedItems))
                setOnAction { sendBookImportRequest(table.selectionModel.selectedItem) }
            }
        )
    }

    private fun CrawlBookSearchResultView.buildImportButton() = Button().apply {
        disableProperty().bind(Bindings.isEmpty(table.selectionModel.selectedItems))
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("plus-icon")
        tooltip = Tooltip(i18n("crawl.book.import.title"))
        setOnAction { sendBookImportRequest(table.selectionModel.selectedItem) }
    }

    private fun sendBookImportRequest(book: Book) {
        context.sendRequest(
            MainView.ModuleOpenRequest(
                BookManagerModule::class.java,
                BookManagerModule.BookImportRequest(book.id!!)
            )
        )
    }
}
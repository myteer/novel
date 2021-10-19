package org.myteer.novel.gui.crawl

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.control.*
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class CrawlBookSearchToolBar(
    private val view: CrawlBookSearchResultView,
    private val onPreviousPageRequest: () -> Unit
) : BiToolBar() {
    init {
        buildUI()
    }

    private fun buildUI() {
        leftItems.add(buildHomeButton())
        leftItems.add(Separator())
        leftItems.add(buildTotalItemsLabel())
        rightItems.add(buildBookInfoButton())
    }

    private fun buildHomeButton() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("arrow-back-icon")
        tooltip = Tooltip(i18n("crawl.book.search.back"))
        setOnAction { onPreviousPageRequest() }
    }

    private fun buildTotalItemsLabel() = Label().apply {
        padding = Insets(0.0, 0.0, 0.0, 5.0)
        textProperty().bind(
            SimpleStringProperty(i18n("crawl.book.search.total_items"))
                .concat(" ")
                .concat(Bindings.size(view.table.items))
        )
    }

    private fun buildBookInfoButton() = Button().apply {
        disableProperty().bind(Bindings.isEmpty(view.table.selectionModel.selectedItems))
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("info-outline-icon")
        tooltip = Tooltip(i18n("crawl.book.details.title"))
        setOnAction { view.showSelectedBookInfo() }
    }
}
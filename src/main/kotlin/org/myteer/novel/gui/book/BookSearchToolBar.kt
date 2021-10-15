package org.myteer.novel.gui.book

import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Separator
import javafx.scene.control.Tooltip
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class BookSearchToolBar(
    private val onPreviousPageRequest: () -> Unit
) : BiToolBar() {
    init {
        buildUI()
    }

    private fun buildUI() {
        leftItems.add(buildHomeButton())
        leftItems.add(Separator())
    }

    private fun buildHomeButton() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("arrow-left-icon")
        tooltip = Tooltip(i18n("book.search.back"))
        setOnAction { onPreviousPageRequest() }
    }
}
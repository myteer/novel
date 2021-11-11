package org.myteer.novel.gui.export.control

import javafx.scene.control.ToggleButton
import javafx.scene.control.Tooltip
import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.onValuePresent
import org.myteer.novel.i18n.i18n

class ReverseItemsToggle(exportConfiguration: BookExportConfiguration) : ToggleButton() {
    init {
        graphic = icon("rotate-icon")
        tooltip = Tooltip(i18n("book.export.reverse_order"))
        isSelected = exportConfiguration.reverseItems
        selectedProperty().onValuePresent {
            exportConfiguration.reverseItems = it
        }
    }
}
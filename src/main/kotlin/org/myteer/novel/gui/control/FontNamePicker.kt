package org.myteer.novel.gui.control

import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.text.Font

class FontNamePicker : ComboBox<String>() {
    init {
        items.addAll(Font.getFontNames().distinct())
        buttonCell = FontCell()
        setCellFactory { FontCell() }
    }

    private class FontCell : ListCell<String>() {
        override fun updateItem(item: String?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty) {
                text = null
                graphic = null
            } else {
                text = null
                graphic = Label(item).apply {
                    style = "-fx-font-family: $item !important"
                }
            }
        }
    }
}
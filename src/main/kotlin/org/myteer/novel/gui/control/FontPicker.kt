package org.myteer.novel.gui.control

import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.util.*

class FontPicker : ComboBox<Font>() {
    init {
        items.addAll(AVAILABLE_FONTS)
        buttonCell = FontCell()
        setCellFactory { FontCell() }
    }

    private class FontCell : ListCell<Font>() {
        override fun updateItem(item: Font?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty) {
                text = null
                graphic = null
            } else {
                text = null
                graphic = Label(item?.family).apply {
                    style = "-fx-font-family: '${item?.getFamily(Locale.ENGLISH)}' !important;"
                }
            }
        }
    }

    private companion object {
        val AVAILABLE_FONTS = GraphicsEnvironment.getLocalGraphicsEnvironment().allFonts.distinctBy(Font::getFamily)
    }
}
package org.myteer.novel.gui.control

import javafx.scene.Cursor
import javafx.scene.control.TextField

class HighlightableLabel(text: String? = null) : TextField(text) {
    init {
        cursor = Cursor.TEXT
        isEditable = false
        prefColumnCount = 15
        styleClass.setAll("highlightable-label", "label")
        style = "-fx-background-color: transparent; -fx-padding: 0;"
        setOnContextMenuRequested { it.consume() }
    }
}
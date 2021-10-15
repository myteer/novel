package org.myteer.novel.gui.control

import javafx.geometry.Insets
import javafx.scene.layout.StackPane
import org.controlsfx.control.textfield.CustomTextField
import org.myteer.novel.gui.utils.icon

class SearchTextField : CustomTextField() {
    init {
        styleClass.add("search-text-field")
        left = StackPane(icon("search-icon")).apply {
            padding = Insets(5.0)
        }
    }
}
package org.myteer.novel.gui.control

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Tooltip
import org.myteer.novel.gui.keybinding.KeyBinding

class KeyBindingTooltip(text: String, keyBinding: KeyBinding) : Tooltip() {
    init {
        textProperty().bind(
            SimpleStringProperty("$text (").concat(keyBinding.keyCombinationProperty).concat(")")
        )
    }
}
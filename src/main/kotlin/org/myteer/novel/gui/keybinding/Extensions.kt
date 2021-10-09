package org.myteer.novel.gui.keybinding

import javafx.scene.control.MenuItem

fun <M : MenuItem> M.keyBinding(keyBinding: KeyBinding?) = apply {
    keyBinding?.let {
        acceleratorProperty().bind(it.keyCombinationProperty)
    }
}
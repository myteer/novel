package org.myteer.novel.gui.utils

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import org.myteer.novel.gui.keybinding.KeyBinding

fun <M : MenuItem> M.action(onAction: EventHandler<ActionEvent>): M = also { it.onAction = onAction }

fun <M : MenuItem> M.graphic(iconStyleClass: String): M = apply { graphic = icon(iconStyleClass) }

fun <M : MenuItem> M.keyBinding(keyBinding: KeyBinding?): M = apply {
    keyBinding?.let {
        acceleratorProperty().bind(it.keyCombinationProperty)
    }
}

fun <M : Menu> M.menuItem(item: MenuItem): M = apply { items.add(item) }

fun <M : Menu> M.separator(): M = apply { items.add(SeparatorMenuItem()) }
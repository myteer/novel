package org.myteer.novel.gui.utils

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.MenuItem

fun <M : MenuItem> M.action(onAction: EventHandler<ActionEvent>): M = also { this.onAction = onAction }
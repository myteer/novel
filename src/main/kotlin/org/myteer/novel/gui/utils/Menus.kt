/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
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
package org.myteer.novel.gui.dbmanager

import animatefx.animation.RotateIn
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class DatabaseManagerToolBar(private val view: DatabaseManagerView) : BiToolBar() {
    init {
        leftToolBar.padding = Insets(0.0, 0.0, 0.0, 10.0)
        buildUI()
    }

    private fun buildUI() {
        leftItems.add(buildIcon())
        leftItems.add(buildLabel())
        rightItems.add(buildSelectedItemsIndicator())
        rightItems.add(buildRefreshButton())
    }

    private fun buildIcon() = icon("database-manager-icon")

    private fun buildLabel() = Label(i18n("database.manager.title"))

    private fun buildSelectedItemsIndicator() = Label().apply {
        textProperty().bind(
            view.itemsCount().asString()
                .concat("/")
                .concat(view.selectedItemsCount())
                .concat(" ")
                .concat(i18n("database.manager.selected"))
        )
    }

    private fun buildRefreshButton() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("reload-icon")
        setOnAction {
            view.refresh()
            RotateIn(graphic).play()
        }
    }
}
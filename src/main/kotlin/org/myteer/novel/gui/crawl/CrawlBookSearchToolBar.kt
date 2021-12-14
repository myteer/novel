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
package org.myteer.novel.gui.crawl

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.control.*
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class CrawlBookSearchToolBar(
    private val view: CrawlBookSearchResultView,
    private val onPreviousPageRequest: () -> Unit
) : BiToolBar() {
    init {
        buildUI()
    }

    private fun buildUI() {
        leftItems.add(buildHomeButton())
        leftItems.add(Separator())
        leftItems.add(buildTotalItemsLabel())
        rightItems.add(buildBookInfoButton())
    }

    private fun buildHomeButton() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("direction-left-icon")
        tooltip = Tooltip(i18n("crawl.book.search.back"))
        setOnAction { onPreviousPageRequest() }
    }

    private fun buildTotalItemsLabel() = Label().apply {
        padding = Insets(0.0, 0.0, 0.0, 5.0)
        textProperty().bind(
            SimpleStringProperty(i18n("crawl.book.search.total_items"))
                .concat(" ")
                .concat(Bindings.size(view.table.items))
        )
    }

    private fun buildBookInfoButton() = Button().apply {
        disableProperty().bind(Bindings.isEmpty(view.table.selectionModel.selectedItems))
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("info-outline-icon")
        tooltip = Tooltip(i18n("crawl.book.details.title"))
        setOnAction { view.showSelectedBookInfo() }
    }
}
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
package org.myteer.novel.gui.volume.chapter

import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.scene.control.ScrollPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import org.myteer.novel.db.data.Chapter

class ChapterShowPane(
    private val configuration: ChapterShowConfiguration,
    private val chapterContent: String?
) : ScrollPane() {
    init {
        styleClass.add("chapter-show-pane")
        padding = Insets(10.0)
        buildUI()
    }

    private fun buildUI() {
        content = buildContentText()
    }

    private fun buildContentText() = Text(chapterContent).also {
        it.fontProperty().bind(Bindings.createObjectBinding({
            Font.font(configuration.fontFamilyProperty.value, configuration.fontSizeProperty.doubleValue())
        }, configuration.fontFamilyProperty, configuration.fontSizeProperty))
        it.fillProperty().bind(configuration.fontColorProperty)
        it.wrappingWidthProperty().bind(widthProperty().subtract(40))
    }
}
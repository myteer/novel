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
package org.myteer.novel.gui.control

import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.ToolBar
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority

open class BiToolBar : HBox() {
    val leftToolBar = buildLeftToolBar()
    private val rightToolBar = buildRightToolBar()

    val leftItems: ObservableList<Node>
        get() = leftToolBar.items

    val rightItems: ObservableList<Node>
        get() = rightToolBar.items

    init {
        children.addAll(
            leftToolBar,
            SeparatorPane(),
            rightToolBar
        )
    }

    private fun buildLeftToolBar() = ToolBar().apply {
        setHgrow(this, Priority.SOMETIMES)
        prefHeightProperty().bind(this@BiToolBar.heightProperty())
    }

    private fun buildRightToolBar() = ToolBar().apply {
        setHgrow(this, Priority.SOMETIMES)
        prefHeightProperty().bind(this@BiToolBar.heightProperty())
    }

    private class SeparatorPane : Pane() {
        init {
            styleClass.add("tool-bar")
            setHgrow(this, Priority.ALWAYS)
        }
    }
}
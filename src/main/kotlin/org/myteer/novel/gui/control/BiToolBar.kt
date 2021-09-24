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
package org.myteer.novel.gui.control

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.TitledPane
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.CodeArea

class ExceptionDisplayPane(exception: Exception) : TitledPane() {
    init {
        content = buildCodeArea(exception)
        isAnimated = true
        isExpanded = false
    }

    private fun buildCodeArea(exception: Exception): Node {
        return VirtualizedScrollPane(CodeArea(exception.stackTraceToString()).apply {
            padding = Insets(5.0)
            prefHeight = 200.0
            isEditable = false
        })
    }
}
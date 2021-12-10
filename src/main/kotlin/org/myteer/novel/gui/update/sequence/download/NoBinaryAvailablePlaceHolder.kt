package org.myteer.novel.gui.update.sequence.download

import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import org.myteer.novel.i18n.i18n

class NoBinaryAvailablePlaceHolder : StackPane() {
    init {
        styleClass.add("no-binary-available-place-holder")
        buildUI()
    }

    private fun buildUI() {
        children.add(buildCenterLabel())
    }

    private fun buildCenterLabel() = Label().apply {
        text = i18n("update.dialog.download.no_binary_available")
    }
}
package org.myteer.novel.gui.update.sequence.details

import javafx.geometry.Insets
import javafx.scene.layout.VBox
import org.myteer.novel.gui.utils.scrollPane
import org.myteer.novel.update.Release

class DetailsSegmentView(private val release: Release) : VBox() {
    init {
        children.add(buildPreviewScrollPane())
    }

    private fun buildPreviewScrollPane() =
        scrollPane(PreviewMarkdownView(release.description), fitToWidth = true, fitToHeight = true).apply {
            setMargin(this, Insets(0.0, 0.0, 10.0, 0.0))
            prefWidth = 200.0
            prefHeight = 200.0
        }
}
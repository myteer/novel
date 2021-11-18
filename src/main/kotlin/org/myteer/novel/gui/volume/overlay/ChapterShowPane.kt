package org.myteer.novel.gui.volume.overlay

import javafx.scene.layout.BorderPane
import javafx.scene.text.Text
import org.myteer.novel.db.data.Chapter
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.utils.scrollPane

class ChapterShowPane(private val chapter: Chapter, view: ChapterLoadPane) : BorderPane() {
    private val toolBar = BiToolBar()
    private val contentText = buildContentText()
    init {
        styleClass.add("chapter-show-pane")
        buildUI()
    }

    private fun buildUI() {
        top = toolBar
        center = scrollPane(contentText)
    }

    private fun buildContentText() = Text(chapter.content)
}
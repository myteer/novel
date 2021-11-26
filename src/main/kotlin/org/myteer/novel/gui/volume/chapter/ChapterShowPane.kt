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
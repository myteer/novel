package org.myteer.novel.gui.volume.chapter

import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.gui.window.BaseWindow

class ChapterWindow(view: ChapterView, owner: Window?) : BaseWindow<ChapterView>(view.titleProperty, view) {
    init {
        initModality(Modality.NONE)
        owner?.let { initOwner(it) }
        width = 700.0
        height = 600.0
        centerOnScreen()
    }
}
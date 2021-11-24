package org.myteer.novel.gui.volume.overlay

import javafx.beans.property.StringProperty
import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.gui.window.BaseWindow

class ChapterWindow(titleProperty: StringProperty, view: ChapterLoadPane, owner: Window?) :
    BaseWindow<ChapterLoadPane>(titleProperty, view) {
    init {
        initModality(Modality.NONE)
        owner?.let { initOwner(it) }
        width = 700.0
        height = 600.0
        centerOnScreen()
    }
}
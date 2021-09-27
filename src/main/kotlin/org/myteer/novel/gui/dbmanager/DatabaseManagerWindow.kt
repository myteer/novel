package org.myteer.novel.gui.dbmanager

import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class DatabaseManagerWindow(view: DatabaseManagerView, owner: Window?) :
    BaseWindow<DatabaseManagerView>(i18n("window.dbmanager.title"), view) {
    init {
        initModality(Modality.APPLICATION_MODAL)
        owner?.let { initOwner(it) }
        width = 1000.0
        height = 430.0
        centerOnScreen()
    }
}
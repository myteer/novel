package org.myteer.novel.gui.dbcreator

import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class DatabaseCreatorWindow(view: DatabaseCreatorView, owner: Window?) :
    BaseWindow<DatabaseCreatorView>(i18n("window.dbcreator.title"), view) {
    init {
        initModality(Modality.APPLICATION_MODAL)
        owner?.also { initOwner(it) }
        width = 741.0
        height = 435.0
        centerOnScreen()
    }
}
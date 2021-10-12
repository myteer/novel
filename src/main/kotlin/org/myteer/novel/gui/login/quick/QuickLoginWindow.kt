package org.myteer.novel.gui.login.quick

import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class QuickLoginWindow(
    view: QuickLoginView,
    databaseMeta: DatabaseMeta,
    owner: Window? = null
) : BaseWindow<QuickLoginView>(i18n("window.login.quick.title", databaseMeta), view) {
    init {
        initModality(Modality.APPLICATION_MODAL)
        owner?.let { initOwner(it) }
        width = 460.0
        height = 230.0
        centerOnScreen()
    }
}
package org.myteer.novel.gui.preferences

import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class PreferencesWindow(view: PreferencesView, owner: Window?) :
    BaseWindow<PreferencesView>(i18n("window.preferences.title"), view) {
    init {
        initModality(Modality.APPLICATION_MODAL)
        owner?.let { initOwner(it) }
        width = 800.0
        height = 500.0
        centerOnScreen()
    }
}
package org.myteer.novel.gui.preferences

import javafx.stage.Window
import org.myteer.novel.config.Preferences

class PreferencesActivity(private val preferences: Preferences) {
    fun show(owner: Window?) {
        val view = PreferencesView(preferences)
        val window = PreferencesWindow(view, owner)
        window.show()
    }
}
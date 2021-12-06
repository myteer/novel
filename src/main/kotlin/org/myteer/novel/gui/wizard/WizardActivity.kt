package org.myteer.novel.gui.wizard

import org.myteer.novel.config.Preferences

class WizardActivity(private val preferences: Preferences) {
    fun show() {
        val window = WizardDialogWindow(WizardDialog(preferences))
        window.showAndWait()
    }

    companion object {
        fun isNeeded(preferences: Preferences): Boolean = preferences.source.isCreated()
    }
}
package org.myteer.novel.gui.wizard

import javafx.stage.Modality
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class WizardDialogWindow(wizardDialog: WizardDialog) :
    BaseWindow<WizardDialog>(i18n("window.wizard.title"), wizardDialog) {
    init {
        initModality(Modality.APPLICATION_MODAL)
        isAlwaysOnTop = true
        isResizable = true
    }
}
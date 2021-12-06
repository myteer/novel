package org.myteer.novel.gui.wizard

import com.dansoftware.sgmdialog.SegmentDialog
import javafx.scene.control.Button
import jfxtras.styles.jmetro.JMetroStyleClass
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.api.EmptyContext
import org.myteer.novel.i18n.I18N
import org.myteer.novel.i18n.i18n

class WizardDialog(
    preferences: Preferences
) : SegmentDialog(I18N.getValues(), DialogSegmentSequence(preferences)), EmptyContext {
    init {
        styleClass.add(JMetroStyleClass.BACKGROUND)
        styleClass.add("wizard-dialog")
        buildUI()
    }

    private fun buildUI() {
        customButtons = listOf(SkipButton())
    }

    private inner class SkipButton : Button() {
        init {
            text = i18n("segment.dialog.button.skip")
            setOnAction {
                this@WizardDialog.segmentSequence.skipAll()
            }
        }
    }
}
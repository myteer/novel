/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.myteer.novel.gui.wizard

import com.dansoftware.sgmdialog.SegmentDialog
import com.dansoftware.sgmdialog.SegmentSequence
import javafx.scene.control.Button
import javafx.stage.Stage
import jfxtras.styles.jmetro.JMetroStyleClass
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.api.EmptyContext
import org.myteer.novel.gui.utils.stage
import org.myteer.novel.gui.wizard.segment.lang.LanguageSegment
import org.myteer.novel.gui.wizard.segment.theme.ThemeSegment
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

    private class DialogSegmentSequence(preferences: Preferences) : SegmentSequence(
        ThemeSegment(preferences), LanguageSegment(preferences)
    ) {
        override fun onSegmentsFinished(segmentDialog: SegmentDialog) {
            segmentDialog.stage?.let(Stage::close)
        }
    }
}
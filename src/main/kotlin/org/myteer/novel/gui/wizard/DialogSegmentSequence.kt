package org.myteer.novel.gui.wizard

import com.dansoftware.sgmdialog.SegmentDialog
import com.dansoftware.sgmdialog.SegmentSequence
import javafx.stage.Stage
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.utils.stage
import org.myteer.novel.gui.wizard.segment.lang.LanguageSegment
import org.myteer.novel.gui.wizard.segment.theme.ThemeSegment

class DialogSegmentSequence(preferences: Preferences) : SegmentSequence(
    ThemeSegment(preferences), LanguageSegment(preferences)
) {
    override fun onSegmentsFinished(segmentDialog: SegmentDialog) {
        segmentDialog.stage?.let(Stage::close)
    }
}
package org.myteer.novel.gui.wizard.segment.lang

import com.dansoftware.sgmdialog.FixedContentTitledSegment
import javafx.scene.Node
import org.myteer.novel.config.Preferences
import org.myteer.novel.i18n.i18n

class LanguageSegment(
    private val preferences: Preferences
) : FixedContentTitledSegment(i18n("segment.lang.name"), i18n("segment.lang.title")) {
    override fun createCenterContent(): Node = LanguageSegmentView(preferences)
}
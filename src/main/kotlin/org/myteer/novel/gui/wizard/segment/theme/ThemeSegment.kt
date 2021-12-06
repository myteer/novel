package org.myteer.novel.gui.wizard.segment.theme

import com.dansoftware.sgmdialog.FixedContentTitledSegment
import javafx.scene.Node
import org.myteer.novel.config.Preferences
import org.myteer.novel.i18n.i18n

class ThemeSegment(
    private val preferences: Preferences
) : FixedContentTitledSegment(i18n("segment.theme.name"), i18n("segment.theme.title")) {
    override fun createCenterContent(): Node = ThemeSegmentView(preferences)
}
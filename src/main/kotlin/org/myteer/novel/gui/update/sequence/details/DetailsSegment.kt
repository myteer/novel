package org.myteer.novel.gui.update.sequence.details

import com.dansoftware.sgmdialog.FixedContentSegment
import javafx.scene.Node
import org.myteer.novel.i18n.i18n
import org.myteer.novel.update.Release

class DetailsSegment(private val release: Release) : FixedContentSegment(i18n("update.segment.details.name")) {
    override fun createContent(): Node = DetailsSegmentView(release)
}
package org.myteer.novel.gui.update.sequence.notification

import com.dansoftware.sgmdialog.FixedContentSegment
import javafx.scene.Node
import org.myteer.novel.i18n.i18n
import org.myteer.novel.update.Release

class NotificationSegment(private val release: Release) : FixedContentSegment(i18n("update.segment.notification.name")) {
    override fun createContent(): Node = NotificationSegmentView(release)
}
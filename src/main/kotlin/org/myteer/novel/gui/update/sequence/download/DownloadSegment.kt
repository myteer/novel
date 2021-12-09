package org.myteer.novel.gui.update.sequence.download

import com.dansoftware.sgmdialog.FixedContentTitledSegment
import javafx.scene.Node
import org.myteer.novel.gui.api.Context
import org.myteer.novel.i18n.i18n
import org.myteer.novel.update.Release

class DownloadSegment(
    private val context: Context,
    private val release: Release
) : FixedContentTitledSegment(i18n("update.segment.download.name"), i18n("update.segment.download.title")) {
    override fun createCenterContent(): Node {
        TODO("Not yet implemented")
    }
}
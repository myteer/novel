package org.myteer.novel.gui.update

import javafx.scene.Group
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import org.myteer.novel.gui.api.Context
import org.myteer.novel.update.Release

class UpdateActivity(private val context: Context, private val release: Release) {
    fun show() {
        var overlay: Region? = null
        overlay = UpdateDialog(context, release) {
            context.hideOverlay(overlay!!)
        }.let { StackPane(Group(it)) }
        context.showOverlay(overlay, true)
    }
}
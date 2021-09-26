package org.myteer.novel.gui.info

import org.myteer.novel.gui.api.Context

class InformationActivity(private val context: Context) {
    fun show() {
        context.showOverlay(InformationViewOverlay(context), false)
    }
}
package org.myteer.novel.gui.export

import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.gui.api.Context

interface ConfigurationDialog<C : BookExportConfiguration> {
    fun show(context: Context, onFinished: (C) -> Unit)
}
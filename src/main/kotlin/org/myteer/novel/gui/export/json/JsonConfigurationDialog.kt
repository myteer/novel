package org.myteer.novel.gui.export.json

import org.myteer.novel.export.json.JsonExportConfiguration
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.export.ConfigurationDialog

class JsonConfigurationDialog : ConfigurationDialog<JsonExportConfiguration> {
    override fun show(context: Context, onFinished: (JsonExportConfiguration) -> Unit) {
        var overlay: JsonConfigurationOverlay? = null
        overlay = JsonConfigurationOverlay {
            context.hideOverlay(overlay!!)
            onFinished(it)
        }
        context.showOverlay(overlay)
    }
}
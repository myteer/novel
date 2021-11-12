package org.myteer.novel.gui.export.excel

import org.myteer.novel.export.excel.ExcelExportConfiguration
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.export.ConfigurationDialog

class ExcelConfigurationDialog : ConfigurationDialog<ExcelExportConfiguration> {
    override fun show(context: Context, onFinished: (ExcelExportConfiguration) -> Unit) {
        var overlay: ExcelConfigurationOverlay? = null
        overlay = ExcelConfigurationOverlay {
            context.hideOverlay(overlay!!)
            onFinished(it)
        }
        context.showOverlay(overlay)
    }
}
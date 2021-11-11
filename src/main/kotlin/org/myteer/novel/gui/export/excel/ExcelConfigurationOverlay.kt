package org.myteer.novel.gui.export.excel

import org.myteer.novel.export.excel.ExcelExportConfiguration
import org.myteer.novel.gui.base.TitledOverlayBox
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class ExcelConfigurationOverlay(onFinished: (ExcelExportConfiguration) -> Unit) : TitledOverlayBox(
    i18n("book.export.excel.title"),
    icon("excel-icon"),
    ExcelConfigurationView(onFinished)
)
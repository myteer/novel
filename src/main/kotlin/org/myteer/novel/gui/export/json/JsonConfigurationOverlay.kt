package org.myteer.novel.gui.export.json

import org.myteer.novel.export.json.JsonExportConfiguration
import org.myteer.novel.gui.base.TitledOverlayBox
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class JsonConfigurationOverlay(onFinished: (JsonExportConfiguration) -> Unit) : TitledOverlayBox(
    i18n("book.export.json.title"),
    icon("json-icon"),
    JsonConfigurationView(onFinished)
)
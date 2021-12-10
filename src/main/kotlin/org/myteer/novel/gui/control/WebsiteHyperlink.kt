package org.myteer.novel.gui.control

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.Hyperlink
import javafx.scene.control.Tooltip
import org.myteer.novel.utils.SystemBrowser

class WebsiteHyperlink(text: String, url: String? = null) : Hyperlink(text) {
    private val urlProperty: StringProperty = SimpleStringProperty(url)

    init {
        tooltip = WebsiteHyperlinkTooltip(this)
        setOnAction {
            urlProperty.get()?.let(SystemBrowser::browse)
        }
    }

    fun urlProperty(): StringProperty = urlProperty

    private class WebsiteHyperlinkTooltip(parent: WebsiteHyperlink) : Tooltip() {
        init {
            textProperty().bind(parent.urlProperty)
        }
    }
}
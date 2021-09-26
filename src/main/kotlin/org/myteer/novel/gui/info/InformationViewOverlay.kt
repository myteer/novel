package org.myteer.novel.gui.info

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Tooltip
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.base.TitledOverlayBox
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class InformationViewOverlay(context: Context) : TitledOverlayBox(
    i18n("info.view.title"),
    icon("info-icon"),
    InformationView(context),
    true,
    false,
    Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("copy-icon")
        tooltip = Tooltip(i18n("info.copy"))
        padding = Insets(0.0)
        setOnAction {
            ClipboardContent().let {
                it.putString(getApplicationInfo())
                Clipboard.getSystemClipboard().setContent(it)
            }
        }
    }
)
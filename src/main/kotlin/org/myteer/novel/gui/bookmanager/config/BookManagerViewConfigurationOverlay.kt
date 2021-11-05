package org.myteer.novel.gui.bookmanager.config

import org.myteer.novel.gui.base.TitledOverlayBox
import org.myteer.novel.gui.bookmanager.BookManagerView
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class BookManagerViewConfigurationOverlay(bookManagerView: BookManagerView) : TitledOverlayBox(
    i18n("record.panel_config"), icon("tune-icon"), BookManagerViewConfigurationPanel(bookManagerView)
)
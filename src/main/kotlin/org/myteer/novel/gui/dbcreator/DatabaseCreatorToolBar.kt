package org.myteer.novel.gui.dbcreator

import javafx.scene.control.Label
import javafx.scene.control.ToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class DatabaseCreatorToolBar : ToolBar() {
    init {
        styleClass.add("header-toolbar")
        buildUI()
    }

    private fun buildUI() {
        items.addAll(
            icon("database-plus-icon"),
            Label(i18n("database.creator.title"))
        )
    }
}
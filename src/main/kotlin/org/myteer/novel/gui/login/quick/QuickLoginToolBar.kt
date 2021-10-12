package org.myteer.novel.gui.login.quick

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class QuickLoginToolBar(private val databaseMeta: DatabaseMeta) : HBox(10.0) {
    init {
        styleClass.add("header-toolbar")
        buildUI()
    }

    private fun buildUI() {
        children.add(buildLogo())
        children.add(buildLabel())
    }

    private fun buildLogo() = icon("login-icon")

    private fun buildLabel() = Label().apply {
        text = i18n("login.quick.title") + " - " + databaseMeta
        setHgrow(this, Priority.ALWAYS)
    }
}
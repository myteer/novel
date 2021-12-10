package org.myteer.novel.gui.login

import javafx.geometry.Insets
import javafx.scene.control.*
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.action.GlobalActions
import org.myteer.novel.gui.action.MenuItems
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.info.InformationActivity
import org.myteer.novel.gui.preferences.PreferencesActivity
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class LoginToolBar(
    private val context: Context,
    private val databaseTracker: DatabaseTracker,
    private val preferences: Preferences
) : BiToolBar() {
    init {
        leftToolBar.padding = Insets(0.0, 10.0, 0.0, 10.0)
        buildUI()
    }

    private fun buildUI() {
        leftItems.add(buildLogo())
        leftItems.add(buildLabel())
        rightItems.add(buildQuickOptionsControl())
        rightItems.add(buildInfoItem())
    }

    private fun buildLogo() = icon("login-icon")

    private fun buildLabel() = Label(i18n("database.auth"))

    private fun buildQuickOptionsControl() = MenuButton().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("settings-icon")
        items.add(buildUpdateSearchMenuItem())
        items.add(buildSettingsMenuItem())
    }

    private fun buildUpdateSearchMenuItem() = MenuItems.of(GlobalActions.SEARCH_FOR_UPDATE, context, preferences, databaseTracker)

    private fun buildSettingsMenuItem() = MenuItem().apply {
        text = i18n("action.settings")
        graphic = icon("settings-icon")
        setOnAction { PreferencesActivity(preferences).show(context.getContextWindow()) }
    }

    private fun buildInfoItem() = Button().apply {
        graphic = icon("info-icon")
        setOnAction { InformationActivity(context).show() }
    }
}
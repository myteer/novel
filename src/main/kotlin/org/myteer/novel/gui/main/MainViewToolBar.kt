package org.myteer.novel.gui.main

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.Group
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import org.apache.commons.io.FileUtils
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.control.BiToolBar
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.revealInExplorer
import org.myteer.novel.i18n.i18n

class MainViewToolBar(
    private val view: MainView,
    private val preferences: Preferences,
    private val databaseTracker: DatabaseTracker
) : BiToolBar() {
    init {
        styleClass.add("main-view-toolbar")
        buildUI()
    }

    private fun buildUI() {
        leftItems.addAll(
            buildHomeButton(),
            buildSeparator(),
            buildDatabaseNameControl()
        )
        rightItems.addAll(
            buildSizeIndicator(),
            buildSeparator(),
            buildFileOpenButton(),
            buildSeparator(),
            buildCloseButton()
        )
    }

    private fun buildHomeButton() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("home-icon")
        setOnAction {
            view.openModuleTab()
        }
    }

    private fun buildSeparator() = Separator(Orientation.VERTICAL)

    private fun buildDatabaseNameControl() = StackPane(
        Group(
            HBox(2.0,
                icon("database-icon"),
                Label(view.databaseMeta.toString())
            )
        )
    )

    private fun buildSizeIndicator() = Label().apply {
        padding = Insets(0.0, 5.0, 0.0, 0.0)
        text = "${i18n("main_view.database_size")} ${FileUtils.byteCountToDisplaySize(view.databaseMeta.file?.length() ?: -1)}"
    }

    private fun buildFileOpenButton() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("folder-open-icon")
        tooltip = Tooltip(i18n("menubar.menu.file.reveal"))
        setOnAction {
            view.databaseMeta.file?.revealInExplorer()
        }
    }

    private fun buildCloseButton() = Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("logout-icon")
        tooltip = Tooltip(i18n("menubar.menu.file.dbclose"))
        setOnAction {
            preferences.editor()
                .put(PreferenceKey.LOGIN_DATA, preferences.get(PreferenceKey.LOGIN_DATA).apply {
                    setAutoLogin(false)
                    setAutoLoginCredentials(null)
                })
                .tryCommit()
            view.close()
        }
    }
}
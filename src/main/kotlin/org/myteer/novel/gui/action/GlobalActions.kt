/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.myteer.novel.gui.action

import javafx.concurrent.Task
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.input.KeyEvent
import javafx.stage.Stage
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.dbcreator.DatabaseCreatorActivity
import org.myteer.novel.gui.dbcreator.DatabaseOpener
import org.myteer.novel.gui.dbmanager.DatabaseManagerActivity
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.info.InformationActivity
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.preferences.PreferencesActivity
import org.myteer.novel.gui.update.UpdateActivity
import org.myteer.novel.gui.utils.runOutsideUIAsync
import org.myteer.novel.gui.utils.typeEquals
import org.myteer.novel.i18n.i18n
import org.myteer.novel.launcher.ActivityLauncher
import org.myteer.novel.launcher.LauncherMode
import org.myteer.novel.main.ApplicationRestart
import org.myteer.novel.update.Release
import org.myteer.novel.update.UpdateSearcher
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

object GlobalActions {
    private val logger = LoggerFactory.getLogger(GlobalActions::class.java)

    @JvmField
    val CREATE_DATABASE = Action(
        "action.create_database",
        "database-plus-icon",
        KeyBindings.createDatabase
    ) { context, preferences, databaseTracker ->
        DatabaseCreatorActivity(databaseTracker).show(context.getContextWindow()).ifPresent {
            submitTask(context, ActivityLauncher(preferences, databaseTracker, LauncherMode.INTERNAL, databaseMeta = it))
        }
    }

    @JvmField
    val OPEN_DATABASE = Action(
        "action.open_database",
        "file-icon",
        KeyBindings.openDatabase
    ) { context, preferences, databaseTracker ->
        DatabaseOpener().showOpenDialog(context.getContextWindow())?.also {
            submitTask(context, ActivityLauncher(preferences, databaseTracker, LauncherMode.INTERNAL, databaseMeta = it))
        }
    }

    @JvmField
    val OPEN_DATABASE_MANAGER = Action(
        "action.open_database_manager",
        "database-manager-icon",
        KeyBindings.openDatabaseManager
    ) { context, _, databaseTracker ->
        DatabaseManagerActivity(databaseTracker).show(context.getContextWindow())
    }

    @JvmField
    val OPEN_SETTINGS = Action(
        "action.settings",
        "settings-icon",
        KeyBindings.openSettings
    ) { context, preferences, _ ->
        PreferencesActivity(preferences).show(context.getContextWindow())
    }

    @JvmField
    val FULL_SCREEN = Action(
        "action.full_screen",
        "full-screen-icon",
        KeyBindings.fullScreen
    ) { context, _, _ ->
        context.getContextWindow()?.also { if (it is Stage) it.isFullScreen = !it.isFullScreen }
    }

    @JvmField
    val NEW_ENTRY = Action(
        "action.new_entry",
        "window-icon",
        KeyBindings.newEntry
    ) { context, preferences, databaseTracker ->
        submitTask(context, ActivityLauncher(preferences, databaseTracker, LauncherMode.INTERNAL))
    }

    @JvmField
    val RESTART_APPLICATION = run {
        val dialogShownContexts: MutableSet<Context> = HashSet()
        Action(
            "action.restart",
            "update-icon",
            KeyBindings.restartApplication
        ) { context, _, _ ->
            if (!dialogShownContexts.contains(context)) {
                context.showConfirmationDialog(
                    i18n("app.restart.dialog.title"),
                    i18n("app.restart.dialog.message")
                ) {
                    if (it.typeEquals(ButtonType.YES)) {
                        ApplicationRestart.restart()
                    }
                    dialogShownContexts.remove(context)
                }
                dialogShownContexts.add(context)
            }
        }
    }

    @JvmField
    val OPEN_APP_INFO = Action(
        "action.open_app_info",
        "info-icon"
    ) { context, _, _ ->
        InformationActivity(context).show()
    }

    @JvmField
    val SEARCH_FOR_UPDATE = Action(
        "action.update_search",
        "update-icon"
    ) { context, preferences, _ ->
        preferences.editor().put(PreferenceKey.LAST_UPDATE_SEARCH, LocalDateTime.now())
        runOutsideUIAsync(object : Task<Release?>() {
            init {
                setOnRunning {
                    context.showIndeterminateProgress()
                }
                setOnFailed {
                    context.stopProgress()
                    logger.error("Update search failed", it.source.exception)
                    context.showErrorDialog(
                        i18n("update.search.failed.title"),
                        i18n("update.search.failed.message"),
                        it.source.exception as Exception?
                    )
                }
                setOnSucceeded {
                    context.stopProgress()
                    value?.let {
                        UpdateActivity(context, it).show()
                    } ?: context.showInformationDialog(
                        i18n("app.up_to_date.title"),
                        i18n("app.up_to_date.message")
                    ) {}
                }
            }

            override fun call(): Release? = UpdateSearcher.default.search()
        })
    }

    fun applyOnScene(
        scene: Scene,
        context: Context,
        preferences: Preferences,
        databaseTracker: DatabaseTracker
    ) {
        val keyBindingActions = allActions().filter { null != it.keyBinding }
        scene.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            keyBindingActions.filter { it.keyBinding!!.match(event) }.forEach {
                it.invoke(context, preferences, databaseTracker)
            }
        }
    }

    private fun allActions(): List<Action> = javaClass.declaredFields
        .filter { it.type == Action::class.java }
        .map { it.get(this) as Action }
        .toList()

    private fun submitTask(context: Context, runnable: Runnable) {
        runOutsideUIAsync(object : Task<Unit>() {
            init {
                setOnRunning { context.takeIf(Context::isShowing)?.showIndeterminateProgress()  }
                setOnFailed { context.takeIf(Context::isShowing)?.stopProgress() }
                setOnSucceeded { context.takeIf(Context::isShowing)?.stopProgress() }
            }

            override fun call() {
                runnable.run()
            }
        })
    }
}
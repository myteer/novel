package org.myteer.novel.gui.action

import javafx.concurrent.Task
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.input.KeyEvent
import javafx.stage.Stage
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.dbcreator.DatabaseCreatorActivity
import org.myteer.novel.gui.dbcreator.DatabaseOpener
import org.myteer.novel.gui.dbmanager.DatabaseManagerActivity
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.info.InformationActivity
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.preferences.PreferencesActivity
import org.myteer.novel.gui.utils.runOutsideUIAsync
import org.myteer.novel.gui.utils.typeEquals
import org.myteer.novel.i18n.i18n
import org.myteer.novel.launcher.ActivityLauncher
import org.myteer.novel.launcher.LauncherMode
import org.myteer.novel.main.ApplicationRestart

object GlobalActions {
    val CREATE_DATABASE = Action(
        "action.create_database",
        "database-plus-icon",
        KeyBindings.createDatabase
    ) { context, preferences, databaseTracker ->
        DatabaseCreatorActivity(databaseTracker).show(context.getContextWindow()).ifPresent {
            submitTask(context, ActivityLauncher(preferences, databaseTracker, LauncherMode.INTERNAL, databaseMeta = it))
        }
    }

    val OPEN_DATABASE = Action(
        "action.open_database",
        "file-icon",
        KeyBindings.openDatabase
    ) { context, preferences, databaseTracker ->
        DatabaseOpener().showOpenDialog(context.getContextWindow())?.also {
            submitTask(context, ActivityLauncher(preferences, databaseTracker, LauncherMode.INTERNAL, databaseMeta = it))
        }
    }

    val OPEN_DATABASE_MANAGER = Action(
        "action.open_database_manager",
        "database-icon",
        KeyBindings.openDatabaseManager
    ) { context, _, databaseTracker ->
        DatabaseManagerActivity(databaseTracker).show(context.getContextWindow())
    }

    val OPEN_SETTINGS = Action(
        "action.settings",
        "settings-icon",
        KeyBindings.openSettings
    ) { context, preferences, _ ->
        PreferencesActivity(preferences).show(context.getContextWindow())
    }

    val FULL_SCREEN = Action(
        "action.full_screen",
        "full-screen-icon",
        KeyBindings.fullScreen
    ) { context, _, _ ->
        context.getContextWindow()?.also { if (it is Stage) it.isFullScreen = !it.isFullScreen }
    }

    val NEW_ENTRY = Action(
        "action.new_entry",
        "database-icon",
        KeyBindings.newEntry
    ) { context, preferences, databaseTracker ->
        submitTask(context, ActivityLauncher(preferences, databaseTracker, LauncherMode.INTERNAL))
    }

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

    val OPEN_APP_INFO = Action(
        "action.open_app_info",
        "info-icon"
    ) { context, _, _ ->
        InformationActivity(context).show()
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
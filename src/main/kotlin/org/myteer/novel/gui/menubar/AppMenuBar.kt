package org.myteer.novel.gui.menubar

import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.collections.ListChangeListener
import javafx.concurrent.Task
import javafx.scene.control.*
import javafx.stage.Stage
import javafx.stage.Window
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.action.GlobalActions
import org.myteer.novel.gui.action.MenuItems
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.main.MainView
import org.myteer.novel.gui.theme.Theme
import org.myteer.novel.gui.theme.Themeable
import org.myteer.novel.gui.utils.*
import org.myteer.novel.i18n.I18N
import org.myteer.novel.i18n.i18n
import org.myteer.novel.launcher.ActivityLauncher
import org.myteer.novel.launcher.LauncherMode
import org.myteer.novel.main.ApplicationRestart
import org.myteer.novel.utils.ReflectionUtils
import org.slf4j.LoggerFactory
import java.lang.ref.WeakReference
import java.util.*

class AppMenuBar(
    view: MainView,
    preferences: Preferences,
    databaseTracker: DatabaseTracker
) : MenuBar() {
    private lateinit var overlayNotShowing: BooleanBinding

    init {
        initDisablePolicy(view)
        menus.addAll(
            FileMenu(view, view.databaseMeta, preferences, databaseTracker),
            ModuleMenu(view),
            PreferencesMenu(view, preferences, databaseTracker),
            WindowMenu(view, preferences, databaseTracker)
        )
    }

    private fun initDisablePolicy(view: MainView) {
        overlayNotShowing = Bindings.isEmpty(view.getBlockingOverlaysShown())
            .and(Bindings.isEmpty(view.getNonBlockingOverlaysShown()))
            .apply {
                addListener { _, _, isEmpty ->
                    isDisable = !isEmpty
                }
            }
    }

    private class FileMenu(
        val context: Context,
        val databaseMeta: DatabaseMeta,
        val preferences: Preferences,
        val databaseTracker: DatabaseTracker
    ) : Menu(i18n("menubar.menu.file")) {
        init {
            menuItem(newEntry())
            menuItem(openDatabase())
            menuItem(createDatabase())
            menuItem(openDatabaseManager())
            menuItem(recentDatabases())
            menuItem(databaseClose())
            separator()
            menuItem(revealInExplorer())
            separator()
            menuItem(closeWindow())
            menuItem(restart())
            menuItem(quit())
        }

        private fun newEntry(): MenuItem = MenuItems.of(GlobalActions.NEW_ENTRY, context, preferences, databaseTracker)

        private fun openDatabase(): MenuItem = MenuItems.of(GlobalActions.OPEN_DATABASE, context, preferences, databaseTracker)

        private fun createDatabase(): MenuItem = MenuItems.of(GlobalActions.CREATE_DATABASE, context, preferences, databaseTracker)

        private fun openDatabaseManager(): MenuItem = MenuItems.of(GlobalActions.OPEN_DATABASE_MANAGER, context, preferences, databaseTracker)

        private fun recentDatabases(): MenuItem = object : Menu(i18n("menubar.menu.file.recent")) {
            private val it = this
            private val menuItemFactory: (DatabaseMeta) -> MenuItem = { db ->
                MenuItem(db.toString()).also { menuItem ->
                    menuItem.setOnAction {
                        startActivityLauncher {
                            ActivityLauncher(preferences, databaseTracker, LauncherMode.INTERNAL, databaseMeta = db)
                        }
                    }
                    menuItem.userData = db
                }
            }
            private val databaseTrackerObserver = object : DatabaseTracker.Observer {
                override fun onDatabaseAdded(databaseMeta: DatabaseMeta) {
                    it.menuItem(menuItemFactory(databaseMeta))
                }

                override fun onDatabaseRemoved(databaseMeta: DatabaseMeta) {
                    it.items.find { menuItem -> menuItem.userData == databaseMeta }?.also { menuItem ->
                        it.items.remove(menuItem)
                    }
                }
            }

            init {
                graphic("database-clock-icon")
                databaseTracker.getSavedDatabases().forEach { db -> menuItem(menuItemFactory(db)) }
                databaseTracker.registerObserver(databaseTrackerObserver)
            }
        }

        private fun databaseClose() = MenuItem(i18n("menubar.menu.file.dbclose"))
            .action {
                preferences.editor().put(PreferenceKey.LOGIN_DATA, preferences.get(PreferenceKey.LOGIN_DATA).apply {
                    if (getAutoLoginDatabase() == databaseMeta) {
                        setAutoLogin(false)
                        setAutoLoginCredentials(null)
                    }
                }).tryCommit()
                GlobalActions.NEW_ENTRY.invoke(context, preferences, databaseTracker)
                context.close()
            }
            .graphic("logout-icon")

        private fun revealInExplorer() = MenuItem(i18n("menubar.menu.file.reveal"))
            .action { databaseMeta.file!!.revealInExplorer() }
            .graphic("folder-open-icon")

        private fun closeWindow() = MenuItem(i18n("menubar.menu.file.closewindow"))
            .action { context.close() }
            .graphic("close-icon")

        private fun restart(): MenuItem = MenuItems.of(GlobalActions.RESTART_APPLICATION, context, preferences, databaseTracker)

        private fun quit() = MenuItem(i18n("menubar.menu.file.quit"))
            .action { Platform.exit() }
            .graphic("close-box-multiple-icon")

        private fun startActivityLauncher(getActivityLauncher: () -> ActivityLauncher) {
            runOutsideUIAsync(object : Task<Unit>() {
                init {
                    setOnRunning { context.takeIf(Context::isShowing)?.showIndeterminateProgress() }
                    setOnFailed { context.takeIf(Context::isShowing)?.stopProgress() }
                    setOnSucceeded { context.takeIf(Context::isShowing)?.stopProgress() }
                }

                override fun call() {
                    getActivityLauncher().run()
                }
            })
        }
    }

    private class ModuleMenu(view: MainView) : Menu(i18n("menubar.menu.modules")) {
        init {
            view.modules.forEach {
                menuItem(MenuItem(it.name, it.icon)).action { _ -> view.openModule(it) }
            }
        }
    }

    private class PreferencesMenu(
        private val context: Context,
        private val preferences: Preferences,
        private val databaseTracker: DatabaseTracker
    ) : Menu(i18n("menubar.menu.preferences")) {
        init {
            menuItem(settings())
            separator()
            menuItem(themeMenu())
            menuItem(langMenu())
        }

        private fun settings(): MenuItem = MenuItems.of(GlobalActions.OPEN_SETTINGS, context, preferences, databaseTracker)

        private fun themeMenu() = object : Menu(i18n("menubar.menu.preferences.theme")) {
            private val themeChangeListener = object : Themeable {
                override fun handleThemeApply(newTheme: Theme, oldTheme: Theme) {
                    items.forEach {
                        if (it is RadioMenuItem) {
                            it.isSelected = newTheme.javaClass == it.userData
                        }
                    }
                }
            }

            init {
                graphic("paint-icon")
                buildItems()
                Theme.registerThemeable(themeChangeListener)
            }

            private fun buildItems() {
                val toggleGroup = ToggleGroup()
                Theme.getAvailableThemesData().forEach { themeMeta ->
                    menuItem(RadioMenuItem(themeMeta.displayName).also {
                        it.toggleGroup = toggleGroup
                        it.userData = themeMeta.themeClass
                        it.isSelected = Theme.getDefault().javaClass == themeMeta.themeClass
                        it.action {
                            try {
                                val theme = ReflectionUtils.constructObject(themeMeta.themeClass)
                                logger.debug("The theme object: {}", theme)
                                Theme.setDefault(theme)
                                preferences.editor().put(PreferenceKey.THEME, theme)
                            } catch (e: Exception) {
                                logger.error("Couldn't set the theme", e)
                            }
                        }
                    })
                }
            }
        }

        private fun langMenu() = Menu(i18n("menubar.menu.preferences.lang")).also { menu ->
            val toggleGroup = ToggleGroup()
            I18N.getAvailableLocales().forEach { locale ->
                menu.menuItem(RadioMenuItem(locale.displayLanguage).also {
                    it.toggleGroup = toggleGroup
                    it.isSelected = Locale.getDefault() == locale
                    it.setOnAction {
                        preferences.editor().put(PreferenceKey.LOCALE, locale)
                        context.showConfirmationDialog(
                            i18n("app.lang.restart.title"),
                            i18n("app.lang.restart.message")
                        ) { btn ->
                            if (btn.typeEquals(ButtonType.YES)) {
                                ApplicationRestart.restart()
                            }
                        }
                    }
                })
            }
        }.graphic("translate-icon")
    }

    private class WindowMenu(
        private val context: Context,
        private val preferences: Preferences,
        private val databaseTracker: DatabaseTracker
    ) : Menu(i18n("menubar.menu.window")) {
        private val windowsChangeOperator = object {
            fun onWindowsAdded(windows: List<Window>) {
                windows.filter { it is Stage && null == it.owner }.map { it as Stage }.forEach { window ->
                    menuItem(CheckMenuItem().also {
                        it.userData = WeakReference<Window>(window)
                        it.textProperty().bind(window.titleProperty())
                        window.focusedProperty().addListener{ _, _, yes ->
                            it.isSelected = yes
                        }
                        it.setOnAction { _ ->
                            if (!it.isSelected) {
                                it.isSelected = true
                            } else {
                                window.toFront()
                            }
                        }
                    })
                }
            }

            fun onWindowsRemoved(windows: List<Window>) {
                windows.filter { it is Stage && null == it.owner }.forEach { window ->
                    val iterator = items.iterator()
                    while (iterator.hasNext()) {
                        val element = iterator.next()
                        if (element.userData is WeakReference<*>) {
                            if ((element.userData as WeakReference<*>).get() == window) {
                                iterator.remove()
                            }
                        }
                    }
                }
            }
        }
        private val windowListChangeListener = ListChangeListener<Window> {
            while (it.next()) {
                when {
                    it.wasAdded() -> windowsChangeOperator.onWindowsAdded(it.addedSubList)
                    it.wasRemoved() -> windowsChangeOperator.onWindowsRemoved(it.removed)
                }
            }
        }

        init {
            menuItem(fullScreen())
            separator()
            windowsChangeOperator.onWindowsAdded(Window.getWindows())
            WeakWindowsChangeListener(WeakReference(windowListChangeListener))
        }

        private fun fullScreen(): MenuItem = MenuItems.of(GlobalActions.FULL_SCREEN, context, preferences, databaseTracker, ::CheckMenuItem).apply {
            context.onWindowPresent {
                if (it is Stage) {
                    it.fullScreenProperty().addListener { _, _, isFullScreen ->
                        selectedProperty().set(isFullScreen)
                    }
                }
            }
        }

        private class WeakWindowsChangeListener(private val weakReference: WeakReference<ListChangeListener<Window>>) : ListChangeListener<Window> {
            init {
                Window.getWindows().addListener(this)
            }

            override fun onChanged(c: ListChangeListener.Change<out Window>?) {
                weakReference.get()?.onChanged(c) ?: Window.getWindows().removeListener(this)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AppMenuBar::class.java)
    }
}
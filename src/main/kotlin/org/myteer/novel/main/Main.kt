package org.myteer.novel.main

import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.config.login.LoginData
import org.myteer.novel.exception.UncaughtExceptionHandler
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.theme.Theme
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n
import org.myteer.novel.instance.ApplicationInstanceService
import org.myteer.novel.launcher.ActivityLauncher
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.system.exitProcess

class Main : BaseApplication() {
    companion object {
        private val logger = LoggerFactory.getLogger(Main::class.java)

        fun main(vararg args: String) {
            // 设置未捕获异常处理器
            Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler())
            // 单应用实例控制
            ApplicationInstanceService.open(args.toList())
            // 启动应用程序
            launchApp(Main::class.java, *args)
        }
    }

    override fun init() {
        val queue = ActivityLauncher.PostLaunchQueue()

        handleApplicationArgument(queue)

        val preferences = readConfigurations(queue)
        applyBaseConfigurations(preferences)
        applyAdditionalConfigurations(preferences)

        logger.debug("Locale is: {}", Locale.getDefault())
        logger.debug("Theme is: {}", Theme.getDefault())

        val databaseTracker = DatabaseTracker.global
        val loginData = readLoginData(preferences, databaseTracker)

        launchGUI(preferences, loginData, databaseTracker, queue)
    }

    @Init
    private fun handleApplicationArgument(queue: ActivityLauncher.PostLaunchQueue) {
        getFormattedApplicationArg(ArgumentTransformer::transform).ifPresent { file ->
            notifyPreloader("preloader.file.open", file.name)
            queue.pushItem { context, launchedDatabase ->
                launchedDatabase?.let {
                    context.showInformationNotification(i18n("database.file.launched", it.name), "")
                }
            }
        }
    }

    @Init
    private fun readConfigurations(queue: ActivityLauncher.PostLaunchQueue): Preferences {
        notifyPreloader("preloader.preferences.read")
        try {
            val preferences = Preferences.getPreferences()
            logger.info("Configurations has been read successfully!")
            return preferences
        } catch (e: Exception) {
            logger.error("Couldn't read configurations", e)
            queue.pushItem { context, _ ->
                context.showErrorNotification(i18n("preferences.read.failed.title"), "") {
                    context.showErrorDialog(
                        i18n("preferences.read.failed.title"),
                        i18n("preferences.read.failed.message"),
                        e
                    )
                }
            }
            return Preferences.empty()
        }
    }

    @Init
    private fun applyBaseConfigurations(preferences: Preferences) {
        notifyPreloader("preloader.lang")
        Locale.setDefault(preferences.get(PreferenceKey.LOCALE))
        notifyPreloader("preloader.theme")
        Theme.setDefault(preferences.get(PreferenceKey.THEME))
    }

    @Init
    private fun applyAdditionalConfigurations(preferences: Preferences) {
        applyWindowsOpacity(preferences)
        loadDefaultKeyBindings(preferences)
    }

    private fun applyWindowsOpacity(preferences: Preferences) {
        val opacity = preferences.get(BaseWindow.GLOBAL_OPACITY_CONFIG_KEY)
        logger.debug("Global window opacity read: {}", opacity)
        BaseWindow.globalOpacity.set(opacity)
    }

    private fun loadDefaultKeyBindings(preferences: Preferences) {
        KeyBindings.loadFrom(preferences)
    }

    @Init
    private fun readLoginData(preferences: Preferences, databaseTracker: DatabaseTracker): LoginData {
        notifyPreloader("preloader.login_data.read")
        return preferences.get(PreferenceKey.LOGIN_DATA).also {
            it.getSavedDatabases().forEach(databaseTracker::saveDatabase)
        }
    }

    @Init
    private fun launchGUI(preferences: Preferences,
                          loginData: LoginData,
                          databaseTracker: DatabaseTracker,
                          queue: ActivityLauncher.PostLaunchQueue) {
        notifyPreloader("preloader.gui.build")
        InitActivityLauncher(preferences, loginData, databaseTracker, getApplicationArgs(), queue).run()
    }

    override fun stop() {
        logger.info("Saving configurations")
        Preferences.getPreferences().editor().tryCommit()

        logger.info("Shutting down application instance service")
        ApplicationInstanceService.release()

        exitProcess(0)
    }

    private class InitActivityLauncher(
        private val preferences: Preferences,
        private val loginData: LoginData,
        databaseTracker: DatabaseTracker,
        params: List<String>,
        postLaunchQueue: PostLaunchQueue
    ) : ActivityLauncher(preferences, databaseTracker, params = params, postLaunchQueue = postLaunchQueue) {
        override fun getLoginData(): LoginData = loginData

        override fun saveLoginData(loginData: LoginData) {
            preferences.editor().put(PreferenceKey.LOGIN_DATA, loginData).tryCommit()
        }
    }

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Init()
}

fun main(vararg args: String) {
    // 设置系统参数
    PropertiesSetup.setupSystemProperties()
    // 启动应用
    Main.main(*args)
}
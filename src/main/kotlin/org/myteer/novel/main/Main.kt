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
package org.myteer.novel.main

import okhttp3.internal.notify
import okhttp3.internal.wait
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.config.login.LoginData
import org.myteer.novel.exception.UncaughtExceptionHandler
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.theme.Theme
import org.myteer.novel.gui.update.UpdateActivity
import org.myteer.novel.gui.utils.runInsideUIAsync
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.gui.wizard.WizardActivity
import org.myteer.novel.i18n.i18n
import org.myteer.novel.instance.ApplicationInstanceService
import org.myteer.novel.launcher.ActivityLauncher
import org.myteer.novel.update.Release
import org.myteer.novel.update.UpdateSearcher
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.*
import kotlin.system.exitProcess

class Main : BaseApplication() {
    companion object {
        private val logger = LoggerFactory.getLogger(Main::class.java)
        private val initThreadLock = Main::class.java

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
        if (!showWizardActivity(preferences)) {
            applyBaseConfigurations(preferences)
        }
        applyAdditionalConfigurations(preferences)

        logger.debug("Locale is: {}", Locale.getDefault())
        logger.debug("Theme is: {}", Theme.getDefault())

        val databaseTracker = DatabaseTracker.global
        val loginData = readLoginData(preferences, databaseTracker)

        // searching for updates, if necessary
        val updateRelease = searchForUpdates(preferences)

        // launching the main gui environment
        launchGUI(preferences, loginData, updateRelease, databaseTracker, queue)
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
    private fun showWizardActivity(preferences: Preferences): Boolean {
        synchronized(initThreadLock) {
            if (WizardActivity.isNeeded(preferences)) {
                hidePreloader()
                logger.debug("WizardDialog needed")
                runInsideUIAsync {
                    synchronized(initThreadLock) {
                        WizardActivity(preferences).show()
                        initThreadLock.notify()
                    }
                }
                initThreadLock.wait()
                showPreloader()
                return true
            }
            return false
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
    private fun searchForUpdates(preferences: Preferences): Release? {
        if (preferences.get(PreferenceKey.SEARCH_UPDATES)) {
            notifyPreloader("preloader.update.search")
            preferences.editor().put(PreferenceKey.LAST_UPDATE_SEARCH, LocalDateTime.now())
            return UpdateSearcher.default.trySearch {
                logger.error("Couldn't search for updates", it)
            }
        }
        return null
    }

    @Init
    private fun launchGUI(preferences: Preferences,
                          loginData: LoginData,
                          updateRelease: Release?,
                          databaseTracker: DatabaseTracker,
                          queue: ActivityLauncher.PostLaunchQueue) {
        notifyPreloader("preloader.gui.build")
        InitActivityLauncher(preferences, loginData, updateRelease, databaseTracker, getApplicationArgs(), queue).run()
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
        private val updateRelease: Release?,
        databaseTracker: DatabaseTracker,
        params: List<String>,
        postLaunchQueue: PostLaunchQueue
    ) : ActivityLauncher(preferences, databaseTracker, params = params, postLaunchQueue = postLaunchQueue) {
        override fun getLoginData(): LoginData = loginData

        override fun saveLoginData(loginData: LoginData) {
            preferences.editor().put(PreferenceKey.LOGIN_DATA, loginData).tryCommit()
        }

        override fun onActivityLaunched(context: Context) {
            updateRelease?.let {
                UpdateActivity(context, it).show()
            }
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
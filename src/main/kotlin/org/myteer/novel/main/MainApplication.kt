package org.myteer.novel.main

import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Stage
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.exception.UncaughtExceptionHandler
import org.myteer.novel.gui.dbcreator.DatabaseCreatorActivity
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.login.DatabaseLoginListener
import org.myteer.novel.gui.login.LoginActivity
import org.myteer.novel.gui.theme.LightTheme
import org.myteer.novel.gui.theme.Theme
import org.myteer.novel.gui.utils.window
import org.slf4j.LoggerFactory
import java.util.*

class MainApplication : BaseApplication() {
    companion object {
        private val logger = LoggerFactory.getLogger(MainApplication::class.java)

        fun main(vararg args: String) {
            // 设置系统参数
            PropertiesSetup.setupSystemProperties()
            // 设置未捕获异常处理器
            Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler())
            // 启动应用程序
            launchApp(MainApplication::class.java, *args)
        }
    }

    override fun init() {
        notifyPreloader(Preloader.MessageNotification("1111", false))
        Thread.sleep(1000)
        notifyPreloader(Preloader.MessageNotification("2222", false))
        Thread.sleep(1000)
        applyBaseConfigurations(Preferences.global)
        logger.debug("Locale: {}", Locale.getDefault())
        logger.debug("Theme: {}", Theme.getDefault())
    }

    @Init
    private fun applyBaseConfigurations(preferences: Preferences) {
        notifyPreloader("preloader.lang")
        Locale.setDefault(preferences.get(PreferenceKey.LOCALE))
        notifyPreloader("preloader.theme")
        Theme.setDefault(preferences.get(PreferenceKey.THEME))
    }

    override fun start(primaryStage: Stage?) {
        val button = Button("登录").apply {
            setOnAction {
                val preferences = Preferences.global
                val databaseTracker = DatabaseTracker.global
                val loginData = preferences.get(PreferenceKey.LOGIN_DATA)
                val listener = object : DatabaseLoginListener {
                    override fun onDatabaseOpened(database: NitriteDatabase) {
                        logger.info("open database: {}", database)
                    }
                }
                val loginActivity = LoginActivity(preferences, databaseTracker, loginData, listener)
                loginActivity.show()
            }
        }
        primaryStage?.scene = Scene(Group(button)).apply {
           /* JMetro(Style.LIGHT).scene = this
            stylesheets.add(Main::class.java.getResource("/org/myteer/novel/gui/theme/light.css")?.toExternalForm())*/
        }
        primaryStage?.title = System.getProperty(PropertiesSetup.APP_NAME)
        primaryStage?.show()
    }

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Init()
}

fun main(vararg args: String) {
    MainApplication.main(*args)
}
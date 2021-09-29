package org.myteer.novel.main

import com.sun.javafx.application.LauncherImpl
import javafx.application.Application
import javafx.stage.Stage
import java.util.*

abstract class BaseApplication : Application() {
    abstract override fun init()

    override fun start(primaryStage: Stage?) { }

    protected fun notifyPreloader(i18n: String, vararg args: String) {
        notifyPreloader(Preloader.MessageNotification(i18n, true, *args))
    }

    protected fun showPreloader() {
        notifyPreloader(Preloader.ShowNotification())
    }

    protected fun hidePreloader() {
        notifyPreloader(Preloader.HideNotification())
    }

    fun getApplicationArgs(): List<String> = parameters.raw

    private fun hasApplicationArgs(): Boolean = getApplicationArgs().isNotEmpty()

    protected fun <T> getFormattedApplicationArg(converter: (List<String>) -> T?): Optional<T> {
        return if (!hasApplicationArgs()) {
            Optional.empty()
        } else {
            Optional.ofNullable(converter.invoke(getApplicationArgs()))
        }
    }

    companion object {
        fun <T : BaseApplication> launchApp(appClass: Class<T>, vararg args: String) {
            LauncherImpl.launchApplication(appClass, Preloader::class.java, args)
        }
    }
}
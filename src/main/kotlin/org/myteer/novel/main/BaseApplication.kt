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
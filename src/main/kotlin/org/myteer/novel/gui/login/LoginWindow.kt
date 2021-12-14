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
package org.myteer.novel.gui.login

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableStringValue
import javafx.event.EventHandler
import javafx.stage.WindowEvent
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.action.GlobalActions
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class LoginWindow(
    private val root: LoginView,
    private val preferences: Preferences,
    private val databaseTracker: DatabaseTracker
) : BaseWindow<LoginView>(TitleProperty("window.login.title", " - ", root.titleProperty()), root), EventHandler<WindowEvent> {
    init {
        minWidth = 530.0
        minHeight = 530.0
        isMaximized = true
        showExitDialog = true
        addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this)
        initKeyBindings()
    }

    private fun initKeyBindings() {
        GlobalActions.applyOnScene(scene, root, preferences, databaseTracker)
    }

    override fun handle(event: WindowEvent?) {
        preferences.editor().put(PreferenceKey.LOGIN_DATA, root.loginData)
    }

    private class TitleProperty(i18n: String, separator: String, extended: ObservableStringValue) : SimpleStringProperty() {
        init {
            val baseTitle = i18n(i18n)
            val totalTitle = Bindings.createStringBinding({
                extended.get()?.takeIf { "null" != it }?.let { baseTitle + separator + it } ?: baseTitle
            }, extended)
            bind(totalTitle)
        }
    }
}
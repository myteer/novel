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
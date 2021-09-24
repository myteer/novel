package org.myteer.novel.gui.login

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.stage.WindowEvent
import org.myteer.novel.config.Preferences
import org.myteer.novel.config.login.LoginData
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import java.lang.ref.WeakReference
import java.util.stream.Collectors

class LoginActivity(
    private val preferences: Preferences,
    private val databaseTracker: DatabaseTracker,
    loginData: LoginData,
    databaseLoginListener: DatabaseLoginListener
) {
    companion object {
        private val instances: MutableList<WeakReference<LoginActivity>> = mutableListOf()

        fun getActiveLoginActivities(): List<LoginActivity> {
            return instances.stream()
                .map { it.get() }
                .filter { null != it && it.isShowing() }
                .map { it!! }
                .collect(Collectors.toList())
        }
    }
    private val showing: BooleanProperty = SimpleBooleanProperty()
    private var loginView: LoginView?

    init {
        loginView = LoginView(preferences, databaseTracker, loginData, databaseLoginListener)
        instances.add(WeakReference(this))
    }

    fun show() {
        if (!isShowing()) {
            val loginWindow = buildLoginWindow()
            showing.bind(loginWindow.showingProperty())
            loginWindow.show()
        }
    }

    private fun buildLoginWindow(): LoginWindow {
        return LoginWindow(loginView!!, preferences).apply {
            addEventHandler(WindowEvent.WINDOW_HIDDEN) {
                loginView = null
                showing.unbind()
                showing.set(false)
            }
        }
    }

    fun isShowing() = showing.get()

    fun getContext(): Context = loginView!!
}
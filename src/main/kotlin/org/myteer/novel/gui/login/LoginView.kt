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

import javafx.beans.value.ObservableStringValue
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.config.login.Credentials
import org.myteer.novel.config.login.LoginData
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.base.BaseView
import org.myteer.novel.gui.dbcreator.DatabaseCreatorActivity
import org.myteer.novel.gui.dbcreator.DatabaseOpener
import org.myteer.novel.gui.dbmanager.DatabaseManagerActivity
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.main.MainActivity
import org.myteer.novel.gui.utils.runInsideUI
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory

class LoginView(
    preferences: Preferences,
    databaseTracker: DatabaseTracker,
    loginData: LoginData,
    databaseLoginListener: DatabaseLoginListener
) : BaseView() {
    private val loginBoxController = LoginBoxController(this, preferences, databaseTracker, loginData, databaseLoginListener)

    val loginData: LoginData
        get() = loginBoxController.loginData

    init {
        setContent(LoginViewBase(loginBoxController))
    }

    fun titleProperty(): ObservableStringValue = loginBoxController.titleProperty()!!

    private class LoginBoxController(
        override val context: Context,
        override val preferences: Preferences,
        override val databaseTracker: DatabaseTracker,
        override val loginData: LoginData,
        private val databaseLoginListener: DatabaseLoginListener
    ) : LoginBox.Controller, DatabaseTracker.Observer {
        override var loginBox: LoginBox? = null
            set(value) {
                value?.takeIf { field !== it }?.let(this::initLoginBox)
                field = value
            }

        init {
            databaseTracker.registerObserver(this)
        }

        fun titleProperty() = loginBox?.titleProperty()

        private fun initLoginBox(box: LoginBox) {
            box.fillForm(loginData)
            box.addSelectedItemListener {
                it?.let(loginData::setSelectedDatabase)
            }
        }

        override fun openDatabaseManager() {
            DatabaseManagerActivity(databaseTracker).show(context.getContextWindow())
        }

        override fun openFile() {
            DatabaseOpener().showOpenMultipleDialog(context.getContextWindow())
                .stream()
                .peek(databaseTracker::saveDatabase)
                .reduce { _, s -> s }
                .ifPresent { loginBox?.select(it) }
        }

        override fun openDatabaseCreator() {
            DatabaseCreatorActivity(databaseTracker).show(context.getContextWindow()).ifPresent {
                loginBox?.select(it)
            }
        }

        override fun login(databaseMeta: DatabaseMeta, credentials: Credentials, remember: Boolean) {
            if (databaseTracker.isDatabaseUsed(databaseMeta)) {
                MainActivity.getByDatabase(databaseMeta).map(MainActivity::getContext).ifPresent(Context::toFrontRequest)
                context.close()
            } else {
                loginData.setAutoLogin(remember)
                loginData.setAutoLoginCredentials(credentials.takeIf { remember })

                NitriteDatabase.builder()
                    .databaseMeta(databaseMeta)
                    .onFailed { message, t ->
                        context.showErrorDialog(i18n("login.failed"), message, t as Exception?)
                        logger.error("Failed to create/open the database", t)
                    }.build(credentials)?.let {
                        logger.debug("Login was successful, closing the LoginWindow")
                        preferences.editor().put(PreferenceKey.LOGIN_DATA, loginData)
                        databaseLoginListener.onDatabaseOpened(it)
                        context.close()
                    }
            }

        }

        override fun onDatabaseAdded(databaseMeta: DatabaseMeta) {
            runInsideUI {
                loginBox?.addItem(databaseMeta)
                loginData.addSavedDatabase(databaseMeta)
            }
        }

        override fun onDatabaseRemoved(databaseMeta: DatabaseMeta) {
            runInsideUI {
                loginBox?.removeItem(databaseMeta)
                loginData.removeSavedDatabase(databaseMeta)
            }
        }

        override fun onDatabaseUsing(databaseMeta: DatabaseMeta) {
            runInsideUI {
                loginBox?.refresh()
                when (databaseMeta) {
                    loginBox?.selectedItem -> {
                        loginBox?.select(null)
                    }
                }
            }
        }

        override fun onDatabaseClosing(databaseMeta: DatabaseMeta) {
            runInsideUI {
                loginBox?.refresh()
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LoginView::class.java)
    }
}
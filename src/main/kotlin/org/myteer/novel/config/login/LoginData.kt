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
package org.myteer.novel.config.login

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.myteer.novel.db.DatabaseMeta

class LoginData(savedDatabases: List<DatabaseMeta> = listOf()) {
    private val savedDatabases: ObservableList<DatabaseMeta>
    private var selectedDatabase: DatabaseMeta? = null
    private var autoLoginCredentials: Credentials? = null
    private var autoLogin: Boolean = false

    init {
        this.savedDatabases = FXCollections.observableArrayList(savedDatabases)
    }

    fun getSavedDatabases(): List<DatabaseMeta> {
        return savedDatabases.toList()
    }

    @Synchronized
    fun addSavedDatabase(database: DatabaseMeta): Boolean {
        return if (!savedDatabases.contains(database)) {
            savedDatabases.add(database)
        } else {
            false
        }
    }

    fun removeSavedDatabase(database: DatabaseMeta) {
        savedDatabases.remove(database)
    }

    fun setSelectedDatabase(database: DatabaseMeta?) {
        database?.let { addSavedDatabase(it) }
        selectedDatabase = database
    }

    fun getSelectedDatabase() = selectedDatabase

    fun setSelectedDatabaseIndex(index: Int) {
        savedDatabases.getOrNull(index)?.let { selectedDatabase = it }
    }

    fun getSelectedDatabaseIndex(): Int = savedDatabases.indexOf(selectedDatabase)

    fun getAutoLoginDatabase(): DatabaseMeta? {
        return if (autoLogin) selectedDatabase else null
    }

    fun getAutoLoginCredentials(): Credentials {
        return autoLoginCredentials ?: Credentials.anonymous()
    }

    fun setAutoLoginCredentials(credentials: Credentials?) {
        autoLoginCredentials = credentials
    }

    fun isAutoLogin(): Boolean = autoLogin && null != selectedDatabase

    fun setAutoLogin(value: Boolean) {
        autoLogin = value
    }

    override fun toString(): String {
        return "LoginData(savedDatabases=$savedDatabases, selectedDatabase=$selectedDatabase, autoLogin=$autoLogin)"
    }

    companion object {
        fun empty() = LoginData()
    }
}
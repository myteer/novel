package org.myteer.novel.config.login

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.myteer.novel.db.DatabaseMeta

class LoginData(savedDatabases: List<DatabaseMeta> = listOf()) {
    val savedDatabases: ObservableList<DatabaseMeta>
    private var selectedDatabase: DatabaseMeta? = null
    private var autoLoginCredentials: Credentials? = null
    private var autoLogin: Boolean = false

    init {
        this.savedDatabases = FXCollections.observableArrayList(savedDatabases)
    }

    fun setSelectedDatabase(database: DatabaseMeta) {
        if (!savedDatabases.contains(database)) {
            savedDatabases.add(database)
        }
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
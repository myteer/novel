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
package org.myteer.novel.gui.entry

import org.myteer.novel.config.Preferences
import org.myteer.novel.config.login.LoginData
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.login.DatabaseLoginListener
import org.myteer.novel.gui.login.LoginActivity
import org.myteer.novel.gui.main.MainActivity
import java.lang.ref.WeakReference
import java.util.*
import java.util.stream.Collectors

class EntryActivity(
    private val preferences: Preferences,
    private val databaseTracker: DatabaseTracker,
    private val loginData: LoginData
) : DatabaseLoginListener {
    companion object {
        private val instances: MutableList<WeakReference<EntryActivity>> = Collections.synchronizedList(mutableListOf())

        fun getActiveEntryActivities(): List<EntryActivity> {
            return instances.stream()
                .map { it.get() }
                .filter { it?.isShowing() ?: false }
                .map { it!! }
                .collect(Collectors.toList())
        }
    }
    private var subContext: Context? = null

    init {
        instances.add(WeakReference(this))
    }

    override fun onDatabaseOpened(database: NitriteDatabase) {
        val mainActivity = MainActivity(database, preferences, databaseTracker)
        subContext = mainActivity.getContext()
        mainActivity.show()
    }

    fun show() {
        if (!isShowing()) {
            val loginActivity = LoginActivity(preferences, databaseTracker, loginData, this)
            subContext = loginActivity.getContext()
            loginActivity.show()
        }
    }

    fun isShowing(): Boolean {
        return subContext?.isShowing() ?: false
    }

    fun getContext(): Context = subContext!!
}
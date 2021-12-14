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
package org.myteer.novel.gui.main

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.stage.WindowEvent
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.main.task.BooksRefreshTask
import org.myteer.novel.gui.menubar.AppMenuBar
import org.myteer.novel.gui.utils.runOutsideUI
import java.lang.ref.WeakReference
import java.util.*

class MainActivity(
    private val database: NitriteDatabase,
    private val preferences: Preferences,
    private val databaseTracker: DatabaseTracker
) {
    companion object {
        private val instances: MutableSet<WeakReference<MainActivity>> = Collections.synchronizedSet(mutableSetOf())

        fun getByDatabase(databaseMeta: DatabaseMeta): Optional<MainActivity> {
            return instances.stream()
                .map { it.get() }
                .filter {
                    it?.database?.meta?.equals(databaseMeta) ?: false
                }
                .map { it!! }
                .filter(MainActivity::isShowing)
                .findAny()
        }
    }
    private val showing: BooleanProperty = SimpleBooleanProperty()
    private val mainView = MainView(preferences, database, databaseTracker)

    init {
        instances.add(WeakReference(this))
        databaseTracker.registerUsedDatabase(database.meta)
    }

    fun show() {
        val window = MainWindow(mainView, AppMenuBar(mainView, preferences, databaseTracker))
        window.show()
        window.addEventHandler(WindowEvent.WINDOW_HIDDEN) {
            database.close()
            databaseTracker.registerClosedDatabase(database.meta)
            showing.set(false)
        }
        showing.set(true)
        runOutsideUI(BooksRefreshTask(database))
    }

    fun isShowing(): Boolean = showing.get()

    fun getContext(): Context = mainView
}
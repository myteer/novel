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
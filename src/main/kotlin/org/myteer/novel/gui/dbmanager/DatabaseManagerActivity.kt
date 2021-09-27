package org.myteer.novel.gui.dbmanager

import javafx.stage.Window
import org.myteer.novel.gui.entry.DatabaseTracker

class DatabaseManagerActivity(private val databaseTracker: DatabaseTracker) {
    fun show(owner: Window?) {
        val view = DatabaseManagerView(databaseTracker)
        val window = DatabaseManagerWindow(view, owner)
        window.show()
    }
}
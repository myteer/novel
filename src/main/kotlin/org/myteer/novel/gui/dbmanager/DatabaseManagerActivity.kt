package org.myteer.novel.gui.dbmanager

import javafx.stage.Window
import org.myteer.novel.gui.entry.DatabaseTracker

class DatabaseManagerActivity {
    fun show(databaseTracker: DatabaseTracker, owner: Window?) {
        val view = DatabaseManagerView(databaseTracker)
        val window = DatabaseManagerWindow(view, owner)
        window.show()
    }
}
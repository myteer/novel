package org.myteer.novel.gui.dbcreator

import javafx.stage.Window
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.entry.DatabaseTracker
import java.util.*

class DatabaseCreatorActivity(private val databaseTracker: DatabaseTracker) {
    fun show(owner: Window?): Optional<DatabaseMeta> {
        val view = DatabaseCreatorView(databaseTracker)
        val window = DatabaseCreatorWindow(view, owner)
        window.showAndWait()
        return Optional.ofNullable(view.getCreatedDatabase())
    }
}
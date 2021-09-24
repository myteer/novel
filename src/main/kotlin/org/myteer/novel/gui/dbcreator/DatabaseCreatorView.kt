package org.myteer.novel.gui.dbcreator

import javafx.scene.layout.VBox
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.base.BaseView
import org.myteer.novel.gui.entry.DatabaseTracker

class DatabaseCreatorView(databaseTracker: DatabaseTracker) : BaseView() {
    private val form: DatabaseCreatorForm

    init {
        form = DatabaseCreatorForm(this, databaseTracker)
        setContent(VBox(DatabaseCreatorToolBar(), form))
    }

    fun getCreatedDatabase(): DatabaseMeta? {
        return form.createdDatabase.get()
    }
}
package org.myteer.novel.gui.dbmanager

import javafx.beans.binding.IntegerBinding
import javafx.scene.layout.VBox
import org.myteer.novel.gui.base.BaseView
import org.myteer.novel.gui.entry.DatabaseTracker

class DatabaseManagerView(databaseTracker: DatabaseTracker) : BaseView() {
    private val table: DatabaseManagerTable

    init {
        table = DatabaseManagerTable(this, databaseTracker)
        setContent(VBox(DatabaseManagerToolBar(this), table))
    }

    fun refresh() = table.refresh()

    fun itemsCount(): IntegerBinding = table.itemsCount()

    fun selectedItemsCount(): IntegerBinding = table.selectedItemsCount()
}
package org.myteer.novel.gui.export.control

import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import org.myteer.novel.db.data.BookProperty
import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.gui.control.CheckListView
import org.myteer.novel.gui.utils.checkedItems

class BookPropertyChecker(exportConfiguration: BookExportConfiguration) : CheckListView<BookProperty<*>>() {
    init {
        GridPane.setColumnSpan(this, 3)
        GridPane.setVgrow(this, Priority.SOMETIMES)
        items = FXCollections.observableArrayList(exportConfiguration.availableFields)
        exportConfiguration.requiredFields.forEach(checkModel::check)
        checkedItems.addListener(ListChangeListener {
            exportConfiguration.requiredFields = checkedItems.toList()
        })
    }
}
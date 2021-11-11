package org.myteer.novel.gui.export.control

import javafx.collections.FXCollections
import javafx.scene.control.ChoiceBox
import org.myteer.novel.db.data.BookProperty
import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.gui.utils.onValuePresent
import org.myteer.novel.gui.utils.selectedItem
import org.myteer.novel.gui.utils.selectedItemProperty

class SortingPropertyChooser(
    exportConfiguration: BookExportConfiguration
) : ChoiceBox<BookProperty<Comparable<*>>?>() {
    init {
        maxWidth = Double.MAX_VALUE
        items = FXCollections.observableArrayList(listOf(null) + BookProperty.sortableProperties)
        selectedItem = exportConfiguration.fieldToSortBy
        selectedItemProperty().onValuePresent {
            exportConfiguration.fieldToSortBy = it
        }
    }
}
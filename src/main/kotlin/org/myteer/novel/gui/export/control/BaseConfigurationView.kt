package org.myteer.novel.gui.export.control

import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.gui.utils.addRow
import org.myteer.novel.i18n.i18n

open class BaseConfigurationView<C : BookExportConfiguration>(
    private val exportConfiguration: C
) : GridPane() {
    init {
        padding = Insets(20.0)
        hgap = 10.0
        vgap = 10.0
        buildUI()
    }

    private fun buildUI() {
        initColumnConstraints()
        addRow(
            Label(i18n("book.export.sort_by")),
            Label(i18n("book.export.sort_locale"))
        )
        addRow(
            SortingPropertyChooser(exportConfiguration),
            SortingLocaleChooser(exportConfiguration),
            ReverseItemsToggle(exportConfiguration)
        )
        addRow(Label(i18n("book.export.fields")))
        addRow(BookPropertyChecker(exportConfiguration))
    }

    private fun initColumnConstraints() {
        columnConstraints.addAll(
            List(3) {
                ColumnConstraints().apply {
                    hgrow = Priority.ALWAYS
                }
            }
        )
    }
}
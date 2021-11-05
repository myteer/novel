package org.myteer.novel.gui.bookmanager.config

import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import jfxtras.styles.jmetro.JMetroStyleClass
import org.controlsfx.control.CheckListView
import org.myteer.novel.gui.bookmanager.BookManagerView
import org.myteer.novel.gui.bookmanager.BookTable
import org.myteer.novel.gui.control.BaseTable
import org.myteer.novel.gui.utils.*
import org.myteer.novel.i18n.I18N
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory
import java.util.*

class BookManagerViewConfigurationPanel(private val bookManagerView: BookManagerView) : GridPane() {
    init {
        styleClass.add(JMetroStyleClass.BACKGROUND)
        styleClass.add("book-manager-view-config-panel")
        padding = Insets(10.0, 20.0, 10.0, 20.0)
        hgap = 10.0
        vgap = 10.0
        buildUI()
    }

    private fun buildUI() {
        add(Label(i18n("book.table.sort")), 0, 0)
        add(buildSortChooserMenu(), 0, 1)
        add(Label(i18n("book.table.preferred_columns")), 0, 2)
        add(buildColumnSelection(), 0, 3)
    }

    private fun buildSortChooserMenu() = ChoiceBox<Locale>().apply {
        setHgrow(this, Priority.ALWAYS)
        maxWidth = Double.MAX_VALUE
        items.addAll(I18N.getAvailableCollators().keys)
        selectedItem = bookManagerView.sort
        valueConvertingPolicy(Locale::getDisplayLanguage, Locale::forLanguageTag)
        selectedItemProperty().onValuePresent(::onSortSelection)
    }

    private fun buildColumnSelection() = CheckListView<BaseTable.ColumnType>().run {
        items = FXCollections.observableArrayList(*BookTable.columnList().toTypedArray())
        bookManagerView.columnsInfo.columnTypes.forEach(checkModel::check)
        checkedItems.addListener(ListChangeListener { onColumnSelection(checkedItems.toList()) })
        VBox.setVgrow(this, Priority.ALWAYS)
        VBox(3.0, this, buildColumnResetButton()).also {
            setVgrow(it, Priority.ALWAYS)
        }
    }

    private fun CheckListView<BaseTable.ColumnType>.buildColumnResetButton() = Button().apply {
        maxWidth = Double.MAX_VALUE
        text = i18n("book.table.columns.reset")
        graphic = icon("columns-icon")
        setOnAction {
            val defaults = BookTable.columnList().filter { it.isDefaultVisible() }
            val toUncheck = checkedItems - defaults
            val toCheck = defaults - checkedItems
            toUncheck.forEach(checkModel::clearCheck)
            toCheck.forEach(checkModel::check)
        }
    }

    private fun onSortSelection(locale: Locale) {
        logger.debug("Sort selection detected")
        bookManagerView.sort = locale
    }

    private fun onColumnSelection(items: List<BaseTable.ColumnType>) {
        logger.debug("Column selection detected")
        bookManagerView.columnsInfo = BookManagerView.TableColumnsInfo(items)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BookManagerViewConfigurationPanel::class.java)
    }
}
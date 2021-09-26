package org.myteer.novel.gui.dbmanager

import com.jfilegoodies.explorer.FileExplorers
import javafx.beans.binding.Bindings
import javafx.beans.binding.IntegerBinding
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Callback
import org.apache.commons.io.FileUtils
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.utils.I18NButtonType
import org.myteer.novel.gui.utils.copy
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.typeEquals
import org.myteer.novel.i18n.i18n

class DatabaseManagerTable(
    private val context: Context,
    private val databaseTracker: DatabaseTracker
) : TableView<DatabaseMeta>(), DatabaseTracker.Observer {
    companion object {
        private const val NOT_EXISTS_CLASS = "state-indicator-file-not-exists"
        private const val USED_CLASS = "state-indicator-used"
    }
    private val itemsCount: IntegerBinding
    private val selectedItemsCount: IntegerBinding

    init {
        databaseTracker.registerObserver(this)
        itemsCount = Bindings.size(items)
        selectedItemsCount = Bindings.size(selectionModel.selectedItems)
        placeholder = Label(i18n("database.manager.table.place.holder"))
        selectionModel.selectionMode = SelectionMode.MULTIPLE
        columns.addAll(
            StateColumn(),
            NameColumn(),
            PathColumn(),
            SizeColumn(),
            FileOpenerColumn(),
            DeleteColumn()
        )
        items.addAll(databaseTracker.getSavedDatabases())
    }

    private inner class StateColumn : TableColumn<DatabaseMeta, String>(), Callback<TableColumn<DatabaseMeta, String>, TableCell<DatabaseMeta, String>> {
        init {
            isReorderable = false
            isSortable = false
            isResizable = false
            prefWidth = 30.0
            cellFactory = this
        }

        override fun call(tableColumn: TableColumn<DatabaseMeta, String>?): TableCell<DatabaseMeta, String> {
            return object : TableCell<DatabaseMeta, String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        text = null
                        graphic = null
                        tooltip = null
                    } else {
                        val databaseMeta = tableView.items[index]
                        val dbFile = databaseMeta.file!!
                        if (dbFile.exists().not() || dbFile.isDirectory) {
                            graphic = icon("warning-icon").apply { styleClass.add(NOT_EXISTS_CLASS) }
                            tableRow.tooltip = Tooltip(i18n("file.not.exists"))
                        } else if(databaseTracker.isDatabaseUsed(databaseMeta)) {
                            graphic = icon("pay-icon").apply { styleClass.add(USED_CLASS) }
                            tableRow.tooltip = Tooltip(i18n("database.currently.used"))
                        } else {
                            graphic = null
                            tableRow.tooltip = null
                        }
                    }
                }
            }
        }
    }

    private class NameColumn : TableColumn<DatabaseMeta, String>(i18n("database.manager.table.column.name")) {
        init {
            isReorderable = false
            cellValueFactory = PropertyValueFactory("name")
        }
    }

    private class PathColumn : TableColumn<DatabaseMeta, String>(i18n("database.manager.table.column.path")) {
        init {
            isReorderable = false
            cellValueFactory = PropertyValueFactory("file")
        }
    }

    private class SizeColumn : TableColumn<DatabaseMeta, String>(i18n("database.manager.table.column.size")), Callback<TableColumn<DatabaseMeta, String>, TableCell<DatabaseMeta, String>> {
        init {
            isReorderable = false
            cellFactory = this
        }

        override fun call(tableColumn: TableColumn<DatabaseMeta, String>?): TableCell<DatabaseMeta, String> {
            return object : TableCell<DatabaseMeta, String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        text = null
                        graphic = null
                    } else {
                        val databaseMeta = tableView.items[index]
                        val dbFile = databaseMeta.file!!
                        text = if (dbFile.exists() && dbFile.isDirectory.not()) {
                            FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(dbFile))
                        } else {
                            "-"
                        }
                    }
                }
            }
        }
    }

    private class FileOpenerColumn : TableColumn<DatabaseMeta, String>(i18n("file.open_in_explorer")), Callback<TableColumn<DatabaseMeta, String>, TableCell<DatabaseMeta, String>> {
        init {
            isReorderable = false
            isSortable = false
            minWidth = 90.0
            cellFactory = this
        }

        override fun call(tableColumn: TableColumn<DatabaseMeta, String>?): TableCell<DatabaseMeta, String> {
            return object : TableCell<DatabaseMeta, String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        text = null
                        graphic = null
                    } else {
                        graphic = Button().also {
                            it.contentDisplay = ContentDisplay.GRAPHIC_ONLY
                            it.graphic = icon("folder-open-icon")
                            it.prefWidthProperty().bind(getTableColumn().widthProperty())
                            it.disableProperty().bind(tableRow.selectedProperty().not())
                            it.setOnAction {
                                tableView.selectionModel.selectedItems
                                    .stream()
                                    .map(DatabaseMeta::file)
                                    .forEach(FileExplorers.getLazy()::openSelect)
                            }
                        }
                    }
                }
            }
        }
    }

    private inner class DeleteColumn : TableColumn<DatabaseMeta, String>(i18n("database.manager.table.column.delete")), Callback<TableColumn<DatabaseMeta, String>, TableCell<DatabaseMeta, String>> {
        init {
            isReorderable = false
            isSortable = false
            minWidth = 90.0
            cellFactory = this
        }

        override fun call(tableColumn: TableColumn<DatabaseMeta, String>?): TableCell<DatabaseMeta, String> {
            return object : TableCell<DatabaseMeta, String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        text = null
                        graphic = null
                    } else {
                        graphic = Button().also {
                            it.contentDisplay = ContentDisplay.GRAPHIC_ONLY
                            it.graphic = icon("database-minus-icon")
                            it.prefWidthProperty().bind(getTableColumn().widthProperty())
                            it.disableProperty().bind(tableRow.selectedProperty().not())
                            it.setOnAction {
                                val items = tableView.selectionModel.selectedItems.copy()
                                DBDeleteDialog().show(items)
                            }
                        }
                    }
                }
            }
        }
    }

    private inner class DBDeleteDialog {
        fun show(items: ObservableList<DatabaseMeta>) {
            context.showDialog(
                i18n("database.manager.confirm_delete.title", items.size),
                ListView(items),
                {
                    if (I18NButtonType.YES.typeEquals(it)) {
                        items.forEach(databaseTracker::removeDatabase)
                    }
                },
                I18NButtonType.YES,
                I18NButtonType.NO
            )
        }
    }
}
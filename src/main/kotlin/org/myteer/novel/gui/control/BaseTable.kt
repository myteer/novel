package org.myteer.novel.gui.control

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.ContextMenu
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.MouseButton
import org.myteer.novel.i18n.i18n
import java.util.function.Consumer

abstract class BaseTable<S> : TableView<S>() {
    private val onItemDoubleClicked: ObjectProperty<Consumer<S>> = SimpleObjectProperty()
    private val onItemSecondaryDoubleClicked: ObjectProperty<Consumer<S>> = SimpleObjectProperty()
    private val rowContextMenu: ObjectProperty<ContextMenu> = SimpleObjectProperty()
    private val sortingComparator: ObjectProperty<Comparator<String>> = SimpleObjectProperty()

    init {
        buildClickHandlingPolicy()
    }

    private fun buildClickHandlingPolicy() {
        setRowFactory { buildTableRow() }
    }

    private fun buildTableRow() = TableRow<S>().also { row ->
        row.setOnMouseClicked { event ->
            if (!row.isEmpty && 2 == event.clickCount) {
                if (MouseButton.PRIMARY == event.button) {
                    onItemDoubleClicked.get()?.accept(row.item)
                } else if (MouseButton.SECONDARY == event.button) {
                    onItemSecondaryDoubleClicked.get()?.accept(row.item)
                }
            }
        }
        row.contextMenuProperty().bind(rowContextMenu)
        row.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED) {
            if (row.isEmpty) it.consume()
        }
    }

    fun isColumnShown(columnType: ColumnType): Boolean {
        return columns.map { it as Column }.map { it.columnType }.any { it == columnType }
    }

    fun getShowingColumnTypes(): List<ColumnType> {
        return columns.map { it as Column }.map { it.columnType }
    }

    @Suppress("UNCHECKED_CAST")
    fun addColumnType(columnType: ColumnType) {
        val column = columnType.columnFactory.invoke(columnType, this)
        columns.add(column as TableColumn<S, *>)
    }

    fun addColumnTypes(vararg columnTypes: ColumnType) {
        columnTypes.forEach(this::addColumnType)
    }

    fun removeColumnType(columnType: ColumnType) {
        columns.removeIf {
            (it as Column<S, *>).columnType == columnType
        }
    }

    fun removeAllColumns() = columns.clear()

    fun getSortingComparator(): Comparator<String> = sortingComparator.get()

    fun sortingComparatorProperty(): ObjectProperty<Comparator<String>> = sortingComparator

    fun setSortingComparator(comparator: Comparator<String>) {
        sortingComparator.set(comparator)
    }

    fun getRowContextMenu(): ContextMenu = rowContextMenu.get()

    fun rowContextMenuProperty(): ObjectProperty<ContextMenu> = rowContextMenu

    fun setRowContextMenu(contextMenu: ContextMenu) {
        rowContextMenu.set(contextMenu)
    }

    fun getOnItemSecondaryDoubleClicked(): Consumer<S> = onItemSecondaryDoubleClicked.get()

    fun onItemSecondaryDoubleClickedProperty(): ObjectProperty<Consumer<S>> = onItemSecondaryDoubleClicked

    fun setOnItemSecondaryDoubleClicked(consumer: Consumer<S>) {
        onItemSecondaryDoubleClicked.set(consumer)
    }

    fun getOnItemDoubleClicked(): Consumer<S> = onItemDoubleClicked.get()

    fun onItemDoubleClickedProperty(): ObjectProperty<Consumer<S>> = onItemDoubleClicked

    fun setOnItemDoubleClicked(consumer: Consumer<S>) {
        onItemDoubleClicked.set(consumer)
    }

    class ColumnType(
        val id: String,
        val text: String,
        val columnFactory: (ColumnType, BaseTable<*>) -> Column<*, *>,
        vararg options: Option
    ) {
        companion object {
            val DEFAULT_VISIBLE = Option()
            val TEXT_GUI_VISIBLE = Option()
        }

        private val options: List<Option> = options.toList()

        fun isDefaultVisible(): Boolean = options.contains(DEFAULT_VISIBLE)

        fun isTextOnUIVisible(): Boolean = options.contains(TEXT_GUI_VISIBLE)

        override fun toString(): String = i18n(text)

        class Option
    }

    open class Column<S, T>(val columnType: ColumnType) : TableColumn<S, T>() {
        init {
            isSortable = false
            initToColumnType(columnType)
        }

        private fun initToColumnType(columnType: ColumnType) {
            text = getTextFor(columnType)
        }

        private fun getTextFor(columnType: ColumnType): String? {
            return if (columnType.isTextOnUIVisible()) columnType.text else null
        }
    }

    abstract class SortableColumn<S>(columnType: ColumnType) : Column<S, String>(columnType) {
        init {
            isSortable = true
            bindToTableSortingComparator()
        }

        private fun bindToTableSortingComparator() {
            tableViewProperty().addListener(object : ChangeListener<TableView<S>> {
                override fun changed(
                    observable: ObservableValue<out TableView<S>>?,
                    oldValue: TableView<S>?,
                    table: TableView<S>?
                ) {
                    when (table) {
                        is BaseTable<S> -> {
                            comparatorProperty().bind(table.sortingComparator)
                            observable?.removeListener(this)
                        }
                    }
                }
            })
        }
    }
}
package org.myteer.novel.gui.bookmanager

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import javafx.beans.binding.Bindings
import javafx.beans.binding.IntegerBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.scene.layout.BorderPane
import org.myteer.novel.config.ConfigAdapter
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.crawl.task.BookQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Book
import org.myteer.novel.db.repository.BookRepository
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.control.BaseTable
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.utils.addKeyBindingDetection
import org.myteer.novel.gui.utils.runOutsideUI
import org.myteer.novel.gui.utils.selectedItems
import org.myteer.novel.i18n.I18N
import org.myteer.novel.i18n.i18n
import org.slf4j.LoggerFactory
import java.lang.reflect.Type
import java.text.Collator
import java.util.*

class BookManagerView(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase
) : BorderPane() {
    private val baseItems: ObservableList<Book> = FXCollections.observableArrayList()
    private val bookManagerViewBase = BookManagerViewBase(preferences, baseItems)
    private val toolBar = BookManagerToolBar(context, this)

    val table: BookTable
        get() = bookManagerViewBase.table

    var isFindDialogVisible: Boolean
        get() = bookManagerViewBase.isFindDialogVisible
        set(value) {
            bookManagerViewBase.isFindDialogVisible = value
        }

    var sort: Locale = I18N.defaultLocale()
        set(value) {
            field = value
            configureSort(value)
        }

    var columnsInfo: TableColumnsInfo
        get() = TableColumnsInfo(table.getShowingColumnTypes())
        set(value) {
            table.columns.clear()
            value.columnTypes.forEach(table::addColumnType)
        }

    init {
        buildUI()
        init()
    }

    private fun buildUI() {
        top = toolBar
        center = bookManagerViewBase
    }

    private fun init() {
        initKeyDetections()
        buildTableRowContextMenu()
        readConfigurations()
        loadRecords()
    }

    private fun initKeyDetections() {
        addKeyBindingDetection(KeyBindings.findRecord) { isFindDialogVisible = true }
    }

    private fun buildTableRowContextMenu() {
        BookContextMenu(this).applyOn(table)
    }

    private fun readConfigurations() {
        columnsInfo = preferences.get(COL_CONFIG_KEY)
        sort = preferences.get(SORT_CONFIG_KEY)
    }

    private fun loadRecords() {
        runOutsideUI(BooksLoadTask(context, table, database))
    }

    private fun configureSort(locale: Locale) {
        @Suppress("UNCHECKED_CAST")
        table.setSortingComparator((I18N.getCollator(locale) ?: Collator.getInstance()) as Comparator<String>)
        runOutsideUI {
            preferences.editor().put(SORT_CONFIG_KEY, locale)
        }
    }

    fun refresh() {
        loadRecords()
        isFindDialogVisible = false
    }

    fun selectedItemsCountProperty(): IntegerBinding = Bindings.size(table.selectedItems)

    fun itemsCountProperty(): IntegerBinding = Bindings.size(baseItems)

    fun removeSelectedItems() {
        removeItems(ArrayList(table.selectedItems))
    }

    private fun removeItems(items: List<Book>) {
        runOutsideUI(object : Task<Unit>() {
            init {
                setOnRunning { context.showIndeterminateProgress() }
                setOnFailed { context.stopProgress() }
                setOnSucceeded {
                    context.stopProgress()
                    baseItems.removeAll(items)
                }
            }

            override fun call() {
                logger.debug("Performing remove action...")
                val ids = items.mapNotNull { it.id }.toTypedArray()
                BookRepository(database).deleteById(*ids)
            }
        })
    }

    fun clearCache() {
        table.clearCache()
    }

    fun importBook(bookId: String) {
        baseItems.firstOrNull { bookId == it.id }?.let {
            table.selectionModel.clearSelection()
            table.selectionModel.select(it)
            table.scrollTo(it)
            return
        }
        runOutsideUI(object : BookQueryTask(bookId) {
            init {
                setOnRunning { context.showIndeterminateProgress() }
                setOnFailed {
                    context.stopProgress()
                    context.showErrorDialog(
                        i18n("book.import.failed.title"),
                        i18n("book.import.failed.message"),
                        it.source.exception as Exception?
                    )
                }
                setOnSucceeded {
                    context.stopProgress()
                    value?.let {
                        val book = it.toLocalBook()
                        BookRepository(database).insert(book)
                        baseItems.add(book)
                        table.selectionModel.clearSelection()
                        table.selectionModel.select(book)
                        table.scrollTo(book)
                    }
                }
            }
        })
    }

    private class BooksLoadTask(
        context: Context,
        table: BookTable,
        private val database: NitriteDatabase
    ) : Task<List<Book>>() {
        init {
            setOnRunning {
                context.showIndeterminateProgress()
            }
            setOnFailed {
                context.stopProgress()
                logger.error("Failed to load books", it.source.exception)
                context.showErrorDialog(
                    i18n("book.load.failed.title"),
                    i18n("book.load.failed.message"),
                    it.source.exception as? Exception
                )
            }
            setOnSucceeded {
                context.stopProgress()
                table.items.setAll(value)
                table.refresh()
            }
        }

        override fun call(): List<Book> {
            return BookRepository(database).selectAll()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BookManagerView::class.java)
        val COL_CONFIG_KEY = PreferenceKey(
            "book.manager.view.table.columns",
            TableColumnsInfo::class.java,
            TableColumnsInfo.Companion::byDefault,
            ColumnsInfoAdapter()
        )
        val SORT_CONFIG_KEY = PreferenceKey(
            "book.manager.view.table.sort",
            Locale::class.java,
            Locale::getDefault
        )
    }

    private class ColumnsInfoAdapter : ConfigAdapter<TableColumnsInfo> {
        override fun serialize(
            src: TableColumnsInfo,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement = JsonArray().also { jsonArray ->
            src.columnTypes.map { it.id }.distinct().forEach(jsonArray::add)
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): TableColumnsInfo = TableColumnsInfo(
            json.asJsonArray.mapNotNull { BookTable.columnById(it.asString) }
        )
    }

    class TableColumnsInfo(val columnTypes: List<BaseTable.ColumnType>) {
        companion object {
            fun byDefault() = TableColumnsInfo(
                BookTable.columnList().filter(BaseTable.ColumnType::isDefaultVisible)
            )
        }
    }
}
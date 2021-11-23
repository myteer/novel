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
import javafx.concurrent.WorkerStateEvent
import javafx.event.Event
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import org.myteer.novel.config.ConfigAdapter
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.crawl.task.BookQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Book
import org.myteer.novel.db.repository.BookRepository
import org.myteer.novel.db.repository.ChapterRepository
import org.myteer.novel.export.api.BookExportConfiguration
import org.myteer.novel.export.api.BookExporter
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.bookmanager.task.ChapterListRefreshTask
import org.myteer.novel.gui.control.BaseTable
import org.myteer.novel.gui.keybinding.KeyBindings
import org.myteer.novel.gui.utils.*
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
    private val bookManagerViewBase = BookManagerViewBase(context, preferences, database, baseItems)
    private val toolBar = BookManagerToolBar(context, this)

    val table: BookTable
        get() = bookManagerViewBase.table

    var isFindDialogVisible: Boolean
        get() = bookManagerViewBase.isFindDialogVisible
        set(value) {
            bookManagerViewBase.isFindDialogVisible = value
        }

    var sortLocale: Locale = I18N.defaultLocale()
        set(value) {
            field = value
            configureSortLocal(value)
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
        sortLocale = preferences.get(SORT_CONFIG_KEY)
    }

    private fun loadRecords() {
        val task = BooksLoadTask(context, table, database).apply {
            addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED) {
                synchronized(baseItems) {
                    baseItems.setAll(value)
                }
            }
        }
        runOutsideUI(task)
    }

    private fun configureSortLocal(locale: Locale) {
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
                ChapterRepository(database).deleteByBookId(*ids)
            }
        })
    }

    fun clearCache() {
        table.clearCache()
    }

    fun <C : BookExportConfiguration> exportSelected(exporter: BookExporter<C>) {
        export(exporter, table.selectedItems.map(Book::copy))
    }

    private fun <C : BookExportConfiguration> export(exporter: BookExporter<C>, items: List<Book>) {
        exporter.configurationDialog.show(context) { config ->
            val fileChooser = FileChooser().apply {
                extensionFilters.add(
                    FileChooser.ExtensionFilter(
                        exporter.contentTypeDescription,
                        "*.${exporter.contentType}"
                    )
                )
            }
            fileChooser.showSaveDialog(context.getContextWindow())?.let { file ->
                val task = exporter.task(items, file.outputStream(), config).apply {
                    setOnRunning { context.showIndeterminateProgress() }
                    setOnFailed {
                        context.stopProgress()
                        logger.error("Couldn't export books to '{}'", exporter.contentType, it.source.exception)
                        context.showErrorDialog(
                            i18n("book.export.error.title"),
                            i18n("book.export.error.message", items.size, exporter.contentType),
                            it.source.exception as Exception?
                        )
                    }
                    setOnSucceeded {
                        context.stopProgress()
                        context.showInformationNotification(
                            i18n("book.export.successful.title"),
                            i18n("book.export.successful.message", items.size, exporter.contentType, file.name),
                            null,
                            Event::consume,
                            hyperlink(i18n("file.open_in_app")) { file.open() },
                            hyperlink(i18n("file.open_in_explorer")) { file.revealInExplorer() }
                        )
                    }
                }
                runOutsideUI(task)
            }
        }
    }

    fun importBook(bookId: String) {
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
                    synchronized(baseItems) {
                        baseItems.firstOrNull { bookId == it.id }?.let {
                            table.selectionModel.clearSelection()
                            table.selectionModel.select(it)
                            table.scrollTo(it)
                            return@synchronized
                        }
                        value?.let {
                            val book = it.toLocalBook()
                            BookRepository(database).save(book)
                            runOutsideUI(ChapterListRefreshTask(database, bookId))
                            baseItems.add(book)
                            table.selectionModel.clearSelection()
                            table.selectionModel.select(book)
                            table.scrollTo(book)
                        }
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
            "book.manager.view.table.sortLocale",
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
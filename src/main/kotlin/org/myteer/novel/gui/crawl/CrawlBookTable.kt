package org.myteer.novel.gui.crawl

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import javafx.scene.Node
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableCell
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.gui.control.BaseTable
import org.myteer.novel.gui.control.BaseTable.ColumnType.Companion.DEFAULT_VISIBLE
import org.myteer.novel.gui.control.BaseTable.ColumnType.Companion.TITLE_VISIBLE
import org.myteer.novel.gui.control.TableViewPlaceHolder
import org.myteer.novel.gui.utils.asyncLoadImage
import org.myteer.novel.gui.utils.constantObservable
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.styleClass
import org.myteer.novel.i18n.i18n
import java.time.Duration

class CrawlBookTable : BaseTable<Book>() {
    companion object {
        private val INDEX_COLUMN: ColumnType = ColumnType(
            "index",
            i18n("crawl.book.table.column.index"),
            { _, _ -> IndexColumn() },
            DEFAULT_VISIBLE
        )
        private val THUMBNAIL_COLUMN: ColumnType = ColumnType(
            "thumbnail",
            i18n("crawl.book.table.column.thumbnail"),
            { _, table -> (table as CrawlBookTable).ThumbnailColumn() },
            DEFAULT_VISIBLE,
            TITLE_VISIBLE
        )
        private val NAME_COLUMN: ColumnType = ColumnType(
            "name",
            i18n("crawl.book.table.column.name"),
            { _, _ -> NameColumn() },
            DEFAULT_VISIBLE,
            TITLE_VISIBLE
        )
        private val AUTHOR_COLUMN: ColumnType = ColumnType(
            "author",
            i18n("crawl.book.table.column.author"),
            { _, _ -> AuthorColumn() },
            DEFAULT_VISIBLE,
            TITLE_VISIBLE
        )
        private val CATEGORY_NAME_COLUMN: ColumnType = ColumnType(
            "category_name",
            i18n("crawl.book.table.column.category"),
            { _, _ -> CategoryNameColumn() },
            DEFAULT_VISIBLE,
            TITLE_VISIBLE
        )
        private val SCORE_COLUMN: ColumnType = ColumnType(
            "score",
            i18n("crawl.book.table.column.score"),
            { _, _ -> ScoreColumn() },
            TITLE_VISIBLE
        )
        private val STATUS_COLUMN: ColumnType = ColumnType(
            "status",
            i18n("crawl.book.table.column.status"),
            { _, _ -> StatusColumn() },
            DEFAULT_VISIBLE,
            TITLE_VISIBLE
        )
        private val LAST_CHAPTER_NAME_COLUMN: ColumnType = ColumnType(
            "last_chapter",
            i18n("crawl.book.table.column.last_chapter"),
            { _, _ -> LastChapterNameColumn() },
            DEFAULT_VISIBLE,
            TITLE_VISIBLE
        )
        private val LAST_UPDATE_TIME_COLUMN: ColumnType = ColumnType(
            "last_update",
            i18n("crawl.book.table.column.last_update"),
            { _, _ -> LastUpdateTimeColumn() },
            DEFAULT_VISIBLE,
            TITLE_VISIBLE
        )
        private val DESCRIPTION_COLUMN: ColumnType = ColumnType(
            "description",
            i18n("crawl.book.table.column.description"),
            { _, _ -> DescriptionColumn() },
            TITLE_VISIBLE
        )
        private val COLUMN_LIST: List<ColumnType> = listOf(
            INDEX_COLUMN,
            THUMBNAIL_COLUMN,
            NAME_COLUMN,
            AUTHOR_COLUMN,
            CATEGORY_NAME_COLUMN,
            SCORE_COLUMN,
            STATUS_COLUMN,
            LAST_CHAPTER_NAME_COLUMN,
            LAST_UPDATE_TIME_COLUMN,
            DESCRIPTION_COLUMN
        )

        fun columnList(): List<ColumnType> = COLUMN_LIST

        fun columnById(id: String): ColumnType? {
            return columnList().find { id == it.id }
        }
    }

    private val imageCache: Cache<String, Image> = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterAccess(Duration.ofSeconds(180))
        .softValues()
        .build()

    init {
        styleClass.add("crawl-book-table")
        selectionModel.selectionMode = SelectionMode.SINGLE
        columnResizePolicy = CONSTRAINED_RESIZE_POLICY
        placeholder = buildPlaceHolder()
    }

    private fun buildPlaceHolder(): Node = TableViewPlaceHolder(
        this,
        { i18n("crawl.book.table.place.holder") },
        { i18n("crawl.book.table.place.holder.no.col") }
    )

    fun clearCache() {
        imageCache.invalidateAll()
    }

    fun buildDefaultColumns() {
        columns.clear()
        columnList().filter(ColumnType::isDefaultVisible).forEach(this::addColumnType)
    }

    private class IndexColumn : Column<Book, Int>(INDEX_COLUMN) {
        init {
            minWidth = 60.0
            maxWidth = 60.0
            setCellValueFactory { cellData ->
                return@setCellValueFactory constantObservable {
                    1 + cellData.tableView.items.indexOf(cellData.value)
                }
            }
        }
    }

    private inner class ThumbnailColumn : Column<Book, String>(THUMBNAIL_COLUMN) {
        private val prefWidthValue = 120.0
        private val prefHeightValue = 150.0

        init {
            minWidth = 20.0
            prefWidth = prefWidthValue
            setCellFactory {
                return@setCellFactory object : TableCell<Book, String>() {
                    override fun updateItem(item: String?, empty: Boolean) {
                        super.updateItem(item, empty)
                        if (empty) {
                            text = null
                            graphic = null
                            prefHeight = USE_COMPUTED_SIZE
                        } else {
                            prefHeight = prefHeightValue + 20
                            val book = tableView.items[index]
                            if (null == book.thumbnail) {
                                text = i18n("crawl.book.table.thumbnail.not.available")
                                graphic = null
                            } else {
                                text = null
                                graphic = icon("image-icon").styleClass("thumbnail-place-holder")
                                val image = imageCache.getIfPresent(book.thumbnail)
                                if (null != image) {
                                    graphic = ImageView(image).apply {
                                        fitWidth = prefWidthValue
                                        fitHeight = prefHeightValue
                                        isPreserveRatio = true
                                    }
                                } else {
                                    asyncLoadImage(book.thumbnail!!) {
                                        imageCache.put(book.thumbnail, it)
                                        if (book == getCurrentBook()) {
                                            graphic = ImageView(it).apply {
                                                fitWidth = prefWidthValue
                                                fitHeight = prefHeightValue
                                                isPreserveRatio = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    private fun getCurrentBook(): Book? {
                        return try {
                            tableView.items[index]
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            }
        }
    }

    private class NameColumn : SimpleBookColumn(NAME_COLUMN) {
        override fun getValue(book: Book): Any? = book.name
    }

    private class AuthorColumn : SimpleBookColumn(AUTHOR_COLUMN) {
        override fun getValue(book: Book): Any? = book.author
    }

    private class CategoryNameColumn : SimpleBookColumn(CATEGORY_NAME_COLUMN) {
        override fun getValue(book: Book): Any? = book.categoryName
    }

    private class ScoreColumn : SimpleBookColumn(SCORE_COLUMN) {
        override fun getValue(book: Book): Any? = book.score
    }

    private class StatusColumn : SimpleBookColumn(STATUS_COLUMN) {
        override fun getValue(book: Book): Any? = book.status
    }

    private class LastChapterNameColumn : SimpleBookColumn(LAST_CHAPTER_NAME_COLUMN) {
        override fun getValue(book: Book): Any? = book.lastChapterName
    }

    private class LastUpdateTimeColumn : SimpleBookColumn(LAST_UPDATE_TIME_COLUMN) {
        override fun getValue(book: Book): Any? = book.lastUpdateTime?.removeSuffix(" 00:00:00")
    }

    private class DescriptionColumn : SimpleBookColumn(DESCRIPTION_COLUMN) {
        override fun getValue(book: Book): Any? = book.description
    }

    private abstract class SimpleBookColumn(columnType: ColumnType) : SortableColumn<Book>(columnType) {
        init {
            setCellValueFactory { cellData ->
                return@setCellValueFactory constantObservable {
                    getValue(cellData.value)?.let(Any::toString) ?: "-"
                }
            }
        }

        protected abstract fun getValue(book: Book): Any?
    }
}
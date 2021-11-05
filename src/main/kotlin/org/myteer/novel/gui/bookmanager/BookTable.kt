package org.myteer.novel.gui.bookmanager

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import javafx.scene.control.Label
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableCell
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import org.myteer.novel.db.data.Book
import org.myteer.novel.db.data.BookProperty
import org.myteer.novel.gui.control.BaseTable
import org.myteer.novel.gui.control.TableViewPlaceHolder
import org.myteer.novel.gui.utils.asyncLoadImage
import org.myteer.novel.gui.utils.constantObservable
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.gui.utils.styleClass
import org.myteer.novel.i18n.i18n
import java.time.Duration

class BookTable : BaseTable<Book>() {
    companion object {
        private val INDEX_COLUMN: ColumnType = ColumnType(
            "index",
            i18n("book.table.column.index"),
            { _, _ -> IndexColumn() },
            ColumnType.DEFAULT_VISIBLE
        )
        private val THUMBNAIL_COLUMN: ColumnType = buildColumnType(
            BookProperty.THUMBNAIL,
            { _, table -> (table as BookTable).ThumbnailColumn() },
            ColumnType.TITLE_VISIBLE
        )
        private val ID_COLUMN: ColumnType = buildColumnType(
            BookProperty.ID,
            ColumnType.TITLE_VISIBLE
        )
        private val NAME_COLUMN: ColumnType = buildColumnType(
            BookProperty.NAME,
            ColumnType.DEFAULT_VISIBLE,
            ColumnType.TITLE_VISIBLE
        )
        private val AUTHOR_COLUMN: ColumnType = buildColumnType(
            BookProperty.AUTHOR,
            ColumnType.DEFAULT_VISIBLE,
            ColumnType.TITLE_VISIBLE
        )
        private val CATEGORY_NAME_COLUMN: ColumnType = buildColumnType(
            BookProperty.CATEGORY_NAME,
            ColumnType.DEFAULT_VISIBLE,
            ColumnType.TITLE_VISIBLE
        )
        private val SCORE_COLUMN: ColumnType = buildColumnType(
            BookProperty.SCORE,
            ColumnType.DEFAULT_VISIBLE,
            ColumnType.TITLE_VISIBLE
        )
        private val STATUS_COLUMN: ColumnType = buildColumnType(
            BookProperty.STATUS,
            ColumnType.DEFAULT_VISIBLE,
            ColumnType.TITLE_VISIBLE
        )
        private val LAST_CHAPTER_ID_COLUMN: ColumnType = buildColumnType(
            BookProperty.LAST_CHAPTER_ID,
            ColumnType.TITLE_VISIBLE
        )
        private val LAST_CHAPTER_NAME_COLUMN: ColumnType = buildColumnType(
            BookProperty.LAST_CHAPTER_NAME,
            ColumnType.DEFAULT_VISIBLE,
            ColumnType.TITLE_VISIBLE
        )
        private val LAST_UPDATE_TIME_COLUMN: ColumnType = buildColumnType(
            BookProperty.LAST_UPDATE_TIME,
            ColumnType.DEFAULT_VISIBLE,
            ColumnType.TITLE_VISIBLE
        )
        private val DESCRIPTION_COLUMN: ColumnType = buildColumnType(
            BookProperty.DESCRIPTION,
            ColumnType.TITLE_VISIBLE
        )
        private val COLUMN_LIST: List<ColumnType> = listOf(
            INDEX_COLUMN,
            THUMBNAIL_COLUMN,
            ID_COLUMN,
            NAME_COLUMN,
            AUTHOR_COLUMN,
            CATEGORY_NAME_COLUMN,
            SCORE_COLUMN,
            STATUS_COLUMN,
            LAST_CHAPTER_ID_COLUMN,
            LAST_CHAPTER_NAME_COLUMN,
            LAST_UPDATE_TIME_COLUMN,
            DESCRIPTION_COLUMN
        )

        private fun buildColumnType(
            property: BookProperty<*>,
            vararg options: ColumnType.Option
        ) = buildColumnType(
            property,
            { col, _ -> SimplePropertyColumn(col, property) },
            *options
        )

        private fun buildColumnType(
            property: BookProperty<*>,
            columnFactory: (ColumnType, BaseTable<*>) -> Column<*, *>,
            vararg options: ColumnType.Option
        ) = ColumnType(property.id, property.name, columnFactory, *options)

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
        styleClass.add("book-table")
        selectionModel.selectionMode = SelectionMode.MULTIPLE
        columnResizePolicy = CONSTRAINED_RESIZE_POLICY
        placeholder = PlaceHolder(this)
    }

    fun clearCache() {
        imageCache.invalidateAll()
    }

    private class PlaceHolder(table: BookTable) : TableViewPlaceHolder(table) {
        init {
            minHeight = 0.0
        }

        override fun contentIfEmpty() = VBox(5.0).apply {
            styleClass.add("book-table-place-holder")
            children.addAll(
                StackPane(ImageView()),
                StackPane(Label(i18n("book.table.place.holder")))
            )
        }

        override fun contentIfNoColumns() = Label(i18n("book.table.place.holder.no.col"))
    }

    private class IndexColumn : Column<Book, Int>(INDEX_COLUMN) {
        init {
            minWidth = 60.0
            maxWidth = 60.0
            isReorderable = false
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
            isReorderable = false
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
                                text = i18n("book.table.thumbnail.not.available")
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

    private class SimplePropertyColumn(columnType: ColumnType, property: BookProperty<*>) : SortableColumn<Book>(columnType) {
        init {
            setCellValueFactory { cellData ->
                return@setCellValueFactory constantObservable {
                    property.getValue(cellData.value)?.let(Any::toString) ?: "-"
                }
            }
        }
    }
}
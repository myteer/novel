package org.myteer.novel.gui.book

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import com.github.benmanes.caffeine.cache.RemovalListener
import javafx.beans.binding.Bindings
import javafx.beans.binding.IntegerBinding
import javafx.scene.CacheHint
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.util.Callback
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.utils.asyncLoadImage
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n
import java.time.Duration

class CrawlBookTable(
    private val context: Context
) : TableView<Book>() {
    private val itemsCount: IntegerBinding
    private val selectedItemsCount: IntegerBinding
    private val imageCache: Cache<String, Image> = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterAccess(Duration.ofSeconds(60))
        .softValues()
        .build()

    init {
        itemsCount = Bindings.size(items)
        selectedItemsCount = Bindings.size(selectionModel.selectedItems)
        placeholder = Label(i18n("book.search.table.place.holder"))
        selectionModel.selectionMode = SelectionMode.MULTIPLE
        columns.addAll(
            ThumbnailColumn(),
            NameColumn(),
            AuthorColumn(),
            CategoryNameColumn(),
            ScoreColumn(),
            StatusColumn(),
            LastChapterNameColumn(),
            LastUpdateTimeColumn(),
        )
        isCache = true
        cacheHint = CacheHint.SCALE_AND_ROTATE
    }

    fun clearCache() {
        imageCache.invalidateAll()
    }

    fun itemsCount(): IntegerBinding = itemsCount

    fun selectedItemsCount(): IntegerBinding = selectedItemsCount

    private inner class ThumbnailColumn : TableColumn<Book, String>(i18n("book.search.table.column.thumbnail")), Callback<TableColumn<Book, String>, TableCell<Book, String>> {
        private val PREF_WIDTH = 120.0
        private val PREF_HEIGHT = 150.0

        init {
            isReorderable = false
            isSortable = false
            cellFactory = this
            prefWidth = PREF_WIDTH + 5
        }

        override fun call(tableColumn: TableColumn<Book, String>?): TableCell<Book, String> {
            return object : TableCell<Book, String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        graphic = null
                        text = null
                        tooltip = null
                        prefHeight = PREF_HEIGHT
                    } else {
                        prefHeight = PREF_HEIGHT
                        graphic = icon("image-icon")
                        val book = tableView.items[index]
                        book.thumbnail?.let { url ->
                            val image = imageCache.getIfPresent(url)
                            if (null != image) {
                                graphic = ImageView(image).apply {
                                    fitWidth = PREF_WIDTH
                                    fitHeight = PREF_HEIGHT
                                    isPreserveRatio = true
                                }
                            } else {
                                asyncLoadImage(url) {
                                    imageCache.put(url, it)
                                    graphic = ImageView(it).apply {
                                        fitWidth = PREF_WIDTH
                                        fitHeight = PREF_HEIGHT
                                        isPreserveRatio = true
                                    }
                                }
                            }
                        }
                        text = null
                        tooltip = Tooltip(book.name)
                    }
                }
            }
        }
    }

    private class NameColumn : TableColumn<Book, String>(i18n("book.search.table.column.name")) {
        init {
            isReorderable = false
            isSortable = false
            cellValueFactory = PropertyValueFactory("name")
        }
    }

    private class AuthorColumn : TableColumn<Book, String>(i18n("book.search.table.column.author")) {
        init {
            isReorderable = false
            isSortable = false
            cellValueFactory = PropertyValueFactory("author")
        }
    }

    private class CategoryNameColumn : TableColumn<Book, String>(i18n("book.search.table.column.category")) {
        init {
            isReorderable = false
            isSortable = false
            cellValueFactory = PropertyValueFactory("categoryName")
        }
    }

    private class ScoreColumn : TableColumn<Book, String>(i18n("book.search.table.column.score")) {
        init {
            isReorderable = false
            isSortable = false
            cellValueFactory = PropertyValueFactory("score")
        }
    }

    private class StatusColumn : TableColumn<Book, String>(i18n("book.search.table.column.status")) {
        init {
            isReorderable = false
            isSortable = false
            cellValueFactory = PropertyValueFactory("status")
        }
    }

    private class LastChapterNameColumn : TableColumn<Book, String>(i18n("book.search.table.column.last_chapter")) {
        init {
            isReorderable = false
            isSortable = false
            cellValueFactory = PropertyValueFactory("lastChapterName")
        }
    }

    private class LastUpdateTimeColumn : TableColumn<Book, String>(i18n("book.search.table.column.last_update")) {
        init {
            isReorderable = false
            isSortable = false
            cellValueFactory = PropertyValueFactory("lastUpdateTime")
        }
    }
}
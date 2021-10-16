package org.myteer.novel.gui.crawl

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import javafx.beans.value.ObservableValueBase
import javafx.scene.control.TableCell
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.gui.control.BaseTable
import org.myteer.novel.gui.control.BaseTable.ColumnType.Companion.DEFAULT_VISIBLE
import org.myteer.novel.gui.control.BaseTable.ColumnType.Companion.TITLE_VISIBLE
import org.myteer.novel.gui.utils.asyncLoadImage
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
    }

    private val imageCache: Cache<String, Image> = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterAccess(Duration.ofSeconds(180))
        .softValues()
        .build()

    private class IndexColumn : Column<Book, Int>(INDEX_COLUMN) {
        init {
            minWidth = 60.0
            maxWidth = 60.0
            setCellValueFactory { cellData ->
                return@setCellValueFactory object : ObservableValueBase<Int>() {
                    override fun getValue(): Int {
                        return 1 + cellData.tableView.items.indexOf(cellData.value)
                    }
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
                            prefHeight = prefHeightValue
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
}
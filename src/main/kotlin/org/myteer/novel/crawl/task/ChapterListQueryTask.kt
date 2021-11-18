package org.myteer.novel.crawl.task

import com.fasterxml.jackson.core.type.TypeReference
import javafx.concurrent.Task
import org.myteer.novel.crawl.model.Chapter
import org.myteer.novel.crawl.vo.ResultVO
import org.myteer.novel.utils.HttpUtils
import org.myteer.novel.utils.JsonUtils
import org.myteer.novel.utils.executeWithRetryStrategy
import org.slf4j.LoggerFactory

open class ChapterListQueryTask(private val bookId: String) : Task<List<Chapter>>() {
    companion object {
        private val logger = LoggerFactory.getLogger(ChapterListQueryTask::class.java)
        private const val baseUrl = "https://infosxs.pysmei.com/book/%s/"
    }

    override fun call(): List<Chapter> {
        logger.debug("chapter list query, bookId: {}", bookId)
        val requestUrl = baseUrl.format(bookId)
        return try {
            mutableListOf<Chapter>().also { list ->
                executeWithRetryStrategy { HttpUtils.get(requestUrl) }.takeIf { it.isNotBlank() }?.let { content ->
                    val value = content.replace(",]", "]")
                    JsonUtils.fromJsonForGeneric(value, object : TypeReference<ResultVO<Book>>() {}).data?.let { book ->
                        var orderNo = 0
                        book.list?.forEachIndexed { index, volume ->
                            volume.list?.forEach { chapter ->
                                Chapter().also { c ->
                                    c.bookId = book.id
                                    c.bookName = book.name
                                    c.volumeIndex = index
                                    c.volumeName = volume.name
                                    c.id = chapter.id
                                    c.name = chapter.name
                                    c.hasContent = chapter.hasContent
                                    c.orderNo = orderNo++
                                    list.add(c)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("chapter list query error", e)
            listOf()
        }
    }

    private class Book {
        var id: String? = null
        var name: String? = null
        val list: List<Volume>? = null
    }

    private class Volume {
        var name: String? = null
        var list: List<Chapter2>? = null
    }

    private class Chapter2 {
        var id: String? = null
        var name: String? = null
        var hasContent: Int? = null
    }
}
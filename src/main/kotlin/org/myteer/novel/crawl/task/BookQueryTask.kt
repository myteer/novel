package org.myteer.novel.crawl.task

import com.fasterxml.jackson.core.type.TypeReference
import javafx.concurrent.Task
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.crawl.vo.ResultVO
import org.myteer.novel.utils.HttpUtils
import org.myteer.novel.utils.JsonUtils
import org.myteer.novel.utils.executeWithRetryStrategy
import org.slf4j.LoggerFactory

open class BookQueryTask(private val bookId: String) : Task<Book?>() {
    companion object {
        private val logger = LoggerFactory.getLogger(BookQueryTask::class.java)
        private const val baseUrl = "https://infosxs.pysmei.com/info/%s.html"
    }

    override fun call(): Book? {
        logger.debug("book query, bookId: {}", bookId)
        val requestUrl = baseUrl.format(bookId)
        return try {
            executeWithRetryStrategy { HttpUtils.get(requestUrl) }.takeIf { it.isNotBlank() }?.let {
                JsonUtils.fromJsonForGeneric(it, object : TypeReference<ResultVO<Book>>() {}).data
            }
        } catch (e: Exception) {
            logger.error("book query error", e)
            null
        }
    }
}
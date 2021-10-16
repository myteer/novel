package org.myteer.novel.crawl.task

import com.fasterxml.jackson.core.type.TypeReference
import javafx.concurrent.Task
import org.myteer.novel.crawl.model.Book
import org.myteer.novel.crawl.vo.BookSearchRequest
import org.myteer.novel.crawl.vo.ResultVO
import org.myteer.novel.utils.HttpUtils
import org.myteer.novel.utils.JsonUtils
import org.myteer.novel.utils.executeWithRetryStrategy
import org.slf4j.LoggerFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

open class BookSearchTask(private val request: BookSearchRequest) : Task<List<Book>>() {
    companion object {
        private val logger = LoggerFactory.getLogger(BookSearchTask::class.java)
        private const val baseUrl = "https://souxs.leeyegy.com/search.aspx?key=%s&page=%s&siteid=app2"
    }

    override fun call(): List<Book> {
        logger.debug("book search: {}", JsonUtils.toJson(request))
        val requestUrl = baseUrl.format(URLEncoder.encode(request.keyword, StandardCharsets.UTF_8), request.page)
        return try {
            executeWithRetryStrategy { HttpUtils.get(requestUrl) }.takeIf { it.isNotBlank() }?.let {
                JsonUtils.fromJsonForGeneric(it, object : TypeReference<ResultVO<List<Book>>>() {}).data
            } ?: listOf()
        } catch (e: Exception) {
            logger.error("book search error", e)
            listOf()
        }
    }
}
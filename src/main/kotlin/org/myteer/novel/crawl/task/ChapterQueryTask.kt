/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.myteer.novel.crawl.task

import com.fasterxml.jackson.core.type.TypeReference
import javafx.concurrent.Task
import org.myteer.novel.crawl.model.Chapter
import org.myteer.novel.crawl.vo.ResultVO
import org.myteer.novel.utils.HttpUtils
import org.myteer.novel.utils.JsonUtils
import org.myteer.novel.utils.executeWithRetryStrategy
import org.slf4j.LoggerFactory

class ChapterQueryTask(
    private val bookId: String,
    private val chapterId: String
) : Task<Chapter?>() {
    companion object {
        private val logger = LoggerFactory.getLogger(ChapterQueryTask::class.java)
        private const val baseUrl = "https://infosxs.pysmei.com/book/%s/%s.html"
    }

    override fun call(): Chapter? {
        logger.debug("chapter query, bookId: {}, chapterId: {}", bookId, chapterId)
        val requestUrl = baseUrl.format(bookId, chapterId)
        return try {
            executeWithRetryStrategy { HttpUtils.get(requestUrl) }.takeIf { it.isNotBlank() }?.let {
                JsonUtils.fromJsonForGeneric(it, object : TypeReference<ResultVO<Chapter>>() {}).data
            }
        } catch (e: Exception) {
            logger.error("chapter query error", e)
            null
        }
    }
}
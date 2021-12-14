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
package org.myteer.novel.crawl.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.myteer.novel.db.data.ChapterContent

class Chapter {
    @JsonProperty("id")
    var bookId: String? = null

    @JsonProperty("name")
    var bookName: String? = null

    @JsonProperty("cid")
    var id: String? = null

    @JsonProperty("cname")
    var name: String? = null

    @JsonProperty("pid")
    var previousId: String? = null

    @JsonProperty("nid")
    var nextId: String? = null

    var hasContent: Int? = null

    var content: String? = null

    var volumeIndex: Int = 0

    var volumeName: String? = null

    var orderNo: Int = 0

    fun toLocalChapter() = org.myteer.novel.db.data.Chapter().also {
        it.bookId = bookId!!
        it.bookName = bookName
        it.id = id!!
        it.name = name
        it.previousId = previousId
        it.nextId = nextId
        it.hasContent = hasContent
        it.volumeIndex = volumeIndex
        it.volumeName = volumeName
        it.orderNo = orderNo
    }

    fun toLocalChapterContent() = ChapterContent(bookId!!, id!!, content)
}
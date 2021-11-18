package org.myteer.novel.crawl.model

import com.fasterxml.jackson.annotation.JsonProperty

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

    var volumeIndex: Int = 0

    var volumeName: String? = null

    var orderNo: Int = 0

    fun toLocalChapter() = org.myteer.novel.db.data.Chapter().also {
        it.bookId = bookId
        it.bookName = bookName
        it.id = id
        it.name = name
        it.previousId = previousId
        it.nextId = nextId
        it.hasContent = hasContent
        it.volumeIndex = volumeIndex
        it.volumeName = volumeName
        it.orderNo = orderNo
    }
}
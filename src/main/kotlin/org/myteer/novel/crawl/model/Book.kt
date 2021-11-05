package org.myteer.novel.crawl.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

class Book {
    @JsonProperty("Id")
    var id: String? = null

    @JsonProperty("Name")
    var name: String? = null

    @JsonProperty("Author")
    var author: String? = null

    @JsonProperty("Img")
    var thumbnail: String? = null
        get() = when {
            null != field && field!!.startsWith("http").not() -> "https://imgapixs.pysmei.com/BookFiles/BookImages/$field"
            else -> field
        }
        set(value) {
            field = when {
                null != value && value.startsWith("http").not() -> "https://imgapixs.pysmei.com/BookFiles/BookImages/$value"
                else -> value
            }
        }

    @JsonProperty("Desc")
    var description: String? = null

    @JsonProperty("CName")
    var categoryName: String? = null

    @JsonProperty("LastChapterId")
    var lastChapterId: String? = null

    @JsonProperty("LastChapter")
    var lastChapterName: String? = null

    @JsonProperty("UpdateTime")
    var updateTime: String? = null

    @JsonProperty("LastTime")
    var lastTime: String? = null

    @JsonProperty("BookStatus")
    var status: String? = null

    @JsonProperty("BookVote")
    var bookVote: BookVote? = null

    @JsonProperty("SameUserBooks")
    var sameAuthorBooks: List<Book>? = null

    @JsonProperty("SameCategoryBooks")
    var sameCategoryBooks: List<Book>? = null

    val lastUpdateTime: String?
        get() = updateTime ?: lastTime

    val score: BigDecimal?
        get() = bookVote?.score

    class BookVote(@JsonProperty("Score") val score: BigDecimal?)

    fun toLocalBook() = org.myteer.novel.db.data.Book().also {
        it.id = id
        it.name = name
        it.author = author
        it.thumbnail = thumbnail
        it.description = description
        it.categoryName = categoryName
        it.lastChapterId = lastChapterId
        it.lastChapterName = lastChapterName
        it.lastUpdateTime = lastUpdateTime
        it.status = status
        it.score = score
    }
}
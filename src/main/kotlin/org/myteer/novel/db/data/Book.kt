package org.myteer.novel.db.data

import org.dizitart.no2.objects.Id
import java.math.BigDecimal

class Book(
    @Id
    var id: String? = null,
    var name: String? = null,
    var author: String? = null,
    var thumbnail: String? = null,
    var description: String? = null,
    var categoryName: String? = null,
    var lastChapterId: String? = null,
    var lastChapterName: String? = null,
    var lastUpdateTime: String? = null,
    var status: String? = null,
    var score: BigDecimal? = null
) {
    fun copy() = Book(
        id,
        name,
        author,
        thumbnail,
        description,
        categoryName,
        lastChapterId,
        lastChapterName,
        lastUpdateTime,
        status,
        score
    )

    fun values(): List<String> = listOfNotNull(
        name,
        author,
        categoryName,
        lastChapterName,
        status
    )

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is Book -> false
            else -> other.id == this.id
        }
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
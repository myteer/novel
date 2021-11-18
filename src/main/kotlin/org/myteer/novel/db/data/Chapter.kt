package org.myteer.novel.db.data

import org.dizitart.no2.IndexType
import org.dizitart.no2.objects.Id
import org.dizitart.no2.objects.Index

@Index(value = "bookId", type = IndexType.NonUnique)
class Chapter(
    var bookId: String? = null,
    var bookName: String? = null,
    @Id
    var id: String? = null,
    var name: String? = null,
    var previousId: String? = null,
    var nextId: String? = null,
    var hasContent: Int? = null,
    var content: String? = null,
    var volumeIndex: Int = 0,
    var volumeName: String? = null
) : Comparable<Chapter> {
    override fun compareTo(other: Chapter): Int {
        return if (volumeIndex != other.volumeIndex) {
            volumeIndex.compareTo(other.volumeIndex)
        } else {
            other.id?.let { id?.compareTo(it) } ?: 0
        }
    }
}
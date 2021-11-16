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
    var contentCached: Boolean = false,
    var volumeIndex: Int? = null,
    var volumeName: String? = null
)
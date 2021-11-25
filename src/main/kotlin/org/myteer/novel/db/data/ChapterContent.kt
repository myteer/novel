package org.myteer.novel.db.data

import org.dizitart.no2.IndexType
import org.dizitart.no2.objects.Id
import org.dizitart.no2.objects.Index

@Index(value = "bookId", type = IndexType.NonUnique)
class ChapterContent(
    var bookId: String,
    @Id
    var id: String,
    var content: String? = null
)
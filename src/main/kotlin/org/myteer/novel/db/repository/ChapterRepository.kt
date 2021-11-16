package org.myteer.novel.db.repository

import org.dizitart.no2.Document
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.eq
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Chapter

class ChapterRepository(database: NitriteDatabase) {
    private val repository: ObjectRepository<Chapter> = database.nitrite.getRepository(Chapter::class.java)

    fun selectByBookId(bookId: String): List<Chapter> {
        return repository.find(eq("bookId", bookId)).toList()
    }

    fun selectById(id: String): Chapter? {
        return repository.find(eq("id", id)).firstOrDefault()
    }

    fun save(chapter: Chapter) {
        chapter.id?.let {
            if (null == selectById(it)) {
                repository.insert(chapter)
            } else {
                repository.update(eq("id", it), createUpdateDocument(chapter))
            }
        }
    }

    private fun createUpdateDocument(chapter: Chapter): Document {
        return Document().also { doc ->
            chapter.nextId?.let { doc["nextId"] = it }
            chapter.contentCached.takeIf { it }?.let { doc["contentCached"] = it }
            chapter.volumeIndex?.let { doc["volumeIndex"] = it }
            chapter.volumeName?.let { doc["volumeName"] = it }
        }
    }
}
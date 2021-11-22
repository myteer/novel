package org.myteer.novel.db.repository

import org.dizitart.no2.Document
import org.dizitart.no2.Document.createDocument
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.*
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

    fun deleteByBookId(vararg bookId: String) {
        repository.remove(`in`("bookId", *bookId))
    }

    fun clearContentCacheByBookId(bookId: String) {
        repository.update(and(eq("bookId", bookId), not(eq("content", null))), createDocument("content", null))
    }

    private fun createUpdateDocument(chapter: Chapter): Document {
        return Document().also { doc ->
            chapter.previousId?.let { doc["previousId"] = it }
            chapter.nextId?.let { doc["nextId"] = it }
            chapter.hasContent?.let { doc["hasContent"] = it }
            chapter.content?.let { doc["content"] = it }
            doc["volumeIndex"] = chapter.volumeIndex
            chapter.volumeName?.let { doc["volumeName"] = it }
            doc["orderNo"] = chapter.orderNo
        }
    }
}
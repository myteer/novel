package org.myteer.novel.db.repository

import org.dizitart.no2.Document
import org.dizitart.no2.Document.createDocument
import org.dizitart.no2.FindOptions.sort
import org.dizitart.no2.SortOrder.Ascending
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.*
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Chapter

class ChapterRepository(database: NitriteDatabase) {
    private val repository: ObjectRepository<Chapter> = database.nitrite.getRepository(Chapter::class.java)

    fun selectByBookId(bookId: String): List<Chapter> {
        return repository.find(eq("bookId", bookId), sort("orderNo", Ascending)).toList()
    }

    fun selectById(id: String): Chapter? {
        return repository.find(eq("id", id)).firstOrDefault()
    }

    fun save(chapter: Chapter) {
        val target = selectById(chapter.id)
        if (null == target) {
            repository.insert(chapter)
        } else {
            createUpdateDocument(chapter, target).takeIf { it.isNotEmpty() }?.let {
                repository.update(eq("id", chapter.id), it)
            }
        }
    }

    fun deleteByBookId(vararg bookId: String) {
        repository.remove(`in`("bookId", *bookId))
    }

    fun resetContentCachedByBookId(bookId: String) {
        repository.update(and(eq("bookId", bookId), eq("contentCached", true)), createDocument("contentCached", null))
    }

    private fun createUpdateDocument(source: Chapter, target: Chapter): Document {
        return Document().also { doc ->
            source.previousId.takeUnless { it.isNullOrBlank() || it == target.previousId }?.let { doc["previousId"] = it }
            source.nextId.takeUnless { it.isNullOrBlank() || it == target.nextId }?.let { doc["nextId"] = it }
            source.hasContent.takeUnless { null == it || it == target.hasContent }?.let { doc["hasContent"] = it }
            source.contentCached.takeUnless { null == it || it == target.contentCached }?.let { doc["contentCached"] = it }
            source.volumeIndex.takeUnless { it == target.volumeIndex }?.let { doc["volumeIndex"] = it }
            source.volumeName.takeUnless { it.isNullOrBlank() || it == target.volumeName }?.let { doc["volumeName"] = it }
            source.orderNo.takeUnless { it == target.orderNo }?.let { doc["orderNo"] = it }
        }
    }
}
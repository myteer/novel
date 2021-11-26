package org.myteer.novel.db.repository

import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.`in`
import org.dizitart.no2.objects.filters.ObjectFilters.eq
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.ChapterContent

class ChapterContentRepository(database: NitriteDatabase) {
    private val repository: ObjectRepository<ChapterContent> = database.nitrite.getRepository(ChapterContent::class.java)

    fun selectById(id: String): ChapterContent? {
        return repository.find(eq("id", id)).firstOrDefault()
    }

    fun save(chapterContent: ChapterContent) {
        repository.update(chapterContent, true)
    }

    fun deleteByBookId(vararg bookId: String) {
        repository.remove(`in`("bookId", *bookId))
    }

}
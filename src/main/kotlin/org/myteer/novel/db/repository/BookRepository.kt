package org.myteer.novel.db.repository

import org.dizitart.no2.Document
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.`in`
import org.dizitart.no2.objects.filters.ObjectFilters.eq
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Book

class BookRepository(database: NitriteDatabase) {
    private val repository: ObjectRepository<Book> = database.nitrite.getRepository(Book::class.java)

    fun selectAll(): List<Book> {
        return repository.find().toList()
    }

    fun selectById(id: String): Book? {
        return repository.find(eq("id", id)).firstOrDefault()
    }

    fun save(book: Book) {
        val target = selectById(book.id)
        if (null == target) {
            repository.insert(book)
        } else {
            createUpdateDocument(book, target).takeIf { it.isNotEmpty() }?.let {
                repository.update(eq("id", book.id), it)
            }
        }
    }

    fun deleteById(vararg id: String) {
        repository.remove(`in`("id", *id))
    }

    private fun createUpdateDocument(source: Book, target: Book): Document {
        return Document().also { doc ->
            source.name.takeUnless { it.isNullOrBlank() || it == target.name }?.let { doc["name"] = it }
            source.author.takeUnless { it.isNullOrBlank() || it == target.author }?.let { doc["author"] = it }
            source.thumbnail.takeUnless { it.isNullOrBlank() || it == target.thumbnail }?.let { doc["thumbnail"] = it }
            source.description.takeUnless { it.isNullOrBlank() || it == target.description }?.let { doc["description"] = it }
            source.categoryName.takeUnless { it.isNullOrBlank() || it == target.categoryName }?.let { doc["categoryName"] = it }
            source.lastChapterId.takeUnless { it.isNullOrBlank() || it == target.lastChapterId }?.let { doc["lastChapterId"] = it }
            source.lastChapterName.takeUnless { it.isNullOrBlank() || it == target.lastChapterName }?.let { doc["lastChapterName"] = it }
            source.lastUpdateTime.takeUnless { it.isNullOrBlank() || it == target.lastUpdateTime }?.let { doc["lastUpdateTime"] = it }
            source.status.takeUnless { it.isNullOrBlank() || it == target.status }?.let { doc["status"] = it }
            source.score.takeUnless { null == it || it.setScale(4) == target.score?.setScale(4) }?.let { doc["score"] = it }
        }
    }
}
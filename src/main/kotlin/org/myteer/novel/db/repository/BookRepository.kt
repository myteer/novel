package org.myteer.novel.db.repository

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

    fun insert(book: Book) {
        book.id?.let {
            repository.insert(book)
        }
    }

    fun update(book: Book) {
        book.id?.let {
            repository.update(eq("id", it), book)
        }
    }

    fun deleteById(vararg id: String) {
        repository.remove(`in`("id", *id))
    }
}
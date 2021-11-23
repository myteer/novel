package org.myteer.novel.gui.main.task

import javafx.concurrent.Task
import org.myteer.novel.crawl.model.Chapter
import org.myteer.novel.crawl.task.BookQueryTask
import org.myteer.novel.crawl.task.ChapterListQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Book
import org.myteer.novel.db.repository.BookRepository
import org.myteer.novel.db.repository.ChapterRepository

class BooksRefreshTask(database: NitriteDatabase) : Task<Unit>() {
    private val bookRepository = BookRepository(database)
    private val chapterRepository = ChapterRepository(database)

    override fun call() {
        bookRepository.selectAll().mapNotNull(Book::id).forEach { bookId ->
            BookQueryTask(bookId).apply { run() }.get()?.let { book ->
                bookRepository.save(book.toLocalBook())
                ChapterListQueryTask(bookId).apply { run() }.get().takeIf { it.isNotEmpty() }?.also { chapters ->
                    chapters.map(Chapter::toLocalChapter).forEach(chapterRepository::save)
                }
            }
        }
    }
}
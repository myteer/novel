/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        bookRepository.selectAll().map(Book::id).forEach { bookId ->
            BookQueryTask(bookId).apply { run() }.get()?.let { book ->
                bookRepository.save(book.toLocalBook())
                ChapterListQueryTask(bookId).apply { run() }.get().takeIf { it.isNotEmpty() }?.also { chapters ->
                    chapters.map(Chapter::toLocalChapter).forEach(chapterRepository::save)
                }
            }
        }
    }
}
package org.myteer.novel.gui.bookmanager.task

import javafx.concurrent.Task
import org.myteer.novel.crawl.model.Chapter
import org.myteer.novel.crawl.task.ChapterListQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.repository.ChapterRepository

class ChapterListRefreshTask(private val database: NitriteDatabase, private val bookId: String) : Task<Unit>() {
    override fun call() {
        ChapterListQueryTask(bookId).apply { run() }.get().takeIf { it.isNotEmpty() }?.also { chapters ->
            ChapterRepository(database).also { chapterRepository ->
                chapters.map(Chapter::toLocalChapter).forEach(chapterRepository::save)
            }
        }
    }
}
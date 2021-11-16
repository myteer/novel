package org.myteer.novel.gui.bookmanager.task

import org.myteer.novel.crawl.model.Chapter
import org.myteer.novel.crawl.task.ChapterListQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.repository.ChapterRepository

class ChapterListRefreshTask(database: NitriteDatabase, bookId: String) : ChapterListQueryTask(bookId) {
    init {
        setOnSucceeded {
            ChapterRepository(database).also { chapterRepository ->
                value.map(Chapter::toLocalChapter).forEach(chapterRepository::save)
            }
        }
    }
}
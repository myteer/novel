package org.myteer.novel.gui.bookmanager.task

import org.myteer.novel.crawl.model.Chapter
import org.myteer.novel.crawl.task.ChapterListQueryTask
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.repository.ChapterRepository
import org.myteer.novel.gui.utils.runOutsideUI

class ChapterListRefreshTask(database: NitriteDatabase, bookId: String) : ChapterListQueryTask(bookId) {
    init {
        setOnSucceeded {
            val chapters = value
            runOutsideUI {
                ChapterRepository(database).also { chapterRepository ->
                    chapters.map(Chapter::toLocalChapter).forEach(chapterRepository::save)
                }
            }
        }
    }
}
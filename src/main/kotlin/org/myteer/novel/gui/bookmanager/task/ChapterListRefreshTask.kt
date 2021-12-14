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
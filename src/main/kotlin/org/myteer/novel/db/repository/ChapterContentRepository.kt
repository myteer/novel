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
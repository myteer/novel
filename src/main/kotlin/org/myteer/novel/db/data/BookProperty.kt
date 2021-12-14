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
package org.myteer.novel.db.data

import org.myteer.novel.i18n.i18n
import java.math.BigDecimal

class BookProperty<T> private constructor(
    val id: String,
    val name: String,
    val typeClassReference: Class<T>,
    val getValue: (Book?) -> T?,
    val setValue: Book.(T) -> Unit,
    val isSortable: Boolean = Comparable::class.java.isAssignableFrom(typeClassReference)
) {
    override fun toString(): String {
        return name
    }

    companion object {
        val ID = BookProperty(
            id = "id",
            name = i18n("book.property.id"),
            typeClassReference = String::class.java,
            getValue = { it?.id },
            setValue = { id = it }
        )

        val NAME = BookProperty(
            id = "name",
            name = i18n("book.property.name"),
            typeClassReference = String::class.java,
            getValue = { it?.name },
            setValue = { name = it }
        )

        val AUTHOR = BookProperty(
            id = "author",
            name = i18n("book.property.author"),
            typeClassReference = String::class.java,
            getValue = { it?.author },
            setValue = { author = it }
        )

        val THUMBNAIL = BookProperty(
            id = "thumbnail",
            name = i18n("book.property.thumbnail"),
            typeClassReference = String::class.java,
            getValue = { it?.thumbnail },
            setValue = { thumbnail = it }
        )

        val DESCRIPTION = BookProperty(
            id = "description",
            name = i18n("book.property.description"),
            typeClassReference = String::class.java,
            getValue = { it?.description },
            setValue = { description = it }
        )

        val CATEGORY_NAME = BookProperty(
            id = "categoryName",
            name = i18n("book.property.category_name"),
            typeClassReference = String::class.java,
            getValue = { it?.categoryName },
            setValue = { categoryName = it }
        )

        val LAST_CHAPTER_ID = BookProperty(
            id = "lastChapterId",
            name = i18n("book.property.last_chapter_id"),
            typeClassReference = String::class.java,
            getValue = { it?.lastChapterId },
            setValue = { lastChapterId = it }
        )

        val LAST_CHAPTER_NAME = BookProperty(
            id = "lastChapterName",
            name = i18n("book.property.last_chapter_name"),
            typeClassReference = String::class.java,
            getValue = { it?.lastChapterName },
            setValue = { lastChapterName = it }
        )

        val LAST_UPDATE_TIME = BookProperty(
            id = "lastUpdateTime",
            name = i18n("book.property.last_update_time"),
            typeClassReference = String::class.java,
            getValue = { it?.lastUpdateTime },
            setValue = { lastUpdateTime = it }
        )

        val STATUS = BookProperty(
            id = "status",
            name = i18n("book.property.status"),
            typeClassReference = String::class.java,
            getValue = { it?.status },
            setValue = { status = it }
        )

        val SCORE = BookProperty(
            id = "score",
            name = i18n("book.property.score"),
            typeClassReference = BigDecimal::class.java,
            getValue = { it?.score },
            setValue = { score = it }
        )

        val allProperties by lazy {
            listOf(
                ID,
                NAME,
                AUTHOR,
                THUMBNAIL,
                DESCRIPTION,
                CATEGORY_NAME,
                LAST_CHAPTER_ID,
                LAST_CHAPTER_NAME,
                LAST_UPDATE_TIME,
                STATUS,
                SCORE
            )
        }

        val sortableProperties: List<BookProperty<Comparable<*>>> by lazy {
            allProperties
                .filter(BookProperty<*>::isSortable)
                .filterIsInstance<BookProperty<Comparable<*>>>()
        }
    }
}
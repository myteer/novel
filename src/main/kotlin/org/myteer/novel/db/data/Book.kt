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

import org.dizitart.no2.objects.Id
import java.math.BigDecimal

class Book(
    @Id
    var id: String = "",
    var name: String? = null,
    var author: String? = null,
    var thumbnail: String? = null,
    var description: String? = null,
    var categoryName: String? = null,
    var lastChapterId: String? = null,
    var lastChapterName: String? = null,
    var lastUpdateTime: String? = null,
    var status: String? = null,
    var score: BigDecimal? = null
) {
    fun copy() = Book(
        id,
        name,
        author,
        thumbnail,
        description,
        categoryName,
        lastChapterId,
        lastChapterName,
        lastUpdateTime,
        status,
        score
    )

    fun values(): List<String> = listOfNotNull(
        name,
        author,
        categoryName,
        lastChapterName,
        status
    )

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is Book -> false
            else -> other.id == this.id
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
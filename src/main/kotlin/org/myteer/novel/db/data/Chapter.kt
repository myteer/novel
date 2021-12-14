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

import org.dizitart.no2.IndexType
import org.dizitart.no2.objects.Id
import org.dizitart.no2.objects.Index

@Index(value = "bookId", type = IndexType.NonUnique)
class Chapter(
    var bookId: String = "",
    var bookName: String? = null,
    @Id
    var id: String = "",
    var name: String? = null,
    var previousId: String? = null,
    var nextId: String? = null,
    var hasContent: Int? = null,
    var contentCached: Boolean? = null,
    var volumeIndex: Int = 0,
    var volumeName: String? = null,
    var orderNo: Int = 0
) : Comparable<Chapter> {
    override fun compareTo(other: Chapter): Int {
        return if (volumeIndex != other.volumeIndex) {
            volumeIndex.compareTo(other.volumeIndex)
        } else {
            orderNo.compareTo(other.orderNo)
        }
    }
}
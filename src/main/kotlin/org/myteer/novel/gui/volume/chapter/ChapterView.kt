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
package org.myteer.novel.gui.volume.chapter

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.base.BaseView

class ChapterView(
    preferences: Preferences,
    database: NitriteDatabase,
    bookId: String,
    chapterId: String
) : BaseView() {
    val titleProperty: StringProperty = SimpleStringProperty()

    init {
        setContent(ChapterLoadPane(this, preferences, database, titleProperty, bookId, chapterId))
    }
}
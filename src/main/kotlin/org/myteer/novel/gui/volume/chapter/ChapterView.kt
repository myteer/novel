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
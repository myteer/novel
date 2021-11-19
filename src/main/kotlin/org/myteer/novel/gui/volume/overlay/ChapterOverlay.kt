package org.myteer.novel.gui.volume.overlay

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.base.TitledOverlayBox
import org.myteer.novel.gui.utils.icon

class ChapterOverlay private constructor(
    context: Context,
    preferences: Preferences,
    database: NitriteDatabase,
    bookId: String,
    chapterId: String,
    overlayTitle: StringProperty
) : TitledOverlayBox(
    overlayTitle,
    icon("book-preview-icon"),
    ChapterLoadPane(context, preferences, database, overlayTitle, bookId, chapterId)
) {
    constructor(
        context: Context,
        preferences: Preferences,
        database: NitriteDatabase,
        bookId: String,
        chapterId: String,
    ) : this(
        context,
        preferences,
        database,
        bookId,
        chapterId,
        SimpleStringProperty()
    )
}
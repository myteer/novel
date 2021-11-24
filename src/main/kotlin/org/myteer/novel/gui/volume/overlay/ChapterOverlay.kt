package org.myteer.novel.gui.volume.overlay

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Tooltip
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.base.TitledOverlayBox
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class ChapterOverlay private constructor(
    context: Context,
    preferences: Preferences,
    database: NitriteDatabase,
    bookId: String,
    chapterIdProperty: StringProperty,
    onFinished: () -> Unit,
    overlayTitle: StringProperty
) : TitledOverlayBox(
    overlayTitle,
    icon("book-preview-icon"),
    ChapterLoadPane(context, preferences, database, overlayTitle, bookId, chapterIdProperty),
    true,
    true,
    Button().apply {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        graphic = icon("window-icon")
        tooltip = Tooltip(i18n("chapter.show_in_window"))
        padding = Insets(0.0)
        setOnAction {
            val view = ChapterLoadPane(context, preferences, database, overlayTitle, bookId, chapterIdProperty)
            ChapterWindow(overlayTitle, view, context.getContextWindow()).show()
            onFinished.invoke()
            setOnAction {  }
        }
    }
) {
    constructor(
        context: Context,
        preferences: Preferences,
        database: NitriteDatabase,
        bookId: String,
        chapterId: String,
        onFinished: () -> Unit
    ) : this(
        context,
        preferences,
        database,
        bookId,
        SimpleStringProperty(chapterId),
        onFinished,
        SimpleStringProperty()
    )
}
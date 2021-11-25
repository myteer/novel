package org.myteer.novel.gui.volume.chapter

import javafx.stage.Window
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase

class ChapterActivity(
    private val preferences: Preferences,
    private val database: NitriteDatabase,
    private val bookId: String,
    private val chapterId: String,
) {
    fun show(owner: Window?) {
        val view = ChapterView(preferences, database, bookId, chapterId)
        val window = ChapterWindow(view, owner)
        window.show()
    }
}
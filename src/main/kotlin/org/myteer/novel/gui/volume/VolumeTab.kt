package org.myteer.novel.gui.volume

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.db.data.Book
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.main.BaseTab
import org.myteer.novel.gui.utils.icon

class VolumeTab(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase,
    private val book: Book
) : BaseTab() {
    override val id = "volume-module-${book.id}"
    override val name = book.name.orEmpty()
    override val icon = icon("library-icon")

    private val content: ObjectProperty<VolumeView> = SimpleObjectProperty()

    override fun buildContent(): Node {
        if (null == content.get()) {
            content.set(VolumeView(context, preferences, database, book.id))
        }
        return content.get()
    }

    override fun destroy(): Boolean {
        content.set(null)
        return true
    }
}

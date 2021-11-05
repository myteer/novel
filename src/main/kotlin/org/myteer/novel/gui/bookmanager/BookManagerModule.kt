package org.myteer.novel.gui.bookmanager

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.main.Module
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class BookManagerModule(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase
) : Module() {
    override val id = "book-manager-module"
    override val name = i18n("book.manager.module.title")
    override val icon: Node get() = icon("library-icon")
    override val preview: Node get() = icon("library-icon")

    private val content: ObjectProperty<BookManagerView> = SimpleObjectProperty()

    override fun buildContent(): Node {
        if (null == content.get()) {
            content.set(BookManagerView(context, preferences, database))
        }
        return content.get()
    }

    override fun destroy(): Boolean {
        content.get()?.clearCache()
        content.set(null)
        return true
    }

    override fun sendMessage(message: Message) {
        content.get()?.let { view ->
            when (message) {
                is BookImportRequest -> view.importBook(message.bookId)
            }
        }
    }

    class BookImportRequest(val bookId: String) : Message
}

package org.myteer.novel.gui.book

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.main.Module
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class BookImportModule(private val context: Context) : Module() {
    override val id = "book-import"
    override val name = i18n("book.import.module.title")
    override val icon = icon("biquge-icon")
    override val preview = ImageView(Image("/org/myteer/novel/image/other/biquge_64.png"))

    private val content: ObjectProperty<BookSearchView> = SimpleObjectProperty()

    override fun buildContent(): Node {
        if (null == content.get()) {
            content.set(BookSearchView(context))
        }
        return content.get()
    }

    override fun destroy(): Boolean {
        content.get()?.clearCache()
        content.set(null)
        return true
    }
}
package org.myteer.novel.gui.crawl

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.main.Module
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class CrawlBookImportModule(private val context: Context) : Module() {
    override val id = "crawl-book-import"
    override val name = i18n("crawl.book.import.module.title")
    override val icon = icon("biquge-icon")
    override val preview = ImageView(Image("/org/myteer/novel/image/other/biquge_64.png"))

    private val content: ObjectProperty<CrawlBookSearchView> = SimpleObjectProperty()

    override fun buildContent(): Node {
        if (null == content.get()) {
            content.set(CrawlBookSearchView(context))
        }
        return content.get()
    }

    override fun destroy(): Boolean {
        content.get()?.clearCache()
        content.set(null)
        return true
    }
}
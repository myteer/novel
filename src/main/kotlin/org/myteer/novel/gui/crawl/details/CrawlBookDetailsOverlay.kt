package org.myteer.novel.gui.crawl.details

import javafx.scene.image.ImageView
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.base.TitledOverlayBox
import org.myteer.novel.i18n.i18n

class CrawlBookDetailsOverlay(context: Context, bookId: String, onFinished: () -> Unit) : TitledOverlayBox(
    i18n("crawl.book.details.title"),
    ImageView("/org/myteer/novel/image/other/biquge_16.png"),
    CrawlBookQueryPane(context, bookId, onFinished)
)
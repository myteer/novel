package org.myteer.novel.gui.update.sequence.details

import com.sandec.mdfx.MarkdownView
import javafx.scene.Cursor
import javafx.scene.Node
import org.myteer.novel.utils.SystemBrowser

class PreviewMarkdownView(markdown: String? = null) : MarkdownView(markdown) {
    override fun setLink(node: Node?, link: String?, description: String?) {
        node?.let {
            it.cursor = Cursor.HAND
            it.setOnMouseClicked {
                link?.let(SystemBrowser::browse)
            }
        }
    }
}
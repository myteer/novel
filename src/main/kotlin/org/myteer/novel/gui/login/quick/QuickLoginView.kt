package org.myteer.novel.gui.login.quick

import javafx.scene.layout.VBox
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.base.BaseView
import org.myteer.novel.gui.login.DatabaseLoginListener

class QuickLoginView(
    databaseMeta: DatabaseMeta,
    databaseLoginListener: DatabaseLoginListener
) : BaseView() {
    init {
        setContent(VBox(
            QuickLoginToolBar(databaseMeta),
            QuickLoginForm(this, databaseMeta, databaseLoginListener)
        ))
    }
}
package org.myteer.novel.gui.login.quick

import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.login.DatabaseLoginListener

class QuickLoginActivity(private val databaseMeta: DatabaseMeta, databaseLoginListener: DatabaseLoginListener) {
    private val view = QuickLoginView(databaseMeta, databaseLoginListener)

    fun show() {
        val window = QuickLoginWindow(view, databaseMeta)
        window.show()
    }

    fun getContext(): Context = view
}
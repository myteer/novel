package org.myteer.novel.gui.bookshelf

import javafx.scene.Node
import javafx.scene.control.Button
import org.myteer.novel.config.Preferences
import org.myteer.novel.db.NitriteDatabase
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.main.Module
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class BookShelf2(
    private val context: Context,
    private val preferences: Preferences,
    private val database: NitriteDatabase
) : Module() {
    override val id: String = "bookshelf-module2"
    override val name: String = i18n("preferences.tab.appearance")
    override val icon: Node = icon("google-icon")

    override fun buildContent(): Node {
        return Button("测试")
    }

    override fun destroy(): Boolean {
        println("销毁书架2")
        return true
    }
}
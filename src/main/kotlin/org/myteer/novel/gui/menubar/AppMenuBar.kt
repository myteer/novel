package org.myteer.novel.gui.menubar

import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.main.MainView
import org.myteer.novel.i18n.i18n

class AppMenuBar(
    view: MainView,
    preferences: Preferences,
    databaseTracker: DatabaseTracker
) : MenuBar() {
    init {
        menus.addAll(
            Menu(i18n("menubar.menu.file"))
        )
    }
}
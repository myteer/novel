package org.myteer.novel.gui.action

import javafx.scene.control.MenuItem
import org.myteer.novel.config.Preferences
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.gui.utils.action
import org.myteer.novel.gui.utils.graphic
import org.myteer.novel.gui.utils.keyBinding

object MenuItems {
    fun of(
        action: Action,
        context: Context,
        preferences: Preferences,
        databaseTracker: DatabaseTracker
    ) = MenuItem(action.displayName)
        .action { action.invoke(context, preferences, databaseTracker) }
        .keyBinding(action.keyBinding)
        .graphic(action.iconStyleClass)

    fun <M : MenuItem> of(
        action: Action,
        context: Context,
        preferences: Preferences,
        databaseTracker: DatabaseTracker,
        menuItemFactory: (String) -> M
    ) = menuItemFactory(action.displayName)
        .action { action.invoke(context, preferences, databaseTracker) }
        .keyBinding(action.keyBinding)
        .graphic(action.iconStyleClass)
}
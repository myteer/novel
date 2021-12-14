/*
 * Copyright (c) 2021 MTSoftware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
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
package org.myteer.novel.gui.dbcreator

import javafx.scene.control.Label
import javafx.scene.control.ToolBar
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class DatabaseCreatorToolBar : ToolBar() {
    init {
        styleClass.add("header-toolbar")
        buildUI()
    }

    private fun buildUI() {
        items.addAll(
            icon("database-plus-icon"),
            Label(i18n("database.creator.title"))
        )
    }
}
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
package org.myteer.novel.gui.login.quick

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.utils.icon
import org.myteer.novel.i18n.i18n

class QuickLoginToolBar(private val databaseMeta: DatabaseMeta) : HBox(10.0) {
    init {
        styleClass.add("header-toolbar")
        buildUI()
    }

    private fun buildUI() {
        children.add(buildLogo())
        children.add(buildLabel())
    }

    private fun buildLogo() = icon("login-icon")

    private fun buildLabel() = Label().apply {
        text = i18n("login.quick.title") + " - " + databaseMeta
        setHgrow(this, Priority.ALWAYS)
    }
}
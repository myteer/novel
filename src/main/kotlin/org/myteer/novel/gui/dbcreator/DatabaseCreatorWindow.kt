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

import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class DatabaseCreatorWindow(view: DatabaseCreatorView, owner: Window?) :
    BaseWindow<DatabaseCreatorView>(i18n("window.dbcreator.title"), view) {
    init {
        initModality(Modality.APPLICATION_MODAL)
        owner?.also { initOwner(it) }
        width = 741.0
        height = 435.0
        centerOnScreen()
    }
}
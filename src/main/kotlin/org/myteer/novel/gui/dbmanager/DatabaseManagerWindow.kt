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
package org.myteer.novel.gui.dbmanager

import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class DatabaseManagerWindow(view: DatabaseManagerView, owner: Window?) :
    BaseWindow<DatabaseManagerView>(i18n("window.dbmanager.title"), view) {
    init {
        initModality(Modality.APPLICATION_MODAL)
        owner?.let { initOwner(it) }
        width = 1000.0
        height = 430.0
        centerOnScreen()
    }
}
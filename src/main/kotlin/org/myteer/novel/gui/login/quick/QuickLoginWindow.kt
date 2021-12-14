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

import javafx.stage.Modality
import javafx.stage.Window
import org.myteer.novel.db.DatabaseMeta
import org.myteer.novel.gui.window.BaseWindow
import org.myteer.novel.i18n.i18n

class QuickLoginWindow(
    view: QuickLoginView,
    databaseMeta: DatabaseMeta,
    owner: Window? = null
) : BaseWindow<QuickLoginView>(i18n("window.login.quick.title", databaseMeta), view) {
    init {
        initModality(Modality.APPLICATION_MODAL)
        owner?.let { initOwner(it) }
        width = 460.0
        height = 230.0
        centerOnScreen()
    }
}
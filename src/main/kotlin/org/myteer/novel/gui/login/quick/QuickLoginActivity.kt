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
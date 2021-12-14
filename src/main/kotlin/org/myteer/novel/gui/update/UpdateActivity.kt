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
package org.myteer.novel.gui.update

import javafx.scene.Group
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import org.myteer.novel.gui.api.Context
import org.myteer.novel.update.Release

class UpdateActivity(private val context: Context, private val release: Release) {
    fun show() {
        var overlay: Region? = null
        overlay = UpdateDialog(context, release) {
            context.hideOverlay(overlay!!)
        }.let { StackPane(Group(it)) }
        context.showOverlay(overlay, true)
    }
}
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
package org.myteer.novel.gui.update.sequence.details

import javafx.geometry.Insets
import javafx.scene.layout.VBox
import org.myteer.novel.gui.utils.scrollPane
import org.myteer.novel.update.Release

class DetailsSegmentView(private val release: Release) : VBox() {
    init {
        children.add(buildPreviewScrollPane())
    }

    private fun buildPreviewScrollPane() =
        scrollPane(PreviewMarkdownView(release.description), fitToWidth = true, fitToHeight = true).apply {
            setMargin(this, Insets(0.0, 0.0, 10.0, 0.0))
            prefWidth = 400.0
            maxHeight = 300.0
        }
}
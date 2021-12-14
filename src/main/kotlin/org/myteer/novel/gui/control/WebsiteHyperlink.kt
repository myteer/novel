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
package org.myteer.novel.gui.control

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.Hyperlink
import javafx.scene.control.Tooltip
import org.myteer.novel.utils.SystemBrowser

class WebsiteHyperlink(text: String, url: String? = null) : Hyperlink(text) {
    private val urlProperty: StringProperty = SimpleStringProperty(url)

    init {
        tooltip = WebsiteHyperlinkTooltip(this)
        setOnAction {
            urlProperty.get()?.let(SystemBrowser::browse)
        }
    }

    fun urlProperty(): StringProperty = urlProperty

    private class WebsiteHyperlinkTooltip(parent: WebsiteHyperlink) : Tooltip() {
        init {
            textProperty().bind(parent.urlProperty)
        }
    }
}
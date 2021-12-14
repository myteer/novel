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
package org.myteer.novel.gui.export.json

import org.myteer.novel.export.json.JsonExportConfiguration
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.export.ConfigurationDialog

class JsonConfigurationDialog : ConfigurationDialog<JsonExportConfiguration> {
    override fun show(context: Context, onFinished: (JsonExportConfiguration) -> Unit) {
        var overlay: JsonConfigurationOverlay? = null
        overlay = JsonConfigurationOverlay {
            context.hideOverlay(overlay!!)
            onFinished(it)
        }
        context.showOverlay(overlay)
    }
}
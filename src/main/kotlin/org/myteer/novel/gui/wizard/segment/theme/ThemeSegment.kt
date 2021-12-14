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
package org.myteer.novel.gui.wizard.segment.theme

import com.dansoftware.sgmdialog.FixedContentTitledSegment
import javafx.scene.Node
import org.myteer.novel.config.Preferences
import org.myteer.novel.i18n.i18n

class ThemeSegment(
    private val preferences: Preferences
) : FixedContentTitledSegment(i18n("segment.theme.name"), i18n("segment.theme.title")) {
    override fun createCenterContent(): Node = ThemeSegmentView(preferences)
}
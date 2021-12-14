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
package org.myteer.novel.gui.crawl.details

import javafx.scene.image.ImageView
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.base.TitledOverlayBox
import org.myteer.novel.i18n.i18n

class CrawlBookDetailsOverlay(context: Context, bookId: String, onFinished: () -> Unit) : TitledOverlayBox(
    i18n("crawl.book.details.title"),
    ImageView("/org/myteer/novel/image/other/biquge_16.png"),
    CrawlBookQueryPane(context, bookId, onFinished)
)
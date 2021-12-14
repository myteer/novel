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

import com.dansoftware.sgmdialog.SegmentDialog
import com.dansoftware.sgmdialog.SegmentSequence
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import jfxtras.styles.jmetro.JMetroStyleClass
import org.myteer.novel.gui.api.Context
import org.myteer.novel.gui.update.sequence.details.DetailsSegment
import org.myteer.novel.gui.update.sequence.download.DownloadSegment
import org.myteer.novel.gui.update.sequence.notification.NotificationSegment
import org.myteer.novel.gui.utils.onValuePresent
import org.myteer.novel.i18n.i18n
import org.myteer.novel.update.Release
import org.myteer.novel.utils.InMemoryResourceBundle
import java.util.*

class UpdateDialog(
    context: Context,
    release: Release,
    private val onFinished: () -> Unit
) : SegmentDialog(buildSegmentResourceBundle(), DialogSegmentSequence(context, release, onFinished)) {
    init {
        styleClass.add(JMetroStyleClass.BACKGROUND)
        styleClass.add("update-dialog")
        customButtons = listOf(LaterButton())
        segmentSequence.focusedSegmentProperty().onValuePresent {
            customButtons = when {
                segmentSequence.isSegmentLast(it) -> listOf()
                else -> listOf(LaterButton())
            }
        }
    }

    private inner class LaterButton : Button() {
        init {
            text = i18n("update.dialog.button.later")
            HBox.setMargin(this, Insets(0.0, 10.0, 0.0, 0.0))
            setOnAction {
                onFinished.invoke()
            }
        }
    }

    private class DialogSegmentSequence(
        context: Context,
        release: Release,
        private val onFinished: () -> Unit
    ) : SegmentSequence(
        NotificationSegment(release),
        DetailsSegment(release),
        DownloadSegment(context, release)
    ) {
        override fun onSegmentsFinished(segmentDialog: SegmentDialog) {
            onFinished.invoke()
        }
    }

    private companion object {
        fun buildSegmentResourceBundle(): ResourceBundle {
            return InMemoryResourceBundle.Builder()
                .put("segment.dialog.button.finish", i18n("update.dialog.button.finish"))
                .put("segment.dialog.button.next", i18n("update.dialog.button.next"))
                .put("segment.dialog.button.prev", i18n("update.dialog.button.prev"))
                .put("segment.dialog.button.skip", i18n("update.dialog.button.skip"))
                .build()
        }
    }
}
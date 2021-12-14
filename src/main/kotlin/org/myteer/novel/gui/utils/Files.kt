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
package org.myteer.novel.gui.utils

import com.jfilegoodies.explorer.FileExplorers
import java.awt.Desktop
import java.io.File

private inline val desktop: Desktop
    get() = Desktop.getDesktop()

fun File.revealInExplorer() {
    runOutsideUIAsync {
        if (desktop.isSupported(Desktop.Action.BROWSE_FILE_DIR)) {
            desktop.browseFileDirectory(this)
        } else {
            FileExplorers.get().openSelect(this)
        }
    }
}

fun File.open() {
    runOutsideUIAsync {
        desktop.open(this)
    }
}


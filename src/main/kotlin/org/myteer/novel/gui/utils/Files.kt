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


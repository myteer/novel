package org.myteer.novel.gui.utils

import javafx.scene.control.TextFormatter

class SpaceValidator : TextFormatter<TextFormatter.Change>({
    when {
        it.text?.isNotEmpty() ?: false -> it.text = it.text.replace(Regex("\\s+"), "")
    }
    it
})
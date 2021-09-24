package org.myteer.novel.gui.utils

import javafx.scene.Node
import javafx.stage.Stage
import javafx.stage.Window

val Node?.window: Window?
    get() = this?.scene?.window

val Node?.stage: Stage?
    get() = window as? Stage
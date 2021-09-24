package org.myteer.novel.gui.preloader

import javafx.stage.Stage
import javafx.stage.StageStyle
import org.apache.commons.lang3.StringUtils

class BackingStage : Stage(StageStyle.UTILITY) {
    init {
        title = StringUtils.EMPTY
        opacity = 0.0
    }

    fun createChild(stageStyle: StageStyle = StageStyle.DECORATED): Stage {
        return Stage(stageStyle).also {
            it.initOwner(this)
        }
    }
}
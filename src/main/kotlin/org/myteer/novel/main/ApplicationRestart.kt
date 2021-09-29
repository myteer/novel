package org.myteer.novel.main

import javafx.application.Platform
import org.myteer.novel.instance.ApplicationInstanceService

class ApplicationRestart {
    companion object {
        fun restart() {
            com.restart4j.ApplicationRestart.builder()
                .beforeNewProcessCreated(ApplicationInstanceService::release)
                .terminationPolicy(Platform::exit)
                .build()
                .restartApp()
        }
    }
}
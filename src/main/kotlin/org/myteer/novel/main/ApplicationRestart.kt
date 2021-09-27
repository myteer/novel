package org.myteer.novel.main

import javafx.application.Platform

class ApplicationRestart {
    companion object {
        fun restart() {
            com.restart4j.ApplicationRestart.builder()
                .terminationPolicy(Platform::exit)
                .build()
                .restartApp()
        }
    }
}
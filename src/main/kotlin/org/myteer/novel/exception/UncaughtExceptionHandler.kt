package org.myteer.novel.exception

import javafx.application.Platform
import org.slf4j.LoggerFactory

class UncaughtExceptionHandler : Thread.UncaughtExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(UncaughtExceptionHandler::class.java)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        logger.error("Uncaught exception occurred", e)
        showDialog(e)
    }

    private fun showDialog(e: Throwable) {
        Platform.runLater {
            UncaughtExceptionDialog(e).show()
        }
    }
}
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
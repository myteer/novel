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

import javafx.application.Platform
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

private object ThreadManager {
    private val executorService = Executors.newCachedThreadPool {
        Thread(it).apply {
            isDaemon = true
        }
    }

    private fun isUIThread(): Boolean = Platform.isFxApplicationThread()

    fun runInsideUI(runnable: Runnable) {
        if (isUIThread()) {
            runnable.run()
        } else {
            Platform.runLater(runnable)
        }
    }

    fun runInsideUIAsync(runnable: Runnable) {
        Platform.runLater(runnable)
    }

    fun runInsideUISync(runnable: Runnable) {
        if (isUIThread()) {
            runnable.run()
        } else {
            val task: FutureTask<Unit> = FutureTask({
                runnable.run()
            }, Unit)
            Platform.runLater(task)
            task.get()
        }
    }

    fun runOutsideUI(runnable: Runnable) {
        if (!isUIThread()) {
            runnable.run()
        } else {
            executorService.submit(runnable)
        }
    }

    fun runOutsideUIAsync(runnable: Runnable) {
        executorService.submit(runnable)
    }

    fun runOutsideUISync(runnable: Runnable) {
        if (!isUIThread()) {
            runnable.run()
        } else {
            executorService.submit(runnable).get()
        }
    }
}

fun runInsideUI(runnable: Runnable) = ThreadManager.runInsideUI(runnable)

fun runInsideUIAsync(runnable: Runnable) = ThreadManager.runInsideUIAsync(runnable)

fun runInsideUISync(runnable: Runnable) = ThreadManager.runInsideUISync(runnable)

fun runOutsideUI(runnable: Runnable) = ThreadManager.runOutsideUI(runnable)

fun runOutsideUIAsync(runnable: Runnable) = ThreadManager.runOutsideUIAsync(runnable)

fun runOutsideUISync(runnable: Runnable) = ThreadManager.runOutsideUISync(runnable)
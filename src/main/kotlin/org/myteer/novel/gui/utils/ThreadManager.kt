package org.myteer.novel.gui.utils

import javafx.application.Platform
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

private object ThreadManager {
    private val executorService = Executors.newFixedThreadPool(5)

    private fun isUIThread(): Boolean = Platform.isFxApplicationThread()

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
            executorService.submit {
                runnable.run()
            }
        }
    }

    fun runOutsideUIAsync(runnable: Runnable) {
        executorService.submit {
            runnable.run()
        }
    }
}

fun runInsideUIAsync(runnable: Runnable) = ThreadManager.runInsideUIAsync(runnable)

fun runInsideUISync(runnable: Runnable) = ThreadManager.runInsideUISync(runnable)

fun runOutsideUI(runnable: Runnable) = ThreadManager.runOutsideUI(runnable)

fun runOutsideUIAsync(runnable: Runnable) = ThreadManager.runOutsideUIAsync(runnable)
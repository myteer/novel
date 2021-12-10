package org.myteer.novel.gui.update.sequence.download

import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.concurrent.Task
import okhttp3.internal.closeQuietly
import okhttp3.internal.notify
import okhttp3.internal.wait
import org.myteer.novel.i18n.i18n
import org.myteer.novel.update.ReleaseAsset
import org.slf4j.LoggerFactory
import java.io.File

class DownloadTask(private val releaseAsset: ReleaseAsset, private val dir: File) : Task<File?>() {
    private val pausedProperty = ReadOnlyBooleanWrapper(false)
    private val lock = Any()

    fun pausedProperty(): ReadOnlyBooleanProperty = pausedProperty.readOnlyProperty

    fun isPaused(): Boolean = pausedProperty.get()

    fun setPaused(paused: Boolean) {
        if (pausedProperty.get() != paused) {
            pausedProperty.set(paused)
            if (!paused) {
                synchronized(lock) {
                    lock.notify()
                }
            }
        }
    }

    override fun call(): File? {
        synchronized(lock) {
            logger.debug("Starting downloading the update bundle...")

            val outputFile = File(dir, releaseAsset.name!!)
            val input = releaseAsset.openInputStream().buffered()
            val output = outputFile.outputStream().buffered()

            try {
                updateMessage(i18n("update.dialog.download.happening"))

                val contentSize = releaseAsset.size
                logger.debug("Full content size (in bytes): {}", contentSize)

                val onePercent = contentSize / 100
                logger.debug("1% size (in bytes): {}", onePercent)

                logger.debug("Starting loop...")
                var allBytesRead = 0
                var bytesRead: Int
                val buffer = ByteArray(4096)
                while (true) {
                    bytesRead = input.read(buffer)
                    if (bytesRead < 0) break
                    allBytesRead += bytesRead
                    output.write(buffer, 0, bytesRead)

                    if (onePercent > 0) {
                        val percent = allBytesRead * 1.0 / onePercent
                        updateProgress(percent, 100.0)
                        updateTitle("${percent.toInt()}%")
                    }

                    if (isCancelled) {
                        logger.debug("Cancelled during loop")
                        updateProgress(0.0, 100.0)
                        return null
                    }

                    if (isPaused()) {
                        logger.debug("Paused")
                        updateMessage(i18n("update.dialog.download.paused"))
                        lock.wait()
                        updateMessage(i18n("update.dialog.download.happening"))
                    }
                }

                logger.debug("Ending loop...")
                updateProgress(100.0, 100.0)
                logger.debug("Download task succeeded: {}", outputFile)
                return outputFile
            } finally {
                output.closeQuietly()
                input.closeQuietly()
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DownloadTask::class.java)
    }
}
package org.myteer.novel.export.api

interface ExportProcessObserver {
    fun updateMessage(message: String?)
    fun updateProgress(workDone: Double, max: Double)
    fun updateTitle(title: String?)
}
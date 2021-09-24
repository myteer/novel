package org.myteer.novel.exception

import org.apache.commons.lang3.StringUtils
import org.controlsfx.dialog.ExceptionDialog
import org.myteer.novel.i18n.i18n

class UncaughtExceptionDialog(e: Throwable) : ExceptionDialog(e) {
    init {
        title = i18n("dialog.uncaught.title")
        headerText = i18n("dialog.uncaught.header_text")
        contentText = StringUtils.EMPTY
    }
}
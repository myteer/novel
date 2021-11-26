package org.myteer.novel.db

import org.dizitart.no2.Nitrite
import org.dizitart.no2.NitriteBuilder
import org.dizitart.no2.exceptions.ErrorMessage
import org.dizitart.no2.exceptions.NitriteException
import org.dizitart.no2.exceptions.NitriteIOException
import org.myteer.novel.config.login.Credentials
import org.myteer.novel.i18n.i18n
import java.io.File

open class NitriteDatabase(
    val nitrite: Nitrite,
    val meta: DatabaseMeta = DatabaseMeta(toString(), null)
){
    val isClosed: Boolean = nitrite.isClosed

    fun close() {
        nitrite.close()
    }

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private val nitriteBuilder: NitriteBuilder = Nitrite.builder()
        private var databaseMeta: DatabaseMeta? = null
        private var onFailed: FailListener? = null

        fun databaseMeta(databaseMeta: DatabaseMeta) = apply {
            this.databaseMeta = databaseMeta
            databaseMeta.file?.let { filePath(it) }
        }

        fun onFailed(onFailed: FailListener) = apply {
            this.onFailed = onFailed
        }

        private fun filePath(file: File) = apply {
            nitriteBuilder.filePath(file)
            compressed()
        }

        private fun compressed() = apply {
            nitriteBuilder.compressed()
        }

        fun autoCommitBufferSize(size: Int) = apply {
            nitriteBuilder.autoCommitBufferSize(size)
        }

        fun touch(credentials: Credentials? = null) {
            build(credentials)?.close()
        }

        fun build(credentials: Credentials? = null): NitriteDatabase? = createDatabase {
            if (null == credentials || credentials.isAnonymous) {
                nitriteBuilder.openOrCreate()
            } else {
                nitriteBuilder.openOrCreate(credentials.username, credentials.password)
            }
        }

        private fun createDatabase(buildNitrite: () -> Nitrite): NitriteDatabase? {
            return try {
                databaseMeta?.let { NitriteDatabase(buildNitrite(), it) } ?: NitriteDatabase(buildNitrite())
            } catch (e: NitriteException) {
                onFailed?.onFail(e.message(), e)
                null
            }
        }

        private fun NitriteException.message(): String {
            return i18n(
                when (errorMessage) {
                    ErrorMessage.NO_USER_MAP_FOUND -> "login.failed.null_user_credential"
                    ErrorMessage.USER_MAP_SHOULD_NOT_EXISTS -> "login.failed.authentication_required"
                    ErrorMessage.DATABASE_OPENED_IN_OTHER_PROCESS -> "login.failed.database_opened_in_other_process"
                    ErrorMessage.UNABLE_TO_CREATE_DB_FILE -> "login.failed.unable_to_create_db_file"

                    ErrorMessage.USER_ID_IS_EMPTY,
                    ErrorMessage.PASSWORD_IS_EMPTY,
                    ErrorMessage.NULL_USER_CREDENTIAL,
                    ErrorMessage.INVALID_USER_PASSWORD -> "login.failed.invalid_user_password"

                    else -> if (this is NitriteIOException) "login.failed.io" else "login.failed.security"
                }
            )
        }

        fun interface FailListener {
            fun onFail(message: String, throwable: Throwable)
        }
    }
}
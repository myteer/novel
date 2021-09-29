package org.myteer.novel.main

import org.apache.commons.lang3.StringUtils
import org.myteer.novel.db.DatabaseMeta
import java.io.File
import java.util.*

class ArgumentTransformer {
    companion object {

        fun transform(args: List<String>?): DatabaseMeta? {
            return transformOptional(args).orElse(null)
        }

        fun transformOptional(args: List<String>?): Optional<DatabaseMeta> {
            return if (args.isNullOrEmpty()) {
                Optional.empty()
            } else {
                transformOptional(args[0])
            }
        }

        fun transformOptional(arg: String): Optional<DatabaseMeta> {
            return if (StringUtils.isBlank(arg)) {
                Optional.empty()
            } else {
                val file = File(arg)
                if (file.exists().not() || file.isDirectory) {
                    Optional.empty()
                } else {
                    Optional.of(DatabaseMeta(file))
                }
            }
        }
    }
}
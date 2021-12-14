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
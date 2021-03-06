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

import com.juserdirs.UserDirectories
import com.sun.javafx.runtime.VersionInfo
import org.apache.commons.io.FileUtils
import java.io.File

object PropertiesSetup {
    const val APP_NAME = "app.name"
    const val APP_VERSION = "app.version"
    const val APP_COMPANY = "app.company"
    const val APP_DEVELOPER = "app.developer"
    const val APP_BUILD_INFO = "app.build.info"
    const val APP_FILE_EXTENSION = "app.file.extension"
    const val CONFIG_FILE_PATH = "config.file.path"
    const val DEFAULT_DIRECTORY_PATH = "default.directory.path"
    const val LOG_FILE_PATH = "log.file.path"
    const val LOG_FILE_PATH_FULL = "log.file.path.full"


    private const val APP_NAME_VALUE = "Novel"
    private const val APP_VERSION_VALUE = "0.0.1"
    private const val APP_COMPANY_VALUE = "MTSoftware"
    private const val APP_DEVELOPER_VALUE = "myteer"
    private const val APP_BUILD_INFO_VALUE = "Built on 2021-12-13"
    private const val APP_FILE_EXTENSION_VALUE = "ndb"
    private val LOG_FILE_PATH_VALUE = FileUtils.getFile(FileUtils.getTempDirectory(), "novel").toString()
    private val LOG_FILE_PATH_FULL_VALUE = "$LOG_FILE_PATH_VALUE.log"

    fun setupSystemProperties() {
        setupAppProperties()
        setupLogProperties()
        setupJFXProperties()
    }

    private fun setupAppProperties() {
        System.setProperty(APP_NAME, APP_NAME_VALUE)
        System.setProperty(APP_VERSION, APP_VERSION_VALUE)
        System.setProperty(APP_COMPANY, APP_COMPANY_VALUE)
        System.setProperty(APP_DEVELOPER, APP_DEVELOPER_VALUE)
        System.setProperty(APP_BUILD_INFO, APP_BUILD_INFO_VALUE)
        System.setProperty(APP_FILE_EXTENSION, APP_FILE_EXTENSION_VALUE)
        System.setProperty(CONFIG_FILE_PATH, getConfigFilePath())
        System.setProperty(DEFAULT_DIRECTORY_PATH, getDefaultDirectoryPath())
    }

    private fun getConfigFilePath(): String {
        return listOf(FileUtils.getUserDirectoryPath(), ".novel", "config.json").joinToString(File.separator)
    }

    private fun getDefaultDirectoryPath(): String {
        return File(
            UserDirectories.get().documentsDirectoryPath() ?: System.getProperty("user.home"),
            "NovelDocuments"
        ).absolutePath
    }

    private fun setupLogProperties() {
        System.setProperty(LOG_FILE_PATH, LOG_FILE_PATH_VALUE)
        System.setProperty(LOG_FILE_PATH_FULL, LOG_FILE_PATH_FULL_VALUE)
    }

    private fun setupJFXProperties() = VersionInfo.setupSystemProperties()

}
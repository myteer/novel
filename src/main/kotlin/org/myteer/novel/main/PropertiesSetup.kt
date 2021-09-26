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
    private const val APP_VERSION_VALUE = "1.0.0-SNAPSHOT"
    private const val APP_COMPANY_VALUE = "MTSoftware"
    private const val APP_DEVELOPER_VALUE = "myteer"
    private const val APP_BUILD_INFO_VALUE = "Built on 2021-09-17"
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
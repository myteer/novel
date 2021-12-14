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
package org.myteer.novel.instance

import it.sauronsoftware.junique.AlreadyLockedException
import it.sauronsoftware.junique.JUnique
import it.sauronsoftware.junique.MessageHandler
import org.apache.commons.io.FileUtils
import org.myteer.novel.config.PreferenceKey
import org.myteer.novel.config.Preferences
import org.myteer.novel.config.login.LoginData
import org.myteer.novel.gui.entry.DatabaseTracker
import org.myteer.novel.launcher.ActivityLauncher
import org.myteer.novel.launcher.LauncherMode
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

class ApplicationInstanceService private constructor(params: List<String>) : MessageHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(ApplicationInstanceService::class.java)
        private const val APPLICATION_ID = "org.myteer.novel"
        private const val ERROR_MESSAGE = -1
        private const val EMPTY_ARGS_MESSAGE = 0
        private const val SUCCESS_MESSAGE = 1
        private var instance: ApplicationInstanceService? = null

        @Synchronized
        fun open(args: List<String>) {
            if (null == instance) {
                instance = ApplicationInstanceService(args)
            }
        }

        @Synchronized
        fun release() {
            JUnique.releaseLock(APPLICATION_ID)
        }
    }

    init {
        try {
            JUnique.acquireLock(APPLICATION_ID, this)
        } catch (e: AlreadyLockedException) {
            logger.info("An application is already running with the id: '${e.id}'")
            logger.info("Sending the arguments to the already running instance...")
            logger.debug("The arguments are: $params")

            if (params.isEmpty()) {
                JUnique.sendMessage(APPLICATION_ID, EMPTY_ARGS_MESSAGE.toString())
            } else {
                try {
                    writeArgumentsToFile(RuntimeArgumentsHolderFile, params)
                    JUnique.sendMessage(APPLICATION_ID, SUCCESS_MESSAGE.toString())
                } catch (e: Exception) {
                    JUnique.sendMessage(APPLICATION_ID, ERROR_MESSAGE.toString())
                }
            }

            logger.info("Exiting...")
            exitProcess(0)
        }
    }

    private fun writeArgumentsToFile(file: File, params: List<String>) {
        FileUtils.writeLines(file, StandardCharsets.UTF_8.name(), params, false)
    }

    private fun readArgumentsFromFile(file: File): List<String> {
        return FileUtils.readLines(file, StandardCharsets.UTF_8)
    }

    override fun handle(message: String): String {
        logger.debug("message from another process: $message")
        var params: List<String> = listOf()
        when (Integer.parseInt(message)) {
            EMPTY_ARGS_MESSAGE -> {
                logger.debug("No need to read arguments from file")
            }
            ERROR_MESSAGE -> {
                logger.debug("Something went wrong in the other process")
                logger.debug("Aren't reading the arguments")
            }
            SUCCESS_MESSAGE -> {
                logger.debug("Reading arguments from file...")
                try {
                    params = readArgumentsFromFile(RuntimeArgumentsHolderFile)
                    logger.debug("Clearing argument-holder file...")
                    RuntimeArgumentsHolderFile.clear()
                } catch (e: Exception) {
                    logger.error("Failed to read the arguments from file!", e)
                }
            }
        }

        logger.debug("starting an ActivityLauncher...")
        ActivityLauncherImpl(Preferences.getPreferences(), DatabaseTracker.global, params).run()
        return ""
    }

    private class ActivityLauncherImpl(
        private val preferences: Preferences,
        private val databaseTracker: DatabaseTracker,
        params: List<String>
    ) : ActivityLauncher(preferences, databaseTracker, LauncherMode.ALREADY_RUNNING, params) {
        private val loginData: LoginData

        init {
            loginData = buildLoginData()
        }

        private fun buildLoginData(): LoginData {
            val usingDatabases = databaseTracker.getUsingDatabases()
            return preferences.get(PreferenceKey.LOGIN_DATA).also {
                usingDatabases.forEach(it::removeSavedDatabase)
                it.setSelectedDatabase(null)
                it.setAutoLogin(false)
            }
        }

        override fun getLoginData(): LoginData = loginData

        override fun saveLoginData(loginData: LoginData) {
            preferences.editor().put(PreferenceKey.LOGIN_DATA, loginData).tryCommit()
        }
    }
}
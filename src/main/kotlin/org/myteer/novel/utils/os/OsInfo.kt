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
package org.myteer.novel.utils.os

import oshi.PlatformEnum
import oshi.SystemInfo

object OsInfo {
    private val platformType: PlatformEnum
    private val version: String
    private val buildNumber: String
    private val name: String

    init {
        val os = SystemInfo().operatingSystem
        platformType = SystemInfo.getCurrentPlatform()
        version = os.versionInfo.version
        buildNumber = os.versionInfo.buildNumber
        name = os.family
    }

    fun isWindows(): Boolean = hasType(PlatformEnum.WINDOWS)

    fun isLinux(): Boolean = hasType(PlatformEnum.LINUX)

    fun isMac(): Boolean = hasType(PlatformEnum.MACOS)

    fun isWindows10(): Boolean = hasTypeAndVersion(PlatformEnum.WINDOWS, "10")

    fun getPlatformType() = platformType

    fun getVersion() = version

    fun getBuildNumber() = buildNumber

    fun getName() = name

    private fun hasTypeAndVersion(platformType: PlatformEnum, versionStarts: String): Boolean =
        hasType(platformType) && version.startsWith(versionStarts)

    private fun hasType(platformType: PlatformEnum): Boolean = OsInfo.platformType == platformType
}
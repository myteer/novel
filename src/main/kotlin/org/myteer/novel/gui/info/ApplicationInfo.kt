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
package org.myteer.novel.gui.info

import org.myteer.novel.main.PropertiesSetup
import org.myteer.novel.utils.os.OsInfo

internal fun getApplicationInfo(): String =
    """Version: ${System.getProperty(PropertiesSetup.APP_VERSION)}
       |${System.getProperty(PropertiesSetup.APP_BUILD_INFO)} 
       ------
       |OS: ${OsInfo.getName()}
       |OS Version: ${OsInfo.getVersion()}
       |OS Build: ${OsInfo.getBuildNumber()}
       ------
       |Java VM: ${System.getProperty("java.vm.name")}
       |Java VM version: ${System.getProperty("java.vm.version")}
       |Java VM vendor: ${System.getProperty("java.vm.vendor")}
       |Java version: ${System.getProperty("java.version")}
       |Java vendor: ${System.getProperty("java.vendor")}
       ------
       |JavaFX version: ${System.getProperty("javafx.version")}
       |JavaFX runtime version: ${System.getProperty("javafx.runtime.version")}
    """.trimMargin()
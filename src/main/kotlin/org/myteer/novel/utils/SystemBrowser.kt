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
package org.myteer.novel.utils

import org.myteer.novel.utils.os.OsInfo
import java.awt.Desktop
import java.net.URL

object SystemBrowser {
    private val browser: Browser

    init {
        browser = when {
            Desktop.isDesktopSupported() -> BaseBrowser()
            OsInfo.isWindows() -> WindowsBrowser()
            OsInfo.isLinux() -> LinuxBrowser()
            OsInfo.isMac() -> MacBrowser()
            else -> NullBrowser()
        }
    }

    fun browse(url: String) {
        browser.browse(url)
    }

    private class BaseBrowser : Browser {
        override fun browse(url: String) {
            Desktop.getDesktop().browse(URL(url).toURI())
        }
    }

    private class WindowsBrowser : Browser {
        override fun browse(url: String) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler $url")
        }
    }

    private class LinuxBrowser : Browser {
        override fun browse(url: String) {
            Runtime.getRuntime().exec("xdg-open $url")
        }
    }

    private class MacBrowser : Browser {
        override fun browse(url: String) {
            Runtime.getRuntime().exec("open $url")
        }
    }

    private class NullBrowser : Browser {
        override fun browse(url: String) {
        }
    }

    private interface Browser {
        fun browse(url: String)
    }
}
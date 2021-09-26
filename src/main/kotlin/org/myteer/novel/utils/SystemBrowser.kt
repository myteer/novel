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
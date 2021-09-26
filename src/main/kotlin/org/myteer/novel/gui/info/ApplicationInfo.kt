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
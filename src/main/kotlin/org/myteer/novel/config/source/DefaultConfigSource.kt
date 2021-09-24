package org.myteer.novel.config.source

import org.myteer.novel.main.PropertiesSetup
import java.io.File

class DefaultConfigSource : JsonFileConfigSource(File(System.getProperty(PropertiesSetup.CONFIG_FILE_PATH))) {
}
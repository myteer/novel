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
package org.myteer.novel.update

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import io.github.g00fy2.versioncompare.Version
import org.myteer.novel.main.PropertiesSetup
import java.net.URL
import java.util.function.Consumer

/**
 * Searches for the latest release in the given github repository
 * @param githubRepository the object representing the github repository
 * @param baseVersion the version the update-searcher should compare the release versions to
 */
class UpdateSearcher(private val githubRepository: GithubRepository, private val baseVersion: String) {
    /**
     * Searches for the latest github release without potentially throwing exceptions
     */
    fun trySearch(onException: Consumer<Exception>? = null): Release? {
        return try {
            search()
        } catch (e: Exception) {
            onException?.accept(e)
            null
        }
    }

    /**
     * Searches for the latest github release
     * @return the [Release], _null_ if there is no newer release
     */
    fun search(): Release? {
        JsonReader(URL(githubRepository.releasesApiUrl(1)).openStream().bufferedReader()).use { reader ->
            val releases: List<Release> = Gson().fromJson(reader, object : TypeToken<List<Release>>() {}.type)
            return releases.getOrNull(0)?.takeIf {
                Version(it.version!!.removePrefix("v")).isHigherThan(baseVersion.removePrefix("v"))
            }
        }
    }

    companion object {
        @JvmField
        val NOVEL_REPOSITORY = GithubRepository("myteer", "novel")

        @JvmStatic
        val default by lazy {
            UpdateSearcher(NOVEL_REPOSITORY, System.getProperty(PropertiesSetup.APP_VERSION))
        }
    }
}
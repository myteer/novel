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

import com.google.gson.annotations.SerializedName
import java.io.InputStream
import java.net.URL

/**
 * Represents an asset (a file) in a particular github release
 */
class ReleaseAsset {
    /**
     * The file name
     */
    var name: String? = null

    /**
     * The file size in bytes
     */
    var size: Long = 0

    /**
     * The url to download file
     */
    @SerializedName("browser_download_url")
    var downloadUrl: String? = null

    /**
     * The mime-type (or content type) of the file
     */
    @SerializedName("content_type")
    var contentType: String? = null

    val sizeInMB: Double
        get() = (size / 1024.0 / 1024.0 * 100).toInt() / 100.0

    /**
     * Opens an input-stream for downloading the asset's file
     */
    fun openInputStream(): InputStream = URL(downloadUrl).openStream()

    override fun toString(): String {
        return "GithubReleaseAsset(name=$name, size=$size, downloadUrl=$downloadUrl, contentType=$contentType)"
    }
}
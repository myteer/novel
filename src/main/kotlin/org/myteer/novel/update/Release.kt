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

/**
 * Represents a Github release
 */
class Release {
    /**
     * The website where this release can be viewed
     */
    @SerializedName("html_url")
    var website: String? = null

    /**
     * The version of the release (tag name)
     */
    @SerializedName("tag_name")
    var version: String? = null

    /**
     * Determines whether the release is a pre-release or a production-ready release
     */
    @SerializedName("prerelease")
    var isPrerelease: Boolean = false

    /**
     * The description of the release
     */
    @SerializedName("body")
    var description: String? = null

    /**
     * The assets of this release
     */
    var assets: List<ReleaseAsset>? = null
}
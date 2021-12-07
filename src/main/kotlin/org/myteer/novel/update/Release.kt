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
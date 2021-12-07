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
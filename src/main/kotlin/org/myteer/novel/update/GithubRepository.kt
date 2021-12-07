package org.myteer.novel.update

/**
 * Represents a Github repository
 */
class GithubRepository(val owner: String, val name: String) {
    fun releasesApiUrl(maxItems: Int = 30): String {
        return "https://api.github.com/repos/$owner/$name/releases?per_page=$maxItems"
    }
}
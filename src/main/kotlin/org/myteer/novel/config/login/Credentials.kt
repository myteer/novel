package org.myteer.novel.config.login

class Credentials(val username: String, val password: String) {
    val isAnonymous: Boolean
        get() = username.isBlank() && password.isBlank()

    companion object {
        fun anonymous() = Credentials("", "")
    }
}
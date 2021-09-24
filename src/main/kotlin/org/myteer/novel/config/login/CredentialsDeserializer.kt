package org.myteer.novel.config.login

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.jasypt.util.text.StrongTextEncryptor
import org.jasypt.util.text.TextEncryptor
import java.lang.reflect.Type

class CredentialsDeserializer : JsonDeserializer<Credentials> {
    companion object {
        private const val USERNAME = "cdt.une"
        private const val PASSWORD = "cdt.pwd"
        private const val ENCRYPTION_PASSWORD = "cdt.ecp.pwd"
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Credentials {
        return json?.let { buildCredentials(it.asJsonObject) } ?: Credentials.anonymous()
    }

    private fun buildCredentials(json: JsonObject): Credentials {
        val username = json.get(USERNAME)?.asString ?: ""
        var password = json.get(PASSWORD)?.asString ?: ""
        val ecpPwd = json.get(ENCRYPTION_PASSWORD)?.asString ?: ""
        if (password.isNotBlank() && ecpPwd.isNotBlank()) {
            password = buildEncryptor(ecpPwd).decrypt(password)
        }
        return Credentials(username, password)
    }

    private fun buildEncryptor(ecpPwd: String): TextEncryptor {
        return StrongTextEncryptor().apply {
            setPassword(ecpPwd)
        }
    }
}
package org.myteer.novel.config.login

import com.google.gson.*
import org.jasypt.util.text.StrongTextEncryptor
import org.jasypt.util.text.TextEncryptor
import java.lang.reflect.Type

class CredentialsSerializer : JsonSerializer<Credentials> {
    companion object {
        private const val USERNAME = "cdt.une"
        private const val PASSWORD = "cdt.pwd"
        private const val ENCRYPTION_PASSWORD = "cdt.ecp.pwd"
    }

    override fun serialize(src: Credentials?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (null == src || src.isAnonymous) {
            JsonNull.INSTANCE
        } else {
            buildJsonObject(src)
        }
    }

    private fun buildJsonObject(src: Credentials): JsonObject {
        val ecpPwd = Math.random().toString()
        val encryptor = buildEncryptor(ecpPwd)
        return JsonObject().apply {
            addProperty(USERNAME, src.username)
            addProperty(PASSWORD, encryptor.encrypt(src.password))
            addProperty(ENCRYPTION_PASSWORD, ecpPwd)
        }
    }

    private fun buildEncryptor(ecpPwd: String): TextEncryptor {
        return StrongTextEncryptor().apply {
            setPassword(ecpPwd)
        }
    }
}
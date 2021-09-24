package org.myteer.novel.config.login

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.myteer.novel.config.ConfigAdapter
import java.lang.reflect.Type

class LoginDataAdapter : ConfigAdapter<LoginData> {
    private val serializer = LoginDataSerializer()
    private val deserializer = LoginDataDeserializer()

    override fun serialize(src: LoginData?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return serializer.serialize(src, typeOfSrc, context)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LoginData {
        return deserializer.deserialize(json, typeOfT, context)
    }
}
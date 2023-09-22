package br.com.market.core.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class InterfaceAdapter<T> : JsonSerializer<T>, JsonDeserializer<T> {

    override fun serialize(src: T?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        requireNotNull(src) { "O objeto não pode ser null" }
        requireNotNull(context) { "O contexto não pode ser null" }

        val jsonObject = JsonObject()
        jsonObject.addProperty(CLASSNAME, src.javaClass.name)
        jsonObject.add(DATA, context.serialize(src))
        return jsonObject
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): T {
        requireNotNull(json) { "O elemento JSON não pode ser null" }
        requireNotNull(context) { "O contexto não pode ser null" }

        val jsonObject = json.asJsonObject
        val prim = jsonObject[CLASSNAME] as JsonPrimitive
        val className = prim.asString
        val klass = Class.forName(className)

        return context.deserialize(jsonObject[DATA], klass)
    }

    companion object {
        private const val CLASSNAME = "CLASSNAME"
        private const val DATA = "DATA"
    }
}
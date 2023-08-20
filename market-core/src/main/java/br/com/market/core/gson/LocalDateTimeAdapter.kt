package br.com.market.core.gson

import br.com.market.core.enums.EnumDateTimePatterns
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {

    private val formatter = DateTimeFormatter.ofPattern(EnumDateTimePatterns.DATE_TIME.pattern)

    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, dateTime: LocalDateTime?) {
        if (dateTime == null) {
            jsonWriter.nullValue()
        } else {
            jsonWriter.value(formatter.format(dateTime))
        }
    }

    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): LocalDateTime? {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull()
            return null
        }
        return LocalDateTime.parse(jsonReader.nextString(), formatter)
    }
}
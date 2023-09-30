package br.com.market.core.gson.adapter

import br.com.market.core.enums.EnumDateTimePatterns
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Adaptador Gson para serialização e desserialização de objetos [LocalDateTime].
 *
 * Este adaptador personalizado permite que objetos [LocalDateTime] sejam convertidos para JSON e vice-versa,
 * utilizando o formato de data e hora especificado pelo [EnumDateTimePatterns.DATE_TIME].
 *
 * @property formatter O [DateTimeFormatter] utilizado para a formatação de [LocalDateTime].
 * @constructor Cria uma instância de [LocalDateTimeAdapter].
 */
class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {

    private val formatter = DateTimeFormatter.ofPattern(EnumDateTimePatterns.DATE_TIME.pattern)

    /**
     * Escreve um objeto [LocalDateTime] como um valor JSON.
     *
     * @param jsonWriter O escritor JSON usado para gravar o valor.
     * @param dateTime O objeto [LocalDateTime] a ser escrito.
     * @throws IOException Em caso de erros de escrita.
     */
    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, dateTime: LocalDateTime?) {
        if (dateTime == null) {
            jsonWriter.nullValue()
        } else {
            jsonWriter.value(formatter.format(dateTime))
        }
    }

    /**
     * Lê um valor JSON e o converte para um objeto [LocalDateTime].
     *
     * @param jsonReader O leitor JSON usado para ler o valor.
     * @return O objeto [LocalDateTime] lido ou nulo se o valor JSON for nulo.
     * @throws IOException Em caso de erros de leitura.
     */
    @Throws(IOException::class)
    override fun read(jsonReader: JsonReader): LocalDateTime? {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull()
            return null
        }
        return LocalDateTime.parse(jsonReader.nextString(), formatter)
    }
}
package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.logging.*

object Serializer {

    private val logReader = jacksonObjectMapper()
        .registerKotlinModule()
        .readerFor(Log::class.java)
        .with(JsonParser.Feature.ALLOW_MISSING_VALUES)
        .with(JsonParser.Feature.ALLOW_SINGLE_QUOTES)

    private val logWriter = jacksonObjectMapper()
        .writerWithDefaultPrettyPrinter()


    fun<TSerializable: MockSerializable> serialize(log: TSerializable): String = logWriter.writeValueAsString(log)
    fun<TSerializable: MockSerializable> deserialize(json: String): TSerializable = logReader.readValue(json)
}

public interface MockSerializable {

}

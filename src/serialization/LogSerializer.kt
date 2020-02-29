package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.logging.*

public object LogSerializer {

    private val logReader = jacksonObjectMapper()
        .registerKotlinModule()
        .readerFor(Log::class.java)
        .with(JsonParser.Feature.ALLOW_MISSING_VALUES)
        .with(JsonParser.Feature.ALLOW_SINGLE_QUOTES)

    private val logWriter = jacksonObjectMapper()
        .writerWithDefaultPrettyPrinter()


    public fun serialize(log: Log): String = logWriter.writeValueAsString(this)
    public fun deserialize(json: String): Log = logReader.readValue(json)
}
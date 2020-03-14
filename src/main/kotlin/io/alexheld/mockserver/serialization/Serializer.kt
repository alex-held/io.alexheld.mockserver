package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.*


object Serializer {

    private val logReader = jacksonObjectMapper()
            .registerKotlinModule()
            .reader()
            .with(DeserializationFeature.WRAP_EXCEPTIONS, DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .without(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .with(JsonParser.Feature.ALLOW_COMMENTS)
            .with(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .with(JsonParser.Feature.ALLOW_SINGLE_QUOTES)


    private val logWriter = jacksonObjectMapper()
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        .registerKotlinModule()
        .writerWithDefaultPrettyPrinter()
        .with(JsonGenerator.Feature.IGNORE_UNKNOWN)
        .withFeatures(
            SerializationFeature.FAIL_ON_SELF_REFERENCES,
            SerializationFeature.INDENT_OUTPUT,
            SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .with(JsonGenerator.Feature.IGNORE_UNKNOWN)



    fun<TSerializable> serialize(log: TSerializable): String = logWriter.writeValueAsString(log)
    fun<TSerializable> deserialize(json: String): TSerializable = logReader.readValue<TSerializable>(json)!!
}


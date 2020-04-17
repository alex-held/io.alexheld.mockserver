package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*


object Yaml {

    fun serialize(value: Any): String =
        mapper.writerFor(value::class.java)
            .writeValueAsString(value)

    val mapper = YAMLMapper.builder()
        .disable(
            DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,
            DeserializationFeature.FAIL_ON_TRAILING_TOKENS,
            DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES,
            DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES
        )
        .enable(
            DeserializationFeature.WRAP_EXCEPTIONS,
            DeserializationFeature.READ_ENUMS_USING_TO_STRING
        ).enable(
            JsonParser.Feature.ALLOW_COMMENTS,
            JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
            JsonParser.Feature.ALLOW_SINGLE_QUOTES
        )
        .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
        .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
        .enable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        .enable(
            SerializationFeature.FAIL_ON_SELF_REFERENCES,
            SerializationFeature.INDENT_OUTPUT,
            SerializationFeature.WRITE_ENUMS_USING_TO_STRING
        )
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .findAndRegisterModules()
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY) as YAMLMapper
}

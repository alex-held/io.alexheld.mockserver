package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*



inline fun<reified T> Yaml.serialize(value: T) = Yaml.getWriterFor(T::class.java).writeValueAsString(value)

inline fun<reified T> Yaml.deserialize(yaml: String): T = Yaml.getReader(T::class.java).readValue(yaml)



object Yaml {

    private val mapper = YAMLMapper.builder().build()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .findAndRegisterModules()
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY) as YAMLMapper

    fun getReader(type: Class<*>) = mapper
        .reader()
        .without(
            DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,
            DeserializationFeature.FAIL_ON_TRAILING_TOKENS
        )
        .with(DeserializationFeature.WRAP_EXCEPTIONS,
            DeserializationFeature.READ_ENUMS_USING_TO_STRING)
        .with(JsonParser.Feature.ALLOW_COMMENTS)
        .with(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
        .with(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
        .forType(type)



    fun getWriterFor(type: Class<*>) = mapper
        .writerWithDefaultPrettyPrinter()
        .with(JsonGenerator.Feature.IGNORE_UNKNOWN)
        .with(YAMLGenerator.Feature.MINIMIZE_QUOTES)
        .without(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        .withFeatures(
            SerializationFeature.FAIL_ON_SELF_REFERENCES,
            SerializationFeature.INDENT_OUTPUT,
            SerializationFeature.WRITE_ENUMS_USING_TO_STRING
        )
        .without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .forType(type)
}

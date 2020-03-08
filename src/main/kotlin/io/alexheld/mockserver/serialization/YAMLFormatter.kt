package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import org.apache.logging.log4j.kotlin.*
import java.text.*
import java.util.*


object YAMLFormatter : Logging {

    public val mapper = YAMLMapper()
        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        //   .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
        .enable(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE)
        .enable(YAMLGenerator.Feature.SPLIT_LINES)
        .enable(YAMLGenerator.Feature.INDENT_ARRAYS)
        .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
        .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("en","DE")))
        .setTimeZone(TimeZone.getTimeZone("UTC"))
        //.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .registerKotlinModule()
        .disable(SerializationFeature.FAIL_ON_SELF_REFERENCES)
        .enable(DeserializationFeature.WRAP_EXCEPTIONS)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)!!


    public fun <T> serialize(serializable: T): String = mapper.writeValueAsString(serializable)
}

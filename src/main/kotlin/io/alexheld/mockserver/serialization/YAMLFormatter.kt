package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import org.apache.logging.log4j.kotlin.*
import java.text.*
import java.util.*


inline fun <reified T> YAMLFormatter.deserialize(yaml: String): T {
    return getMapper().readValue(yaml, T::class.java) as T
}

object YAMLFormatter : Logging {

    fun getMapper(): YAMLMapper {

        val mapper = YAMLMapper()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
            //   .enable(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE)
            .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("en", "DE")))
            .setTimeZone(TimeZone.getTimeZone("UTC"))
            .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
            .registerKotlinModule()
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)

        return mapper as YAMLMapper

    }

    fun <T> serialize(serializable: T): String = getMapper().writeValueAsString(serializable)
}

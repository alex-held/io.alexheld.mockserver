package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import org.apache.logging.log4j.kotlin.*
import java.text.*
import java.util.*


object YAMLFormatter : Logging {
//
//fun setup(){
//    val builder = DumpSettingsBuilder::class.java.newInstance()
//        .setDefaultScalarStyle(ScalarStyle.PLAIN)
//        .setIndent(4).setIndicatorIndent(2).build()
//  YAMLGenerator(IOContext(null!!, !!, true), )
//}
//    val factory = YAMLFactory.builder().enable(StreamReadFeature.IGNORE_UNDEFINED)
//


fun getMapper() : YAMLMapper {
     val mapper = YAMLMapper()
        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
        // .enable(YAMLGenerator.Feature.LITERAL_BLOC._STYLE)
        // .enable(YAMLGenerator.Feature.SPLIT_LINES)
        .enable(YAMLGenerator.Feature.INDENT_ARRAYS)
        .enable(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)
        .enable(MapperFeature.USE_ANNOTATIONS)
        .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("en","DE")))
        .setTimeZone(TimeZone.getTimeZone("UTC"))
        //.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .registerKotlinModule()
        .disable(SerializationFeature.FAIL_ON_SELF_REFERENCES)
        .setDefaultMergeable(true)
        .enable(DeserializationFeature.WRAP_EXCEPTIONS)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

      return mapper as YAMLMapper

}


    public fun <T> serialize(serializable: T): String = getMapper().writeValueAsString(serializable)
}
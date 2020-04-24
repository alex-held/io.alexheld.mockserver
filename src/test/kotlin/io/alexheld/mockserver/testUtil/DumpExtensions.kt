package io.alexheld.mockserver.testUtil

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import org.litote.kmongo.id.jackson.*

fun String.dump(name: String) {
    val size = name.toList().joinToString("-") { _ -> "" }

    println("--- $name ---")
    println(this)
    println("----$size----")
}


fun Any.dumpAsJson(name: String) {
    val size = name.toList().joinToString("-") { _ -> "" }
    val json = jacksonObjectMapper()
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(JavaTimeModule())
        .registerModule(IdJacksonModule())
        .registerKotlinModule()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(this)
        .dump(name)

    println("--- $name ---")
    println(json)
    println("----$size----")
}


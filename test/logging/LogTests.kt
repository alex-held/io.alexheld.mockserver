package io.alexheld.mockserver.logging

import com.fasterxml.jackson.module.kotlin.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class LogTests {

    @Test
    fun should() {
        val log = Log()
        val mapper = jacksonObjectMapper().registerKotlinModule()

        val json = mapper.writeValueAsString(log)

        println(json)
        assertNotNull(json)
    }
}

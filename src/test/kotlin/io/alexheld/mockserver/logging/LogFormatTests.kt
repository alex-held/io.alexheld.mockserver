package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import org.junit.jupiter.api.*

class LogFormatTests {

    @Test
    fun `should pretty format log`(){

        // Arrange
        val log = Log.setupCreated(Setup(request = Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"), Pair("Content-Length","100")))))

        // Act
        val formatted = log.format()

        println(formatted)
    }
}

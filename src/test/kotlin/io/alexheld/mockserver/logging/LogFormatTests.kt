package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*


class LogFormatTests {

    @Test
    fun `should format setup created`() {
        // Arrange
        val log = Log.setupCreated(setup = Setup(request = Request(method = "PUT", path = "/api/cars"), action = Action(message = "response message")))

        // Act
        val formatted = log.format()

        println(formatted)
        formatted.shouldContainAll("PUT", "/api/cars", "created", "request", "action", "response message")
    }

    @Test
    fun `should format received request`() {
        // Arrange
        val log = Log.requestReceived(Request(method = "PUT", path = "/api/cars"))

        // Act
        val formatted = log.format()

        println(formatted)
        formatted.shouldContainAll("PUT", "/api/cars", "received request")
    }


    @Test
    fun `should format Match`() {

        // Arrange
        val expected = """
            returning response:

            {
              "message" : "DEFAULT RESPONSE MESSAGE",
              "statusCode" : 200
            }

             for request:

            {
              "method" : "POST",
              "path" : "/some/path"
            }""".trimIndent()

        val request = Request(method = "POST", path = "/some/path")
        val action = Action("DEFAULT RESPONSE MESSAGE", 200)
        val requestSetup = Request(method = "POST")
        val setup = Setup(request = requestSetup, action = action)
        val log = Log.matched(request, setup)

        // Act
        val formatted = log.format()

        // Assert
        formatted.shouldBeEqualWhenTrimmed(expected)
    }
}



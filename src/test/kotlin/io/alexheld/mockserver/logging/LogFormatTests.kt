package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import io.ktor.http.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*

class LogFormatTests {

    @Test
    fun `should format Match`(){

        // Arrange
        val expected = """
            returning response:

            {
              "message" : "DEFAULT RESPONSE MESSAGE",
              "statusCode" : {
                "value" : 200,
                "description" : "OK"
              }
            }

             for request:

            {
              "method" : "POST",
              "path" : "/some/path",
              "headers" : null,
              "cookies" : null,
              "body" : null
            }""".trimIndent()
        val request = Request(method = "POST", path = "/some/path")
        val action = Action("DEFAULT RESPONSE MESSAGE", HttpStatusCode.OK)
        val requestSetup = Request(method = "POST")
        val setup = Setup(request = requestSetup, action = action)
        val log = Log.matched(request, setup)

        // Act
        //  LogFormatter.formatLogMessage("returning response:{}for request:{}", arrayOf( action, request))
        val formatted = log.format()

        // Assert
        println(formatted)
        formatted.trim(' ').trim('\n').shouldBeEqualTo(expected)
    }
}

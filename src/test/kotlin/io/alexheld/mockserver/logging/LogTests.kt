package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import org.amshove.kluent.*
import org.apache.logging.log4j.*
import org.junit.jupiter.api.*
import java.util.*


class LogTests {

    @Test
    fun should() {

        // Arrange
        val log = Log.matched(request = Request(method = "PUT"), setup = Setup(request = Request(method = "PUT")))

        // Act
        val json = log.format()
        println(json)

        val deserializedLog = Log("msadf", LogMessageType.Operation) // Serializer.deserialize<Log>(json)

        // Assert
        deserializedLog.id.shouldNotBeNullOrBlank().length.shouldBeEqualTo(36)
        deserializedLog.timestamp.before(Date()).shouldBeTrue()
        deserializedLog.level.shouldBeEqualTo(Level.INFO)
    }
}

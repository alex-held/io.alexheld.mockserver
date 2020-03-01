package io.alexheld.mockserver.logging

import io.alexheld.mockserver.logging.Log.*
import io.alexheld.mockserver.serialization.*
import io.kotlintest.*
import io.kotlintest.matchers.types.*
import org.junit.jupiter.api.*
import org.slf4j.event.*
import java.time.*
import java.util.*


class LogTests {

    @Test
    fun should() {

        // Arrange
        val log = Log(
            id = UUID.fromString("2a956c9d-da4b-4c0a-bcf2-d37d026ba9a").asLogId(),
            timestamp = Date.from(Instant.EPOCH),
            level = Level.WARN,
            type = LogMessageType.EXPECTATION_MATCHED
        )

        // Act
        val json = LogSerializer.serialize(log)

        println(json)

        val deserializedLog = LogSerializer.deserialize(json)

        // Assert
        deserializedLog.id.id.shouldBe(UUID.fromString("2a956c9d-da4b-4c0a-bcf2-d37d026ba9a"))
        deserializedLog.timestamp.shouldBe(Date.from(Instant.EPOCH))
        deserializedLog.level.shouldBeNull()
    }
}

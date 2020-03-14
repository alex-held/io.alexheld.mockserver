package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.serialization.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import java.time.*


class LogTests {

    @Test
    fun should() {

        // Arrange
        val log = YamlLog.matched(request = Request(method = "PUT"), setup = Setup(request = Request(method = "PUT")))

        // Act
        val yaml = YAMLFormatter.serialize(log)
        println(yaml)

        val deserializedLog = YAMLFormatter.getMapper().readValue(yaml, YamlLog::class.java)

        // Assert
        deserializedLog.id.shouldNotBeNullOrBlank().length.shouldBeEqualTo(36)
        Instant.parse(deserializedLog.timestamp).isBefore(Instant.now()).shouldBeTrue()
    }
}

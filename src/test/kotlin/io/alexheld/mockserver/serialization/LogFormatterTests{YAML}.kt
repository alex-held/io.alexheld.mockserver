package io.alexheld.mockserver.serialization

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import java.io.*
import java.time.*

class LogFormatterTests {

    private val root = "/serialization/yaml"
    private fun streamYamlFile(fileName: String): InputStream = this::class.java.getResourceAsStream("$root/$fileName.yaml")

    private fun readYaml(file: String): String {
        val stream = streamYamlFile(file)
        val reader = InputStreamReader(stream)
        return reader.readText()
    }

    @Test
    fun `should format Match`() {

        // Arrange
        val expectedYaml = readYaml("Request_Matched")

        // Act
        val log = YAMLFormatter.getMapper().readValue(expectedYaml, Log::class.java)
        val yaml = YAMLFormatter.serialize(log)

        // Assert
        yaml.shouldBeEqualWhenTrimmed(expectedYaml)
    }


    @Test
    fun `should format Setup_Created`() {

        // Arrange
        val expectedYaml = readYaml("Setup_Created")

        // Act
        val log = YAMLFormatter.getMapper().readValue(expectedYaml, YamlDocument::class.java)
        val yaml = YAMLFormatter.serialize(log)

        // Assert
        yaml.shouldBeEqualWhenTrimmed(expectedYaml)
    }

    @Test
    fun `should format logtemplate`() {

        val expectedLog = YamlLogDocument(
            mapOf(
                "apiVersion" to "1.0",
                "kind" to "Log",
                "type" to LogMessageType.Setup_Created.type,
                "id" to "134",
                "timestamp" to Instant.EPOCH.toString()
            )
        ).withNamed("events", Log.requestReceived(Request(method = "GET", path = "/api/some/path")))

        val yaml = YAMLFormatter.serialize(expectedLog)
        yaml.dump("yaml")

        val log = YAMLFormatter.deserialize<YamlLogDocument>(yaml)
        log.apiVersion.version.shouldBeEqualTo("1.0")

    }


    @Test
    fun `should add child`() {

        // Arrange
        Generator.enableDebugGeneration = true
        val expectedLog = YamlLogDocument()
            .withNamed("events", Log.requestReceived(Request(path = "/api/a/b")))
            .withNamed(
                "events", Log.requestMatched(
                    Setup(
                        request = Request(path = "/api/a/b", method = "OPTIONS"),
                        action = Action("hello world")
                    )
                )
            )
            .withNamed("events", Log.action(Action("hello world")))


        // Act
        val yaml = YAMLFormatter.serialize(expectedLog)
        yaml.dump("yaml")
        val log = YAMLFormatter.deserialize<YamlLogDocument>(yaml)

        // Assert
        log.id.shouldBeEqualTo(Generator.getId())
    }
}

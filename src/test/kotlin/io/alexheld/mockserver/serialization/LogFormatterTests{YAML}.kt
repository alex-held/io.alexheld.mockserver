package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.testUtil.*
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
        val log = YAMLFormatter.getMapper().readValue(expectedYaml, YamlLog::class.java)
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

        val document = YamlLog(
            mapOf(
                "apiVersion" to "1.0",
                "kind" to "Log",
                "type" to LogMessageType.Setup_Created.type,
                "id" to "134",
                "timestamp" to Instant.EPOCH.toString(),
                "events" to listOf(
                    LogNode(
                        mapOf(
                            "type" to LogMessageType.Request_Received.type,
                            "id" to "00000000-0000-0000-0000-000000000000",
                            "timestamp" to Instant.EPOCH.toString()
                        )
                    )
                )
            )
        )

        // 1. Request
        document.dump()


    }


    @Test
    fun `should add child`() {

        val matched = LogNode()
        matched.timestamp = Instant.EPOCH.toString()
        matched.event = LogMessageType.Request_Matched.type
        matched.id = "00000000-0000-0000-0000-000000000000"

        val test = YamlDocument()
        test.apiVersion = "1.0"
        //test.kind ="Log"
        test.id = "00000000-0000-0000-0000-000000000000"
        test.timestamp = Instant.EPOCH.toString()
        test.properties["events"] = mutableListOf(matched)

        val mapper = YAMLMapper
            .builder()
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .build()
            .registerKotlinModule()

        val yaml = mapper.writeValueAsString(test)

        println(yaml)
    }


    fun Node.dump() {
        val yaml = YAMLFormatter.serialize(this)
        println(yaml)
    }


}


class Node2Tests {

    companion object {
        val MAPPER: YAMLMapper = YAMLMapper
            .builder()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .build()
    }

    class YamlDoc : Node() {

        var apiVersion: String by properties
        var kind: String by properties

        // var events: NodeMap by properties
        var events: MutableList<Node> by properties
    }


    @Test
    fun should_serialize() {
        val document = YamlDoc()
        document.apiVersion = "1.0"
        document.kind = "Log"
        document.events = mutableListOf(Node(mutableMapOf(Pair("id", "1234"))))
        val yaml = MAPPER.writeValueAsString(document)

        yaml.dump("yaml")
    }


    fun String.dump(name: String) {
        val size = name.toList().map { c -> "" }.joinToString("-")

        println("--- $name ---")
        println(this)
        println("----$size----")
    }

}




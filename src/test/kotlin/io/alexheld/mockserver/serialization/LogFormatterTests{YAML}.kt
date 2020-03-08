package io.alexheld.mockserver.serialization

import ContainerNode
import YamlDocument
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.testUtil.*
import org.junit.jupiter.api.*
import java.io.*

public class LogFormatterTests {

    private val root = "/serialization/yaml"
    private fun streamYamlFile(fileName: String) : InputStream = this::class.java.getResourceAsStream("$root/$fileName.yaml")

    private fun readYaml(file: String) : String {
        val stream = streamYamlFile(file)
        val reader = InputStreamReader(stream)
        return reader.readText()
    }

    @Test
    fun `should format Match`() {

        // Arrange
        val stream = streamYamlFile("Request_Matched")
        val reader = InputStreamReader(stream)
        val expectedYaml = reader.readText()


        // Act
        val log = YAMLFormatter.getMapper().readValue(expectedYaml, Log::class.java)
        val yaml = YAMLFormatter.serialize(log)

        // Assert
        yaml.shouldBeEqualWhenTrimmed(expectedYaml)
    }



    @Test
    fun `should format Setup_Created`(){

        // Arrange
        val yaml = readYaml("Setup_Created")

        val expected = """
message: "successfully created setup"
type: "Setup_Created"
timestamp: "2020-03-08T09:22:15Z"
setup:
  request:
    method: "PUT"
    path: "/api/cars"
  action:
    message: "response message"
    statusCode: 404""".trimIndent()

        // Act
        yaml.shouldBeEqualWhenTrimmed(expected)
        // Act
//        val yamlSource = YAMLFormatter.serialize(log)
//        yamlSource.shouldBe(expected)
    }

    @Test
    fun `should format logtemplate`() {

        val document = YamlDocument(
            "1.0",
            "Log",
            "Setup_Created",
            id = "134",
            timestamp = "1970-01-01T00:00:00Z")

        document.dump()

        // 1. Request
        val requestReceivedNode = ContainerNode(LogMessageType.Request_Received.type,
            "00,000000-0000-0000-0000-000000000000","1970-01-01T00:00:00Z"
        )

        document.addChild(requestReceivedNode)

        document.dump()


    }



    fun YamlDocument.dump(){
        val yaml = YAMLFormatter.serialize(this)
        println(yaml)
    }



}

package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.documents.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.constructor.*
import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.*
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


    @Test
    fun `should serialize YamlFile`() {

        // Arrange
        Generator.enableDebugGeneration = true

        val listLogs = YamlLogDocument()
            .withNamed("events", Log.listLogs())

        val listSetups = YamlLogDocument()
            .withNamed("events", Log.listSetups())

        val createdLog = YamlLogDocument()
            .withNamed(
                "events", Log.setupCreated(
                    Setup(
                        request = Request(path = "/api/a/b", method = "OPTIONS"),
                        action = Action("hello world")
                    )
                )
            )

        val matchedRequest = YamlLogDocument.match(
            Request(path = "/api/a/b"), Setup(
                request = Request(path = "/api/a/b", method = "OPTIONS"),
                action = Action("hello world")
            )
        )

        val doc = YamlSetupDocument(
            mutableListOf(
                listLogs,
                listSetups,
                createdLog,
                matchedRequest
            )
        )


        // Act
        val sb = StringWriter()
        val writer = YAMLMapper.builder()
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
            .build()
            .registerKotlinModule()
            .writer()
            .writeValues(sb)

        for (document in doc.documents) {
            writer.write(document)

        }
        writer.close()

        val yaml = sb.toString()
        yaml.dump("YamlSetupDocument")

        //val log = YAMLFormatter.deserialize<YamlLogDocument>(yaml)

        // Assert
        //log.id.shouldBeEqualTo(Generator.getId())
    }


    @Test
    fun `should serialize Document`() {

        // Arrange
        Generator.enableDebugGeneration = true

        val listLogs = Log.listLogs()
        val listSetups = Log.listSetups()
        val createdLog = Log.setupCreated(
            Setup(
                request = Request(path = "/api/a/b", method = "OPTIONS"),
                action = Action("hello world")
            )
        )

        val matchedRequest = Log.action(Action("hello world"))

        val doc = Document(
            mutableListOf(
                listLogs,
                listSetups,
                createdLog,
                matchedRequest
            )
        )

        val yamlLogDocument = YamlLogDocument
            .match(
                Request(path = "/api/some/path"), Setup(
                    request = Request(path = "/api/some/path", method = "OPTIONS"),
                    action = Action("hello world")
                )
            )

        // Act
        val map = yamlLogDocument.toMap()

        val maps = mutableListOf(map, map, map)

        val constructor = Constructor()
        constructor.addTypeDescription(TypeDescription(Log::class.java, Tag("!log")))
        val represent = Representer()
        represent.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        represent.addClassTag(Log::class.java, Tag.STR)

        val options = DumperOptions()
        options.isPrettyFlow = true
        options.isExplicitStart = true
        options.indent = 4
        options.defaultScalarStyle = DumperOptions.ScalarStyle.LITERAL
        val y = Yaml()

        val sb = StringWriter()
        y.setBeanAccess(BeanAccess.FIELD)
        y.dumpAll(iterator {
            for (m in maps)
                yield(m)
        }, sb)


        // writer.close()


        /*
         val sb = StringWriter()
            val writer = YAMLFormatter.getMapper()
                .writer()
            writeMap(map, writer, sb)
           */


        val yaml = sb.toString()
        //val yaml = YAMLFormatter.serialize(map)


        yaml.dump("Document<YamlLogDocument>")

        //val log = YAMLFormatter.deserialize<YamlLogDocument>(yaml)

        // Assert
        //log.id.shouldBeEqualTo(Generator.getId())


/*
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = "Silenthand Olleander"
        data["race"] = "Human"
        data["traits"] = arrayOf("ONE_HAND", "ONE_EYE")

        val representer = Representer()
        val options = DumperOptions()

        representer.addClassTag(Log::class.java, Tag("!io.alexheld.mockserver.documents.Document"))*/
    }


    fun writeMap(map: Map<String, Any>, writer: ObjectWriter, output: StringWriter) {

        val primetives = map.toMutableMap()
        val list = map.filter { v ->
            if (v.component2() is Iterable<*>) {
                primetives.remove(v.component1())
                return@filter true
            }
            return@filter false
        }.mapValues {
            return@mapValues it.component2() as Iterable<Any>
        }

        val nested = map
            .filter { v ->
                if (v.component2() is Map<*, *>) {
                    primetives.remove(v.component1())
                    return@filter true
                }
                return@filter false
            }.mapValues {
                return@mapValues it.component2() as Map<String, Any>
            }

        for (prime in primetives)
            writer.writeValue(output, prime)

        for (item in list)
            writer.writeValue(output, item)

        if (nested.isNotEmpty())
            writeMap(nested, writer, output)
    }
}

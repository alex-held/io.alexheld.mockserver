package io.alexheld.mockserver.serialization.yaml

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.serialization.*
import io.alexheld.mockserver.testUtil.*
import org.junit.jupiter.api.*
import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.DumperOptions.*
import org.yaml.snakeyaml.constructor.*
import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.nodes.Tag
import java.time.*
import java.util.regex.*
import org.yaml.snakeyaml.Yaml as SnakeYaml


class MockServerRepresenterTests {

    fun generateSubject(): Iterator<YamlLogDocument> = iterator {
        Generator.enableDebugGeneration = true
        while (true) {
            yield(
                YamlLogDocument
                    .match(
                        Request(path = "/api/some/path"), Setup(
                            request = Request(path = "/api/some/path", method = "OPTIONS"),
                            action = Action("hello world")
                        )
                    )
            )
        }

    }


    fun generateRequestMatched(count: Int = 1): Iterator<RequestMatched> = iterator {
        Generator.enableDebugGeneration = true
        for (i in 0..1) {
            val subject = RequestMatched(
                mutableMapOf(
                    "event" to LogMessageType.Request_Matched,
                    "id" to "00000000-0000-0000-0000-000000000000",
                    "timestamp" to Instant.EPOCH.toString(),
                    "events" to mutableListOf(
                        Request(method = "GET", path = "/some/path"),
                        Setup(request = Request(method = "GET"), action = Action("Hello World")),
                        Action("Hello World")
                    )
                )
            )
            Generator.enableDebugGeneration = false
            yield(subject)
        }
    }

    @Test
    fun canSerializeKotlinx() {

        // Arrange
        val subject = YamlLogDocument()
        subject.apiVersion = "3.5"
        subject.id = "123"
        subject.kind = "Log"
        subject.children + mutableMapOf("logs" to listOf(Log.listLogs()))

        // Act
        val yaml = Yaml.dump(subject)

        // Assert
        yaml.dump("with logs as children")

    }


    @Test
    fun canSerializeYamlLogDocument() {
        // Arrange
        val subject = Log.listLogs()

        // Act
        val yaml = Yaml.dump(subject)

        // Assert
        yaml.dump("listLogs")
    }


    @Test
    fun canSerializeKotlinx2() {

        // Arrange
        val subject = YamlLogDocument()
        subject.apiVersion = "3.5"
        subject.id = "1"
        subject.kind = "Log"

        subject.properties.putAll(
            mapOf(
                "type" to LogMessageType.Setup_Created,
                "setup" to Setup(request = Request(method = "POST"), action = Action("Hello World!"))
            )
        )

        // Act
        val yaml = Yaml.dump(subject)

        // Assert
        yaml.dump("YamlFile")

    }


    @Test
    fun canSerializeKotlinx3() {

        // Arrange
        val subject = generateRequestMatched().next()

        // Act
        val yaml = Yaml.dump(subject)


        // Assert
        yaml.dump("RequestMatched")
    }


    fun take(count: Int = 1): YamlLogDocument = generateSubject().asSequence().first()
}



object Yaml {

    fun dump(type: Class<*>, any: Any): String = dump(type)
    fun dump(any: Any): String = getMapper(any::class.java).dumpAsMap(any)

    fun getMapper(type: Class<*>): SnakeYaml {

        val y = SnakeYaml(
            buildConstructor(type),
            MockServerRepresentation(),
            buildDumpOptions()
        )

        val delegatingNodeDescription = TypeDescription(DelegatingNode::class.java, DelegatingNode::class.java)
        delegatingNodeDescription.setExcludes("properties", "children")

        y.addTypeDescription(TypeDescription(LogMessageType::class.java, Tag.STR))
        y.addTypeDescription(TypeDescription(RequestReceived::class.java, Tag.MAP))
        y.addTypeDescription(delegatingNodeDescription)
        y.addImplicitResolver(Tag.STR, Pattern.compile("\\*id001"), null)

        y.setBeanAccess(BeanAccess.FIELD)

        return y
    }

    fun buildConstructor(type: Class<*>): Constructor = Constructor(type)

    fun buildDumpOptions(indent: Int = 2): DumperOptions {
        val opt = DumperOptions()
        opt.defaultFlowStyle = FlowStyle.BLOCK
        opt.indicatorIndent = indent
        opt.indent = indent + 2
        return opt
    }
}


inline fun <reified T> Yaml.buildConstructor(): Constructor = buildConstructor(T::class.java)
inline fun <reified T> Yaml.dumpAs(any: Any): String = dump(T::class.java, any)
inline fun <reified T> Yaml.getMapper(): org.yaml.snakeyaml.Yaml = getMapper(T::class.java)








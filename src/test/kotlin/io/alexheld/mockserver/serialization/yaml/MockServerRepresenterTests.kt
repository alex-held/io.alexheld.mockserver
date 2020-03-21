package io.alexheld.mockserver.serialization.yaml

import com.fasterxml.jackson.databind.json.*
import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.serialization.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.DumperOptions.*
import org.yaml.snakeyaml.constructor.*
import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.nodes.Tag
import java.time.*
import org.yaml.snakeyaml.Yaml as SnakeYaml


class MockServerRepresenterTests {

    fun generateSubject(): Iterator<YamlLogDocument> = iterator {
        Generator.enableDebugGeneration = true
        while (true) {
            yield(
                YamlLogDocument
                    .match(
                        Request(path = "/api/some/path"), Setup(1, Instant.EPOCH,
                            Request(path = "/api/some/path", method = "OPTIONS"),
                            Action("hello world")
                        )
                    )
            )
        }

    }


    fun generateRequestMatched(count: Int = 1): Iterator<RequestMatchedLog> = iterator {
        Generator.enableDebugGeneration = true
        for (i in 0..1) {
            val subject = RequestMatchedLog(
                "00000000-0000-0000-0000-000000000000", Instant.EPOCH,
                Request(method = "GET", path = "/some/path"),
                Setup(1, Instant.EPOCH, Request(method = "GET"), Action("Hello World"))
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
                "setup" to Setup(1, Instant.EPOCH, Request(method = "POST"),  Action("Hello World!"))
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

    @Test
    fun canRequestSerializeRequestMatchedModel() {


        // Arrange
        val subject = RequestMatchedLog(
            "1234", Instant.EPOCH,
            Request(method = "POST"), Setup(1234, Instant.EPOCH, Request(method = "POST", path = "/some/api"), Action(message = "Hello World!",
                statusCode = 202))
        )


        // Act
        val y = org.yaml.snakeyaml.Yaml()
        val yaml = y.dumpAsMap(subject)

        // Assert
        yaml.dump("DictLog")
    }




    @Test
    fun canRequestSerializeSetupCreated() {

        // Arrange
        val subject = SetupCreatedLog("1234", Instant.EPOCH,
            Setup( 1234,Instant.EPOCH,  Request(method = "POST", path = "/some/api"), Action(message = "Hello World!", statusCode = 202))
        )

        // Act
        val y = org.yaml.snakeyaml.Yaml(MockServerConstruction(), SetupRepresentation())
        val yaml = y.dumpAsMap(subject)

        // Assert
        yaml.dump("SetupCreatedLogDTO")
    }

    @Test
    fun canRequestSerializeRequestMatchedLog() {

        // Arrange
        val requestReceived = Request(method = "POST")
        val setup = Setup(1234, Instant.EPOCH, Request(method = "POST", path = "/some/api"), Action(message = "Hello World!", statusCode = 202))

        val subject = RequestMatchedLog("1234", Instant.EPOCH, requestReceived, setup)

        // Act
        val y = org.yaml.snakeyaml.Yaml(SetupRepresentation())
        val yaml = y.dumpAsMap(subject)

        // Assert
        yaml.dump("RequestMatchedLog")
    }

    @Test
    fun canRequestDeserializeSetupCreated() {

        // Arrange
        val expected = SetupCreatedLog("1", Instant.EPOCH, Setup(1,Instant.EPOCH,
                Request(method = "POST", path = "/some/api"),
                Action(message = "Hello World!", statusCode = 202))
        )

        val yaml = """
id: '1'
type: Setup_Created
timestamp: '1970-01-01T00:00:00Z'
setup:
  id: 1234
  request:
    method: POST
    path: /some/path/123
  action:
    message: Some response Message
    statusCode: 102
""".trimIndent()

        // Act
        val ctor = MockServerConstruction()
        val td = TypeDescription(Setup::class.java)
        td.tag =  Tag("!setup")
        ctor.addTypeDescription(td)

        val y = org.yaml.snakeyaml.Yaml(ctor, SetupRepresentation())
        val actual = y.load<SetupCreatedLog>(yaml)

        // Assert
        actual.id.shouldBeEqualTo(expected.id)
        actual.timestamp.shouldBeEqualTo(expected.timestamp)
        actual.type.shouldBeEqualTo(expected.type)

        val json = JsonMapper.builder().build().registerKotlinModule()
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(actual)

        json.dump("JSON")
        y.dumpAsMap(actual).dump("YAML")
        actual.setup.action.statusCode.shouldBeEqualTo(102)
    }
}





class MockServerConstruction : Constructor() {

    init {
        propertyUtils.isSkipMissingProperties = true
        propertyUtils.setBeanAccess(BeanAccess.FIELD)

        addTypeDescription(TypeDescription(Setup::class.java, Tag("!setup")))
        typeTags[Tag("!setup")] = Setup::class.java
        addTypeDescription(TypeDescription(Action::class.java, Tag.MAP))
        addTypeDescription(TypeDescription(Request::class.java, Tag.MAP))
        addTypeDescription(TypeDescription(RequestBody::class.java, Tag.MAP))

        addTypeDescription(TypeDescription(RequestMatchedLog::class.java, Tag.MAP))
        addTypeDescription(TypeDescription(SetupCreatedLog::class.java, SetupCreatedLog::class.java))

        this.yamlConstructors[Tag("!setup")] = SetupConstruct()
        this.yamlConstructors[Tag(Setup::class.java)] = SetupConstruct()
    }


inner class SetupConstruct : Construct {

    override fun construct2ndStep(node: Node?, `object`: Any?) {
        println(`object`)
    }

    override fun construct(node: Node?): Any {
       val a = data

       if (node is MappingNode)
       {
           val b = this@MockServerConstruction.getClassForNode(node)

       }
        return Setup()
    }

}

    private fun resolveMembers(mappingNode: MappingNode): Map<String, Any> {

        val memberNodes = mappingNode.value.map { node ->
            val keyNode = node.keyNode as ScalarNode
            val valueNode = node.valueNode

            return@map Pair(keyNode.value, valueNode)
        }.toMap().toMutableMap()

        val members = memberNodes.map { member ->
            val name = member.key

            return@map Pair(
                name, when (val node = member.value) {
                    is MappingNode -> resolveMembers(node)
                    is ScalarNode -> node.value
                    is SequenceNode -> node.value
                    is CollectionNode<*> -> node.value
                    else -> ""
                }
            )
        }

        return members.toMap()
    }

    override fun constructObject(node: Node?): Any {

        if (node !is MappingNode)
            throw UnsupportedOperationException()

        val typeValue = node.value.firstOrNull { map ->
            (map.keyNode as? ScalarNode)?.value == "type"
        }?.valueNode as? ScalarNode


        val type = enumValueOf<LogMessageType>(typeValue!!.value)

        try {

            val properties = node.value.map {
                val key = (it.keyNode as ScalarNode).value

                return@map when (val value = it.valueNode) {
                    is MappingNode -> Pair(key, resolveMembers(node))
                    is SequenceNode -> Pair(key, value.value)
                    is ScalarNode -> Pair(key, value.value)
                    else -> throw UnsupportedOperationException()
                }
            }.toMap()

            val instance = type.createInstance(properties)

            return instance
        } catch (e: Exception) {
            println("$[{MockServerConstruction::class.simpleName}]\t${e.localizedMessage}")
        }

        throw UnsupportedOperationException()
    }




    @Test fun shouldSetSetupData(){

        // Arrange
        val setup = Setup(1, Instant.EPOCH, Request(method="GET"), Action("Hello World!"))
        val subject = SetupCreatedLog("1234", Instant.EPOCH)

        // Act
        subject.setup = setup
        subject.setup.shouldNotBeNull()
        subject.timestamp.epochSecond.shouldBeEqualTo(Instant.EPOCH.epochSecond)
        subject.setup.id.shouldBeEqualTo("1")
        subject.id.shouldBeEqualTo("1234")
    }
}


/*

class RequestMatchedLog(val id: String, val timestamp: Instant, val request: Request, setup: Setup) : HashMap<String, Any>(
    mapOf(
        "id" to id,
        "timestamp" to timestamp.toString(),
        "events" to listOf(
            request,
            setup,
            setup.action
        )
    )
)
*/

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
        y.addTypeDescription(delegatingNodeDescription)
        y.setBeanAccess(BeanAccess.FIELD)

        return y
    }


    fun buildConstructor(type: Class<*>): Constructor {
        val c = Constructor(type)
        c.isAllowDuplicateKeys = false
        val propertyUtils = c.propertyUtils
        propertyUtils.isSkipMissingProperties = true
        return c
    }


    fun buildDumpOptions(indent: Int = 2): DumperOptions {
        val opt = DumperOptions()
        opt.defaultFlowStyle = FlowStyle.BLOCK
        opt.indicatorIndent = 2
        opt.indent = indent + 2

        return opt
    }
}


inline fun <reified T> Yaml.buildConstructor(): Constructor = buildConstructor(T::class.java)
inline fun <reified T> Yaml.dumpAs(any: Any): String = dump(T::class.java, any)

inline fun <reified T> Yaml.getMapper(): org.yaml.snakeyaml.Yaml = getMapper(T::class.java)

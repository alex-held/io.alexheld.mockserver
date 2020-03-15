package io.alexheld.mockserver.serialization.yaml

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.serialization.*
import io.alexheld.mockserver.testUtil.*
import org.apache.tools.ant.taskdefs.*
import org.junit.jupiter.api.*
import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.DumperOptions.*
import org.yaml.snakeyaml.constructor.*
import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.introspector.Property
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.*
import java.time.*
import java.util.*
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
                linkedMapOf(
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

        // Act
        val yaml = Yaml.dump(subject)

        // Assert
        yaml.dump("YamlFile")

    }


    @Test
    fun canSerializeKotlinx2() {

        // Arrange
        val subject = YamlLogDocument()
        subject.apiVersion = "3.5"
        subject.id = "1"
        subject.kind = "Log"
        /*subject.events = mutableListOf(mutableMapOf(
            "type" to LogMessageType.Setup_Created,
            "setup" to Setup(request = Request(method = "POST"), action = Action("Hello World!"))
        ))*/

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

    fun dump(type: Class<*>, any: Any): String = getMapper(type).dumpAsMap(any)
    fun dump(any: Any): String = getMapper(any::class.java).dumpAsMap(any)

    fun getMapper(type: Class<*>): SnakeYaml {

        val y = SnakeYaml(
            buildConstructor(type),
            MockServerRepresentation(),
            buildDumpOptions()
        )

        y.addTypeDescription(TypeDescription(LogMessageType::class.java, Tag.STR))
        y.addTypeDescription(TypeDescription(RequestReceived::class.java, Tag.MAP))

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


class MockServerConstructor(val rootType: Class<*>, val classloader: Classloader) : Constructor(rootType) {
    constructor(classloader: Classloader) : this(Objects::class.java, classloader)

    private var loader = CustomClassLoaderConstructor::class.java.classLoader


    override fun createDefaultList(initSize: Int): List<Any?>? {
        return LinkedList()
    }

    override fun getClassForName(name: String?): Class<*>? {
        return Class.forName(name, true, loader)
    }
}


class MockServerRepresentation : Representer() {

    init {
        this.defaultFlowStyle = FlowStyle.BLOCK
        this.addClassTag(RequestMatched::class.java, Tag.MAP)
        this.addClassTag(Request::class.java, Tag.MAP)
        this.addClassTag(Setup::class.java, Tag.MAP)
        this.addClassTag(Action::class.java, Tag.MAP)

        this.representers[LogMessageType::class.java] = PresentLogMessageType()
        this.representers[DelegatingNode::class.java] = RepresentDelegatingNode()
    }


    private fun getPropertyPosition(property: String): Int {
        return when (property) {
            "apiVersion" -> 0
            "kind" -> 1
            "metadata" -> 2
            "spec" -> 3
            "type" -> 4
            else -> Int.MAX_VALUE
        }
    }


    inner class RepresentDelegatingNode : Represent {

        @Suppress("SENSELESS_COMPARISON")
        override fun representData(data: Any?): Node? {
            if (data !is DelegatingNode)
                return null

            val sortedProperties = data.properties
                .filter { it.key != null && it.value != null }
                .toSortedMap { a, b ->
                    val comparasionResult = getPropertyPosition(a).compareTo(getPropertyPosition(b))
                    return@toSortedMap comparasionResult
                }
            return representMapping(Tag.MAP, sortedProperties, FlowStyle.BLOCK)
        }
    }

    inner class PresentLogMessageType : Represent {
        override fun representData(data: Any?): Node? {
            if (data !is LogMessageType)
                return null
            return representScalar(Tag.STR, data.type, ScalarStyle.PLAIN)
        }
    }

    override fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple? {
        val clearName = property!!.name.takeWhile { c -> c != '$' }

        /** ignore all properties with {@see null} values. to ignore it, we need to return null from this function  */
        if (propertyValue == null)
            return null

        /** substitute delegate property names {@sample name$delegate}  with  with {@sample name}  */
        val substitute = PropertySubstitute(clearName, property.type)


        val node = represent(propertyValue)

        if (property.type == String::class.java)
            representScalar(Tag.STR, propertyValue as String, ScalarStyle.PLAIN)

        val nodeTuple = super.representJavaBeanProperty(javaBean, substitute, propertyValue, Tag.MAP)
        return nodeTuple
    }
}


class DelegatingNodeDescription : TypeDescription(DelegatingNode::class.java, DelegatingNode::class.java) {

    companion object {
        const val tagIdentifier = "io.alexheld.mockserver.nodes.delegating"
    }

    init {
        setTag(tagIdentifier)
    }

    /*private fun handleMappingNode(node: MappingNode) {

        val nonEmptyMembers= node.value.filter {
            val keyNode = it.keyNode as ScalarNode
            val valueNode = it.valueNode
            keyNode.value != null && valueNode != null
        }.toMutableList()

        val linkedHashMa: LinkedHashMap<String, Any> = linkedMapOf()
        for (member in nonEmptyMembers)
        {

        }
            linkedHashMa.put()
        val delegatingNode = DelegatingNode()
    }*/


}

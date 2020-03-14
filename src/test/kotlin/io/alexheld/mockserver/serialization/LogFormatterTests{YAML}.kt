package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.documents.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.testUtil.*
import kotlinx.io.StringWriter
import org.amshove.kluent.*
import org.apache.tools.ant.util.*
import org.gradle.internal.impldep.org.bouncycastle.util.Iterable
import org.junit.jupiter.api.*
import org.snakeyaml.engine.v2.api.*
import org.snakeyaml.engine.v2.common.*
import org.snakeyaml.engine.v2.representer.*
import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.resolver.*
import java.io.*
import java.lang.reflect.Array
import java.time.*
import java.util.*
import java.util.regex.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set
import kotlin.reflect.full.*


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
        log.apiVersion.shouldBeEqualTo("1.0")

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


        val options = DumperOptions()
        options.isAllowUnicode = true
        options.indicatorIndent = 2
        options.indent = 4
        options.isPrettyFlow = true
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.isExplicitStart = true
        options.isExplicitEnd = true
        options.defaultScalarStyle = DumperOptions.ScalarStyle.PLAIN


        val presenter = LogRepresenter()
        presenter.addClassTag(Log::class.java, Tag.MAP)
        presenter.addClassTag(Node::class.java, Tag.MAP)
        presenter.addClassTag(Setup::class.java, Tag.MAP)
        presenter.addClassTag(Request::class.java, Tag.MAP)
        presenter.addClassTag(LinkedHashMap::class.java, Tag.MAP)
        presenter.addClassTag(LinkedHashSet::class.java, Tag.MAP)
        presenter.addClassTag(LinkedHashtable::class.java, Tag.MAP)
        presenter.addClassTag(HashMap::class.java, Tag.MAP)
        presenter.addClassTag(ArrayList::class.java, Tag.MAP)
        presenter.addClassTag(Array::class.java, Tag.MAP)

        val loader = LoaderOptions()
        loader.isAllowDuplicateKeys = false
        loader.isWrappedToRootException = true

        val y = Yaml(Constructor(Node::class.java), presenter, options, loader, LogResolver())
        y.setBeanAccess(BeanAccess.FIELD)
        y.addImplicitResolver(Tag.MAP, Pattern.compile("XXXXXXXX"), null)

        val yaml = y.dumpAll(iterator {
            for (m in maps)
                yield(m)
        })

        yaml.dump("YamlSetupDocument")


        // writer.close()


/*

        val composer = Composer(ParserImpl(ScannerImpl(StreamReader(Reader.nullReader(), loadSettings)),loadSettings), loadSettings)
        composer.forEachRemaining{ node -> node.anchor.map { a -> println("anchor= ${a.value}") } }
*/

        /*


        val represent = LogRepresenter()
        represent.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK

        val loaded = ParserImpl(Scanner(readYaml("Request_Matched")), LoadSettingsBuilder()
            .setAllowDuplicateKeys(false)
            .setAllowRecursiveKeys(true)
            .setCustomProperty(SettingKey()) )
            .setScalarResolver{ tag, implicit -> if(tag == "log")
            .build())


            val yaml= Dump(settings).dumpAllToString( iterator {
            for (m in maps)
                yield(m)
        })

        -----


        val y = Yaml(CustomClassLoaderConstructor(Log::class.java, ClassLoader.getSystemClassLoader()),represent, options, resolver)
        y.setBeanAccess(BeanAccess.FIELD)

        val options = DumperOptions()
        options.isAllowUnicode = true
        options.indicatorIndent = 2
        options.indent = 4
        options.isCanonical = false
        options.defaultScalarStyle = DumperOptions.ScalarStyle.SINGLE_QUOTED



        val yaml= y.dumpAll( iterator {
            for (m in maps)
                yield(m)
        })
        */

        // writer.close()


        /*
         val sb = StringWriter()
            val writer = YAMLFormatter.getMapper()
                .writer()
            writeMap(map, writer, sb)
           */


        //val yaml = YAMLFormatter.serialize(map)


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


    fun formatV2(data: Iterable<*>) {

        val loadSettings = LoadSettings.builder()
            .setTagConstructors(mutableMapOf(Pair(org.snakeyaml.engine.v2.nodes.Tag.MAP, ConstructNode { node ->
                node.anchor = Optional.empty()
                return@ConstructNode node
            })))
            .setAllowDuplicateKeys(false)
            .setScalarResolver { value: String, implicit: Boolean -> org.snakeyaml.engine.v2.nodes.Tag("") }
            .setUseMarks(false)
            .setMaxAliasesForCollections(0)
            .build()

        val writer = StreamToStringWriter()
        Dump(buildDumpSettings(2), StandardRepresenter(buildDumpSettings(2)))
            .dumpAll(iterator {
                for (m in data)
                    yield(m)
            }, writer)

        writer.writer.close()

        val yaml = writer.writer.toString()
        yaml.dump("Document<YamlLogDocument>")
    }


    class LogResolver : Resolver() {

        init {
            addImplicitResolver(Tag("log"), VALUE, "setup")
        }

        override fun resolve(kind: NodeId?, value: String?, implicit: Boolean): Tag {
            if (kind != null && kind == NodeId.scalar)
                return Tag.MAP

            return super.resolve(kind, value, implicit)
        }
    }


    private fun buildDumpSettings(indicatorIndent: Int): DumpSettings = DumpSettings.builder()
        .setIndent(indicatorIndent + 2)
        .setIndicatorIndent(indicatorIndent)
        .setDefaultFlowStyle(FlowStyle.BLOCK)
        /* .setDefaultScalarStyle(ScalarStyle.SINGLE_QUOTED)
         .setUseUnicodeEncoding(true)
         .setBestLineBreak("\n")
         .setNonPrintableStyle(NonPrintableStyle.ESCAPE)
         .setExplicitEnd(true)*/

        .build()

    private fun buildDump(indicatorIndent: Int): Dump = Dump(buildDumpSettings(indicatorIndent))
}

inline fun <reified TNode : Node> getDelegatedValues(target: TNode): Map<String, Any> {

    val type = TNode::class
    val values = mutableMapOf<String, Any>()

    for (p in type.memberProperties.filterNot { p -> p.name == "properties" }) {
        val delegatedValue = p.getDelegate(target)
        if (delegatedValue != null)
            values[p.name] = delegatedValue
        continue
    }

    return values
}



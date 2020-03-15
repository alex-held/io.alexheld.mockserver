package io.alexheld.mockserver.serialization.yaml
/*

import io.alexheld.mockserver.serialization.*
import org.snakeyaml.engine.v2.api.*
import org.snakeyaml.engine.v2.common.*
import org.snakeyaml.engine.v2.constructor.*
import org.snakeyaml.engine.v2.nodes.*
import org.snakeyaml.engine.v2.representer.*
import java.util.*

object YamlConfiguration {

    private fun initModelMap() {
        val classLoader = YamlConfiguration::class.java.classLoader
        classLoader.isRegisteredAsParallelCapable
    }

    init {
        try {
            initModelMap()
        } catch (ex: Exception) {
            println("Unexpected exception while loading classes: ${ex.localizedMessage}")
        }
    }

    private const val indent: Int = 2


    fun dump(data: Iterable<*>): String {
        return buildDump().dumpAllToString(data.iterator())
    }

    fun dumpLogDocument(data: YamlLogDocument): String {
        return buildDump().dumpToString(data)
    }

    fun construct(node: Node): Any? {
        return buildConstructor().constructSingleDocument(Optional.of(node))
    }

    fun represent(data: Any): Node {
        return buildRepresenter().represent(data)
    }

    private fun buildConstructor(): BaseConstructor {
        return StandardConstructor(loadSettings)
    }


    fun buildDump(): Dump = Dump(dumpSettings)

    private val dumpSettings: DumpSettings = DumpSettings.builder()
        .setCanonical(false)
        .setExplicitEnd(true)
        .setIndent(indent + 2)
        .setIndicatorIndent(indent)
        .setDefaultFlowStyle(FlowStyle.BLOCK)
        .setDefaultScalarStyle(ScalarStyle.LITERAL)
        .build()

    val loadSettings: LoadSettings = LoadSettings.builder()
        .setAllowRecursiveKeys(true)
        //.setMaxAliasesForCollections(1)
        //.setUseMarks(false)
        .build()


    fun buildRepresenter(): BaseRepresenter {
        return StandardRepresenter(dumpSettings)
    }
}
*/

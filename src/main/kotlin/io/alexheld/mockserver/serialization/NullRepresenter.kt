package io.alexheld.mockserver.serialization
/*

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import org.gradle.internal.impldep.org.yaml.snakeyaml.*
import org.gradle.internal.impldep.org.yaml.snakeyaml.nodes.*
import org.gradle.internal.impldep.org.yaml.snakeyaml.representer.*
import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.representer.*
import java.util.*
import kotlin.reflect.full.*


class NullRepresenter() : Representer() {

    init {
        this.defaultScalarStyle = DumperOptions.ScalarStyle.LITERAL
        this.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        addClassTag(LinkedList::class.java, Tag.MAP)
        addClassTag(HashMap::class.java, Tag.MAP)
        addClassTag(Log::class.java, Tag.MAP)
        addClassTag(Array<String>::class.java, Tag.MAP)
        addClassTag(ArrayList::class.java, Tag.MAP)
        addClassTag(LogMessageType::class.java, Tag.MAP)
        addClassTag(Setup::class.java, Tag.MAP)
        addClassTag(Node::class.java, Tag.MAP)
        addClassTag(Request::class.java, Tag.MAP)
        addClassTag(Action::class.java, Tag.MAP)
        addClassTag(YamlLogDocument::class.java, Tag.MAP)
    }










    override fun representJavaBean(properties: Set<Property>?, javaBean: Any?): MappingNode? {
        if (javaBean == null) {
            return super.representJavaBean(properties, javaBean)
        }
        if (!classTags.containsKey(javaBean::class.java))
            addClassTag(javaBean::class.java, Tag.MAP)


       */
/* // Sort the output of our map so that we put certain keys, such as apiVersion, first.
        val sorted= properties!!.toMutableList().sortWith(Comparator { a: NodeTuple, b: NodeTuple ->
            val nameA = (a.keyNode as ScalarNode).value
            val nameB = (b.keyNode as ScalarNode).value
            val intCompare = getPropertyPosition(nameA).compareTo(getPropertyPosition(nameB))

            if (intCompare != 0) {
                return@Comparator intCompare
            } else {
                return@Comparator nameA.compareTo(nameB)
            }
        })*//*



        println("Discovered tag for type ${javaBean::class.java}; tag=${Tag.MAP.value}")
        val value: MutableList<NodeTuple> = ArrayList(properties!!.size)
        for (property in properties) {

            val memberValue = property!!.get(javaBean)
            val customPropertyTag = if (memberValue == null) null else classTags[memberValue.javaClass]
            val tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag)
            value.add(tuple!!)
        }

        return super.representJavaBean(properties, javaBean)

*/
/*
        val memberProperties: MutableList<NodeTuple> = arrayListOf()
        val node = MappingNode(Tag.MAP, memberProperties, DumperOptions.FlowStyle.BLOCK)
        representedObjects[javaBean] = node


        for (prop in properties){
            val memberValue: Any = prop!!.get(javaBean)
            val tuple = representJavaBeanProperty(javaBean, prop, memberValue, Tag.MAP) ?: continue;
            memberProperties.add(tuple);
        }

        return node;*//*

    }

    */
/**
 * This returns the ordering of properties that by convention should appear at the beginning of
 * a Yaml object in Kubernetes.
 *//*

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

    override fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple {

        try {

            if (property?.name?.endsWith("\$delegate") == true) {
                val name = property.name.takeWhile { c -> c != '$' }
                println("Found delegated property. name=$name")
                if (propertyValue != null) return NodeTuple(representData(name), representData(propertyValue))
            }


            val hasAlias = representedObjects.containsKey(propertyValue)
            val nodeKey = representData(property!!.name) as ScalarNode


            if (propertyValue != null) {
                val yamTagAnnotation = propertyValue::class.findAnnotation<YamlTagAnnotation>()
                val tag = yamTagAnnotation?.getTag()
                if (tag is Tag)
                    return NodeTuple(representData(tag.value), representData(propertyValue))
            }

            println("node key = $nodeKey")
            val nodeValue = representData(propertyValue)
            if (!hasAlias && javaBean == null) return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag)
            val nodeId = nodeValue.nodeId

            if (property.type != Enum::class.java && propertyValue is Enum<*>) nodeValue.tag = Tag.STR
            else if (nodeId == NodeId.mapping) {
                if (propertyValue != null && property.type == propertyValue.javaClass) {
                    if (propertyValue !is Map<*, *>) {
                        if (nodeValue.tag != Tag.SET) {
                            nodeValue.tag = Tag.MAP
                        }
                    }
                }
                checkGlobalTag(property, nodeValue, propertyValue)
            }

            return NodeTuple(representData(nodeKey.value), representData(propertyValue))
        } catch (e: Exception) {
            println(e.localizedMessage)
            throw e
        }
    }


}
*/

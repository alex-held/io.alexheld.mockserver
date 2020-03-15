package io.alexheld.mockserver.serialization

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import org.gradle.internal.impldep.org.yaml.snakeyaml.*
import org.gradle.internal.impldep.org.yaml.snakeyaml.introspector.*
import org.gradle.internal.impldep.org.yaml.snakeyaml.nodes.*
import org.gradle.internal.impldep.org.yaml.snakeyaml.representer.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.java
import kotlin.reflect.full.*

/*
inline fun <reified T> YAMLFormatter.deserialize(yaml: String): T {
    return getMapper().readValue(yaml, T::class.java) as T
}*/

@kotlin.annotation.Target(AnnotationTarget.TYPE, AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class YamlTagAnnotation(val defaultTag: String)

fun YamlTagAnnotation.getTag(): Tag {
    return Tag(defaultTag)
}

fun YamlTagAnnotation.getTagV2(): Tag {
    return Tag(Node::class.java)
}


fun Class<*>?.isNode(): Boolean = this?.kotlin?.isSubclassOf(Node::class) ?: false

fun Class<*>?.tryGetTagAnnotation(): YamlTagAnnotation? {
    return this?.kotlin?.findAnnotation()
}

class LogRepresenter : Representer {

    /* this.addClassTag(Log::class.java, Tag("api:model:Log"))
     this.addClassTag(Setup::class.java, Tag("api:model:Setup"))
     this.addClassTag(Request::class.java, Tag("api:model:Request"))
     this.addClassTag(Node::class.java, Tag("api:model:Node"))
     this.addClassTag(Setup::class.java, Tag("api:model:setup"))*/
    constructor(dumperOptions: DumperOptions = DumperOptions()) : super() {

        this.defaultFlowStyle = dumperOptions.defaultFlowStyle

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

    override fun representJavaBean(properties: MutableSet<Property>?, javaBean: Any?): MappingNode {
        if (javaBean == null) return super.representJavaBean(properties, javaBean)

        if (!classTags.containsKey(javaBean::class.java))
            addClassTag(javaBean::class.java, Tag.MAP)

        println("Discovered tag for type ${javaBean::class.java}; tag=${Tag.MAP.value}")

        val value: MutableList<NodeTuple> = java.util.ArrayList(properties!!.size)

        for (property in properties) {

            val memberValue = property.get(javaBean)
            val customPropertyTag = if (memberValue == null) null else classTags[memberValue.javaClass]
            val tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag)
            value.add(tuple)
        }

        return super.representJavaBean(properties, javaBean)
    }

    override fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple {

        try {
            if (property?.name?.endsWith("\$delegate") == true) {
                val name = property.name.takeWhile { c -> c != '$' }
                if (propertyValue != null)
                    return NodeTuple(representData(name), representData(propertyValue))
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
            if (!hasAlias && javaBean == null || property == null) return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag)
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
            if (propertyValue != null) {
                val yamTagAnnotation = propertyValue::class.findAnnotation<YamlTagAnnotation>()
                val tag = yamTagAnnotation?.getTag()
                if (tag is Tag)
                    return NodeTuple(representData(tag.value), representData(propertyValue))
            }
            return NodeTuple(representData(nodeKey.value), representData(propertyValue))
        } catch (e: Exception) {
            println(e.localizedMessage)
            throw e
        }
    }

    override fun setPropertyUtils(propertyUtils: PropertyUtils?) {
        propertyUtils?.setBeanAccess(BeanAccess.FIELD)
    }

    override fun checkGlobalTag(property: Property?, node: Node?, `object`: Any?) {
        super.checkGlobalTag(property, node, `object`)
    }

    /* override fun getProperties(type: Class<out Any>?): MutableSet<Property> {
         if (type.isNode())
         {
             val node = objectToRepresent as Node
             val properties = node.properties.map { e-> PropertySubstitute(e.key, e.value.javaClass) }
                 .toMutableList()

             for(prop in properties)
             {
                 if(node::class.java.kotlin.memberProperties.)
             }
             return properties.toMutableSet()
         }

         return super.getProperties(type)
     }*/


    /*

    */
    /**
     * Represent the provided Java instance to a Node
     *
     * @param data - Java instance to be represented
     * @return The Node to be serialized
     *//*

    override fun represent(data: Any?): org.snakeyaml.engine.v2.nodes.Node {

        this.objectToRepresent = data

        val node = representData(data)
        if (node.nodeType == NodeType.MAPPING)
            return representMapping(node.tag, classTags, FlowStyle.AUTO)

        representedObjects.clear()
        return node
    }

    private class RepresentLog : RepresentToNode {

        */
    /**
     * Create a Node
     *
     * @param data the instance to represent
     * @return Node to dump
     *//*

        override fun representData(data: Any?): org.snakeyaml.engine.v2.nodes.Node {
            val log: Log = data as Log
            val value: String = log.id
            return ScalarNode(Tag("!log"), value, ScalarStyle.LITERAL)
        }
}
*/
}

/*

object YAMLFormatter : Logging {

    fun getMapper(): YAMLMapper {

        val mapper = YAMLMapper()
            //     .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
            .enable(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE)
            .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("en", "DE")))
            .setTimeZone(TimeZone.getTimeZone("UTC"))
            .registerKotlinModule()
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)


        return mapper as YAMLMapper
    }


    fun <T> serialize(serializable: T): String = getMapper().writeValueAsString(serializable)
}
*/

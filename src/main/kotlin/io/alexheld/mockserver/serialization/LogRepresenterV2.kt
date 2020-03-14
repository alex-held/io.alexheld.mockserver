package io.alexheld.mockserver.serialization

import org.snakeyaml.engine.v2.api.*
import org.snakeyaml.engine.v2.representer.*


class CustomRepresenter(settings: DumpSettings) : StandardRepresenter(settings)


/*

public open class LogRepresenterV2(settings: DumpSettings) : StandardRepresenter(settings) {

    init {

        this.parentClassRepresenters[MockServerNode::class.java] = NodeRepresenter()
    }


    override fun findRepresenterFor(data: Any?): Optional<RepresentToNode> {
        objectToRepresent = data
        if (data is MockServerNode)
        val nodeKeyValuePairs = NodeRepresenter().representData(data)

        return super.findRepresenterFor(data)
    }


//
//    override fun representJavaBean(properties: MutableSet<Property>?, javaBean: Any?): MappingNode {
//        if (javaBean == null) return super.representJavaBean(properties, javaBean)
//        if (!classTags.containsKey(javaBean::class.java))
//            addClassTag(javaBean::class.java, Tag.MAP)
//
//        println("Discovered tag for type ${javaBean::class.java}; tag=${Tag.MAP.value}")
//
//        val value: MutableList<NodeTuple> = java.util.ArrayList(properties!!.size)
//
//        for (property in properties){
//
//            val memberValue = property.get(javaBean)
//            val customPropertyTag = if (memberValue == null) null else classTags[memberValue.javaClass]
//            val tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag) ?: continue
//            value.add(tuple)
//
//        }
//        return super.representJavaBean(properties, javaBean)
//    }


    override fun representScalar(tag: Tag?, value: String?): org.snakeyaml.engine.v2.nodes.Node {
        JsonScalarResolver().resolve()
        return super.representScalar(tag, value)
    }


    override fun getTag(clazz: Class<*>?, defaultTag: Tag?): Tag {

        return if (classTags.containsKey(clazz)) {
            classTags[clazz]!!
        } else if (!classTags.containsKey(clazz)) {
            val tagAnnotation = clazz.tryGetTagAnnotation()?.getTagV2()
            if (tagAnnotation is Tag) {
                classTags[clazz] = tagAnnotation
                return tagAnnotation
            }
            defaultTag!!
        } else {
            defaultTag!!
        }
    }

    override fun representScalar(tag: Tag?, value: String?, style: ScalarStyle?): org.snakeyaml.engine.v2.nodes.Node {

        return super.representScalar(tag, value, style)
    }


    public override fun representMapping(tag: Tag?, mapping: MutableMap<*, *>?, flowStyle: FlowStyle?): SnakeYamlNode {

        return super.representMapping(tag, mapping, flowStyle)
    }


    protected class RepresentMap : RepresentToNode {
        override fun representData(data: Any): Node? {
            return representMapping(
                getTag(data.javaClass, Tag.MAP), data as Map<Any?, Any?>,
                FlowStyle.AUTO
            )
        }

        private fun representMapping(tag: org.yaml.snakeyaml.nodes.Tag, map: Map<Any?, Any?>, auto: FlowStyle): Node? {
            TODO("Not yet implemented")
        }
    }


    protected class NodeRepresenter : RepresentToNode {

        */

/**
 * Create a Node
 *
 * @param data the instance to represent
 * @return Node to dump
 *//*

        override fun representData(data: Any?): Node? {

            if (data !is MockServerNode)
                throw NotSupportedException("Cannot represent $data.")

            val tuples: MutableList<NodeTuple> = mutableListOf()

            for (p in data.properties) {
                val keyNode = ScalarNode(Tag.STR, p.key, ScalarStyle.LITERAL)
                val valueNode = representData(p.value)
                val tuple = NodeTuple(keyNode, valueNode)
                tuples.add(tuple)
            }

            return representData(data)
        }
    }

}
*/
/*    override fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple {

        try {
            if(property?.name?.endsWith("\$delegate") == true) {
                val name = property.name.takeWhile { c -> c != '$' }
                if (propertyValue != null)
                    return NodeTuple(representData(name), representData(propertyValue))
            }


            val hasAlias = representedObjects.containsKey(propertyValue)

            val nodeKey = representData(property!!.name) as ScalarNode

            if (propertyValue != null) {
                val simpleName = property.type.simpleName
                val yamTagAnnotation = propertyValue::class.findAnnotation<YamlTagAnnotation>()
                val tag = yamTagAnnotation?.getTag()
                if (tag is Tag)
                    return NodeTuple(representData(tag.value), representData( propertyValue))
            }

            println("node key = $nodeKey")
            val nodeValue = representData(propertyValue)
            if (!hasAlias && javaBean == null || property == null) return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag)
            val nodeId = nodeValue.nodeId

            if (property.type != Enum::class.java && propertyValue is Enum<*>) nodeValue.tag = Tag
            else if(nodeId == NodeId.mapping) {
                if (propertyValue != null && property.type == propertyValue.javaClass) {
                    if (propertyValue !is Map<*, *>) {
                        if (nodeValue.tag != Tag) {
                            nodeValue.tag = Tag
                        }
                    }
                }
                checkGlobalTag(property, nodeValue, propertyValue)
            }
            if (propertyValue != null) {
                val simpleName = property.type.simpleName
                val yamTagAnnotation = propertyValue::class.findAnnotation<YamlTagAnnotation>()
                val tag = yamTagAnnotation?.getTag()
                if (tag is Tag)
                    return NodeTuple(representData(tag.value), representData( propertyValue))
            }
            return NodeTuple(representData(nodeKey.value), representData( propertyValue))
        }
        catch (e: Exception)
        {
            println(e.localizedMessage)
            throw e
        }
    }*/


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


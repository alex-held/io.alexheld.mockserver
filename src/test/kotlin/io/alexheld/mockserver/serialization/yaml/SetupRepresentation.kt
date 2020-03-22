/*
package io.alexheld.mockserver.serialization.yaml

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.models.*
import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.representer.*


class SetupRepresentation : Representer() {

    init {

        defaultScalarStyle = DumperOptions.ScalarStyle.PLAIN
        defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        propertyUtils.setBeanAccess(BeanAccess.FIELD)
        propertyUtils.isSkipMissingProperties = true

*/
/*


        addTypeDescription(TypeDescription(Setup::class.java, Tag.MAP))
        addTypeDescription(TypeDescription(Action::class.java, Tag("act")))
        addTypeDescription(TypeDescription(Request::class.java, Tag.MAP))
        addTypeDescription(TypeDescription(RequestBody::class.java, Tag.MAP))
        addTypeDescription(TypeDescription(RequestMatchedData::class.java, Tag.MAP))
*//*

        addTypeDescription(TypeDescription(Setup::class.java, Tag("!setup")))
        addClassTag(Setup::class.java, Tag("!setup"))
        //  addClassTag(Setup::class.java,  Tag.MAP)
        addClassTag(Action::class.java, Tag.MAP)
        addClassTag(Request::class.java, Tag.MAP)
        addClassTag(RequestBody::class.java, Tag.MAP)
        addClassTag(SetupCreatedData::class.java, Tag.MAP)
        addClassTag(RequestMatchedData::class.java, Tag.MAP)
    }


    override fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple? {

        if (propertyValue == null) {
            return null
        }
        val keyNode = representData(property!!.name) as ScalarNode


        // the first occurrence of the node must keep the tag
        val hasAlias = representedObjects.containsKey(propertyValue)

        val valueNode = representData(propertyValue)
        if (propertyValue != null && !hasAlias) {

            // set scalar style for literals (strings, int, bool etc...)
            if (valueNode.nodeId == NodeId.scalar) valueNode.tag = Tag.STR

            // set map style for members
            else if (valueNode.nodeId == NodeId.mapping) valueNode.tag = Tag.MAP

            // check against duplicate tag
            checkGlobalTag(property, valueNode, propertyValue)
        }

        return NodeTuple(keyNode, valueNode)
    }

    override fun representJavaBean(properties: MutableSet<Property>, javaBean: Any?): MappingNode? {

        val members = mutableSetOf<NodeTuple>().toMutableList()
        val node = MappingNode(Tag.MAP, members, DumperOptions.FlowStyle.BLOCK)

        for (property in properties) {

            // skip <null> properties
            val propertyValue = property.get(javaBean) ?: continue

            val beanPropertyNode = representJavaBeanProperty(javaBean, property, propertyValue, Tag.MAP)
            if (beanPropertyNode is NodeTuple) members.add(beanPropertyNode)
        }

        return node
    }


    override fun represent(data: Any?): Node {
        if (data is LogDTO)
            return representData(data.content)
        return representData(data)
    }

    private fun getPropertyPosition(propertyName: String): Int {
        return when (propertyName) {
            "apiVersion" -> 0
            "id" -> 1
            "kind" -> 2
            "spec" -> 3
            "type" -> 4
            "timestamp" -> 5
            else -> Int.MAX_VALUE
        }
    }
}
*/

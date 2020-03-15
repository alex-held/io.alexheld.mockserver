package io.alexheld.mockserver.serialization

import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.representer.*
import java.util.*


class NullRepresenter : Representer() {

    override fun representJavaBean(properties: Set<Property?>?, javaBean: Any?): MappingNode? {

        val node = super.representJavaBean(properties, javaBean)
        // Always set the tag to MAP so that SnakeYaml doesn't print out the class name as a tag.
        node.tag = Tag.MAP
        // Sort the output of our map so that we put certain keys, such as apiVersion, first.
        node.value.sortWith(Comparator { a: NodeTuple, b: NodeTuple ->
            val nameA = (a.keyNode as ScalarNode).value
            val nameB = (b.keyNode as ScalarNode).value
            val intCompare = getPropertyPosition(nameA).compareTo(getPropertyPosition(nameB))

            if (intCompare != 0) {
                return@Comparator intCompare
            } else {
                return@Comparator nameA.compareTo(nameB)
            }
        })
        return node
    }

    /**
     * This returns the ordering of properties that by convention should appear at the beginning of
     * a Yaml object in Kubernetes.
     */
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

    override fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple? {
        // returning null for a null property value means we won't output it in the Yaml
        if (propertyValue == null) {
            return null
        }
        return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag)
    }

}

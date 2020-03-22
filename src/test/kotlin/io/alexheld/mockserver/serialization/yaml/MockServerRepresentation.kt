/*
package io.alexheld.mockserver.serialization.yaml

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.introspector.*
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.representer.*

class MockServerRepresentation : Representer() {

    init {
        this.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        this.addClassTag(RequestMatchedData::class.java, Tag.MAP)
        this.addClassTag(Request::class.java, Tag.MAP)
        this.addClassTag(Setup::class.java, Tag.MAP)
        this.addClassTag(Action::class.java, Tag.MAP)

        this.representers[LogMessageType::class.java] = PresentLogMessageType()
        this.representers[DelegatingNode::class.java] = RepresentDelegatingNode()
        this.representers[ILog::class.java] = RepresentDelegatingNode()
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

    private fun representChildren(data: DelegatingNode, children: MutableMap<String, MutableList<DelegatingNode>>): List<NodeTuple> {
        try {
            val nodeMembers: MutableList<NodeTuple> = mutableListOf()

            for (group in children) {
                val propertyName = group.key
                val values = group.value

                if (values.size == 1) {
                    val childGroup = representChildNode(data, propertyName, values.first())
                    if (childGroup is NodeTuple) nodeMembers.add(childGroup)

                    continue
                }

                val childGroup = representChildGroup(data, propertyName, values)
                if (childGroup is NodeTuple) nodeMembers.add(childGroup)
            }

            return nodeMembers

        } catch (e: Exception) {
            println(e.localizedMessage)
            return listOf()
        }
    }

    private fun representChildGroup(data: DelegatingNode, propertyName: String, children: MutableList<DelegatingNode>): NodeTuple? {
        return try {
            val keyNode = representScalar(Tag.STR, propertyName, DumperOptions.ScalarStyle.PLAIN)
            val valueNode = representSequence(Tag.OMAP, children, DumperOptions.FlowStyle.BLOCK)
            NodeTuple(keyNode, valueNode)
        } catch (e: Exception) {
            println(e.localizedMessage)
            null
        }
    }

    private fun representChildNode(data: DelegatingNode, propertyName: String, child: DelegatingNode): NodeTuple? {
        return try {
            val childNode = representData(child) ?: return null
            NodeTuple(
                representScalar(Tag.STR, propertyName, DumperOptions.ScalarStyle.PLAIN),
                representSequence(Tag.SEQ, mutableListOf(childNode), DumperOptions.FlowStyle.BLOCK)
            )
        } catch (e: Exception) {
            println(e.localizedMessage)
            null
        }

    }


    inner class RepresentDelegatingNode : Represent {

        fun handleMembers(delegatingNode: DelegatingNode, members: Map<String, Any>): MappingNode? {
            return try {
                return representMapping(Tag.MAP, members.mapKeys { k -> k.key.takeWhile { c -> c != '$' } }, DumperOptions.FlowStyle.BLOCK) as MappingNode
            } catch (e: Exception) {
                println(e.localizedMessage)
                null
            }
        }
*/
/*

        fun handleMembers(delegatingNode: DelegatingNode, members: Map<String, Any>): MappingNode? {
            return try {

                val value : MutableList<NodeTuple> = arrayListOf()
                for (m in members){
                    val propertyNode = handleProperty(delegatingNode, Pair(m.key, m.value))

                    if (propertyNode is NodeTuple)
                        value.add(propertyNode)
                }
                return representMapping(Tag.SEQ, , DumperOptions.FlowStyle.BLOCK) as MappingNode
            } catch (e: Exception) {
                println(e.localizedMessage)
                null
            }
        }

        fun handleProperty(delegatingNode: DelegatingNode, pair: Pair<String, Any>): NodeTuple? {
            return try {

                *//*

        */
/** substitute delegate property names {@sample name$delegate}  with  with {@sample name}  *//*
*/
/*

                val clearName = pair.first.takeWhile { c -> c != '$'}

                *//*

        */
/** ignore all properties with {@see null} values. to ignore it, we need to return null from this function  *//*
*/
/*

        *//*

*/
/*        val propertyValue = pair.second ?: return null
                val substitute = PropertySubstitute(clearName, propertyValue::class.java)
*//*
*/
/*


                NodeTuple(
                    representScalar(Tag.STR, clearName, DumperOptions.ScalarStyle.PLAIN),
                    representData(pair.second)
                )
            } catch (e: Exception) {
                println(e.localizedMessage)
                null
            }

        }
*//*


        override fun representData(data: Any?): Node? {
            if (data !is DelegatingNode) return null

            val members = data.properties
                .filterValues { return@filterValues it != null }
                .toSortedMap(Comparator.comparingInt { name ->
                    getPropertyPosition(name)
                })

            members.map {
                val cleanName = it.key.takeWhile { c -> c != '$' }
                println("PropertyName ${it.key} -> $cleanName")
            }


            //    val mainNode = representMapping(Tag.MAP, members, DumperOptions.FlowStyle.BLOCK) as MappingNode
            val mainNode = handleMembers(data, members)


            val children = representChildren(data, data.children)
            if (!children.any())
                return mainNode

            for (child in children) {
                mainNode!!.setTypes(child.keyNode.type, child.valueNode.type)
            }

            return mainNode
        }

    }

    inner class PresentLogMessageType : Represent {
        override fun representData(data: Any?): Node? {
            if (data !is LogMessageType)
                return null
            return representScalar(Tag.STR, data.name, DumperOptions.ScalarStyle.PLAIN)
        }
    }

    override fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple? {
        val clearName = property!!.name.takeWhile { c -> c != '$' }

        */
/** ignore all properties with {@see null} values. to ignore it, we need to return null from this function *//*

        if (propertyValue == null)
            return null

        */
/** substitute delegate property names {@sample name$delegate}  with  with {@sample name} *//*

        val substitute = PropertySubstitute(clearName, property.type)

        println(javaBean!!::class.java.name)

        // TODO: This makes the touble
        //val node = representScalar(Tag.MAP, propertyValue as String)
        val node = representMapping(Tag.MAP, propertyValue as Map<*, *>, DumperOptions.FlowStyle.BLOCK)
        if (property.type == String::class.java)
            representScalar(Tag.STR, propertyValue as String, DumperOptions.ScalarStyle.PLAIN)

        return super.representJavaBeanProperty(javaBean, substitute, propertyValue, Tag.STR)
    }

}
*/

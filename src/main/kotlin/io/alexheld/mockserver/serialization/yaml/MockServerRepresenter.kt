package io.alexheld.mockserver.serialization.yaml

/**
package io.alexheld.mockserver.serialization.yaml

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.serialization.*
import org.gradle.internal.impldep.org.yaml.snakeyaml.nodes.*
import org.snakeyaml.engine.v2.api.*
import org.snakeyaml.engine.v2.common.*
import org.snakeyaml.engine.v2.nodes.*
import org.snakeyaml.engine.v2.nodes.MappingNode
import org.snakeyaml.engine.v2.nodes.NodeTuple
import org.snakeyaml.engine.v2.nodes.ScalarNode
import org.snakeyaml.engine.v2.nodes.Tag as Tag
import org.snakeyaml.engine.v2.representer.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.*
import kotlin.reflect.full.*
import org.snakeyaml.engine.v2.nodes.Node as SnakeYamlNode


fun KProperty1<out Any, *>.isDelegatedProperty() = name.endsWith("\$delegate")
fun KProperty1<out Any, *>.getPropertyName() = name.takeWhile { c -> c != '$' }


public class MockServerRepresenter(private val dumpSettings: DumpSettings = YamlConfiguration.dumpSettings) : StandardRepresenter(dumpSettings) {

init {
defaultFlowStyle = dumpSettings.defaultFlowStyle
defaultScalarStyle = dumpSettings.defaultScalarStyle


addClassTag(LinkedList::class.java, Tag.MAP)
addClassTag(HashMap::class.java, Tag.MAP)
addClassTag(Log::class.java, Tag.MAP)
addClassTag(Array<String>::class.java, Tag.MAP)
addClassTag(ArrayList::class.java, Tag.MAP)
addClassTag(LogMessageType::class.java, Tag.MAP)
addClassTag(Setup::class.java, Tag.MAP)
addClassTag(DelegatingNode::class.java, Tag.MAP)
addClassTag(Request::class.java, Tag.MAP)
addClassTag(Action::class.java, Tag.MAP)
addClassTag(YamlLogDocument::class.java, Tag.MAP)

//this.representers[IntOrString::class.java] = RepresentIntOrString()
this.representers[YamlLogDocument::class.java] = RepresentYamlLogDocument()
this.representers[DelegatingNode::class.java] = RepresentDelegatingNode()
this.representers[Log::class.java] = RepresentDelegatingNode()
this.representers[Setup::class.java] = RepresentDelegatingNode()
this.representers[Request::class.java] = RepresentDelegatingNode()
this.representers[RequestBody::class.java] = RepresentDelegatingNode()
this.representers[Action::class.java] = RepresentDelegatingNode()

this.parentClassRepresenters[DelegatingNode::class.java] = RepresentDelegatingNode()
this.parentClassRepresenters[HashMap::class.java] = RepresentDelegatedProperties()
this.parentClassRepresenters[LinkedHashMap::class.java] = RepresentDelegatedProperties()
this.parentClassRepresenters[AbstractMap::class.java] = RepresentDelegatedProperties()

}

override fun represent(data: Any?): SnakeYamlNode? {
val node: SnakeYamlNode = representData(data)
representedObjects.clear()
objectToRepresent = null
return node
}


fun representJavaBean(properties: MutableSet<Property>?, javaBean: Any?): MappingNode {
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

fun representJavaBeanProperty(javaBean: Any?, property: Property?, propertyValue: Any?, customTag: Tag?): NodeTuple {

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
val tag = yamTagAnnotation?.getTagV2()
if (tag is Tag) {
return NodeTuple(representData(tag.value), representData(propertyValue))
}
return NodeTuple(representData(nodeKey.value), representData(propertyValue))
}


println("node key = $nodeKey")
val nodeValue = representData(propertyValue)


if (!hasAlias && javaBean == null || property == null)
return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag)
val nodeType = nodeValue.nodeType
if (property.type != Enum::class.java && propertyValue is Enum<*>) nodeValue.tag = Tag.STR
else if (nodeType == NodeType.MAPPING) {
if (propertyValue != null && property.type == propertyValue::class.java) {
if (propertyValue !is Map<*, *>) {
if (nodeValue.tag != Tag.SET) {
nodeValue.tag = Tag.MAP
}
}
}
}
return NodeTuple(representData(nodeKey.value), representData(propertyValue))
} catch (e: Exception) {
println(e.localizedMessage)
throw e
}
}

override fun findRepresenterFor(data: Any?): Optional<RepresentToNode> {
objectToRepresent = data
if (data is DelegatingNode)
{
val nodeKeyValuePairs = RepresentDelegatingNode().representDelegationNode(data)
}
return super.findRepresenterFor(data)
}

override fun getTag(clazz: Class<*>?, defaultTag: Tag?): Tag? {
return if (classTags.containsKey(clazz)) {
classTags[clazz]
} else {
defaultTag
}
}


override fun representMapping(tag: Tag?, mapping: MutableMap<*, *>?, fs: FlowStyle?): SnakeYamlNode {
val flowStyle = dumpSettings.defaultFlowStyle
return super.representMapping(tag, mapping, flowStyle)
}


fun representDelegatedPropertyNode(properties: Set<Property>, data: Any): SnakeYamlNode? {

val value: MutableList<NodeTuple> = ArrayList<NodeTuple>(properties.size)
val tag = MAP
val customTag = classTags[data::class.java]

val node = MappingNode(tag, value, FlowStyle.AUTO)
representedObjects[data] = node

for (kProp in properties) {
// Returns the instance of a delegated extension property,
// or null if this property is not delegated.
val memberValue = kProp.getExtensionDelegate() ?: return nullRepresenter.representData(data)

val memberTag = classTags[memberValue::class.java]

val memberProperty: NodeTuple = representDelegatedProperty(data, kProp, memberValue, memberTag)

val nodeValue: SnakeYamlNode = memberProperty.valueNode
value.add(memberProperty)
}

return node;
}


fun representDelegatingNode(node: DelegatingNode): SnakeYamlNode {

val propertyMap = node.properties.map { p ->
val key = representData(p.value) as ScalarNode
val value = representData(p.value)
classTags.containsKey(p.value::class.java)
NodeTuple(key, value)
}
.map { nodeTuple: NodeTuple -> Pair(nodeTuple.keyNode, nodeTuple.valueNode) }
.toMap()
.toMutableMap()

return representMapping(Tag.MAP, propertyMap, dumpSettings.defaultFlowStyle)
}


fun representDelegatedProperty(data: Any, kProp: KProperty1<out Any, *>, memberValue: Any?, memberTag: Tag?): NodeTuple {

val nodeKey = representData(kProp.name) as ScalarNode
val hasAlias = this@MockServerRepresenter.representedObjects.containsKey(memberValue)
val nodeValue = representData(memberValue)

if (memberValue != null && !hasAlias) when (nodeValue?.nodeType) {

NodeType.SCALAR -> {
//generic Enum requires the full tag
if (kProp.returnType !== Enum::class.java) {
if (memberValue is Enum<*>) {
nodeValue.tag = Tag.STR
}
}
}
NodeType.MAPPING -> {
if (kProp.returnType === memberValue::class) {
if (memberValue !is Map<*, *>) {
if (nodeValue.tag != Tag.SET) {
nodeValue.tag = Tag.MAP
}
}
}
}
NodeType.SEQUENCE -> TODO()
NodeType.ANCHOR -> TODO()
null -> TODO()
}

return NodeTuple(nodeKey, nodeValue)
}


 *
 * This returns the ordering of properties that by convention should appear at the beginning of
 * a Yaml object in Kubernetes.


open fun getPropertyPosition(property: String): Int {
return when (property) {
"apiVersion" -> 0
"kind" -> 1
"metadata" -> 2
"spec" -> 3
"type" -> 4
else -> Int.MAX_VALUE
}
}

public inner class RepresentDelegatingNode : RepresentToNode {

public fun representDelegationNode(node: DelegatingNode): SnakeYamlNode = representData(node)

override fun representData(data: Any?): SnakeYamlNode {
if (data !is DelegatingNode) return nullRepresenter.representData(data)
return this@MockServerRepresenter.representDelegatingNode(data)
}
}

public inner class RepresentIntOrString : RepresentToNode {
override fun representData(data: Any): SnakeYamlNode {
val intOrString: IntOrString = data as IntOrString
return if (intOrString.isInteger) {
this@MockServerRepresenter.representData(intOrString.integerValue)
} else {
this@MockServerRepresenter.representData(intOrString.stringValue)
}
}
}

public inner class RepresentDelegatedProperties : RepresentToNode {

override fun representData(data: Any?): SnakeYamlNode? {
if (data == null) return nullRepresenter.representData(data)
return this@MockServerRepresenter.representDelegatedPropertyNode(data::class.java.kotlin.declaredMemberProperties.toMutableSet(), data)
}
}


public inner class RepresentYamlLogDocument : RepresentToNode {
override fun representData(data: Any?): SnakeYamlNode? {
if (data == null) return nullRepresenter.representData(data)
return this@MockServerRepresenter.representDelegatingNode(data as YamlLogDocument)
}
}


}
 */

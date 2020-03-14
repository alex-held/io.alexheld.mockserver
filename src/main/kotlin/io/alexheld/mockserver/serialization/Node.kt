package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*
import org.gradle.internal.impldep.org.eclipse.jgit.errors.*

@Suppress("UNCHECKED_CAST")
@JsonSerialize(using = NodeSerializer::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder("kind", "event", "timestamp", "id")
open class Node(@JsonIgnore val properties: MutableMap<String, Any> = mutableMapOf()) {

    companion object {
        const val ScalarValueKey: String = "<<SCALAR_VALUE>>"
    }

    @JsonAnyGetter
    fun any(): Map<String, Any> = properties

    @JsonIgnore
    fun isAtomic(): Boolean = properties.containsKey(ScalarValueKey)

    @JsonIgnore
    fun <T : Any> getAtomicValue(): T = properties.getValue(ScalarValueKey) as T

    @JsonAnySetter
    open fun anySetter(key: String, value: Any) {
        properties[key] = value
    }
}


fun <TNode : Node> TNode.toMap(): MutableMap<String, Any> {
    val map = mutableMapOf<String, Any>()

    for (entry in properties) {
        if (entry.value !is Node)
            map[entry.key] = entry.value
        else if (entry.value is Node && (entry.value as Node).isAtomic())
            map[entry.key] = (entry.value as Node).getAtomicValue()
        else {
            val innerProperties = (entry.value as Node).toMap()
            map[entry.key] = innerProperties
        }
    }
    return map
}


@JsonIgnore
fun <TNode : Node> TNode.withNamed(key: String, node: Node): TNode {

    if (properties[key] == null) {
        properties[key] = mutableListOf(node)
        return this
    }

    val named = properties[key] as? MutableList<*>
    if (named != null) {
        properties[key] = named.plus(node)
        return this
    } else throw NotSupportedException("Cannot add node to named list: name=$key, list=${properties[key]}")
}

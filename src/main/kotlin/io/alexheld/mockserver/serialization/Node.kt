package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*

@JsonSerialize(using = NodeSerializer::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder("kind", "event", "timestamp", "id")
public open class Node(@JsonIgnore val properties: MutableMap<String, Any> = mutableMapOf()) {

    companion object {
        public const val ScalarValueKey: String = "<<SCALAR_VALUE>>"
    }

    @JsonAnyGetter
    fun any(): Map<String, Any> = properties

    @JsonIgnore
    fun isAtomic(): Boolean = properties.containsKey(ScalarValueKey)

    @JsonIgnore
    fun<T: Any> getAtomicValue(): T = properties.getValue(ScalarValueKey) as T

    @JsonAnySetter
    open fun anySetter(key: String, value: Any) {
        properties[key] = value
    }
}

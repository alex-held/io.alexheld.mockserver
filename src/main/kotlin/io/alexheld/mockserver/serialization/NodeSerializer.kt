package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*

class NodeSerializer : JsonSerializer<DelegatingNode>() {

    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value Value to serialize; can **not** be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     * serializing Objects value contains, if any.
     */
    override fun serialize(value: DelegatingNode?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value == null) return

        if (value.isAtomic()) {
            val atomicValue = value.getAtomicValue<Any>()
            serializers?.defaultSerializeValue(atomicValue, gen)
        } else {
            val anyValues = value.properties
            val sorted = LinkedHashMap<String, Any>(anyValues.size)
            if (anyValues.containsKey("apiVersion"))
                sorted["apiVersion"] = anyValues.remove("apiVersion")!!
            if (anyValues.containsKey("kind"))
                sorted["kind"] = anyValues.remove("kind")!!
            if (anyValues.containsKey("event"))
                sorted["event"] = anyValues.remove("event")!!
            if (anyValues.containsKey("id"))
                sorted["id"] = anyValues.remove("id")!!
            if (anyValues.containsKey("timestamp"))
                sorted["timestamp"] = anyValues.remove("timestamp")!!
            anyValues.forEach {
                sorted[it.key] = it.value
            }
            serializers?.defaultSerializeValue(sorted, gen)
        }
    }
}

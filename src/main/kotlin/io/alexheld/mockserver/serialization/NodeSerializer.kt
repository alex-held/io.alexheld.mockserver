package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*

public class NodeSerializer: JsonSerializer<Node>() {

    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value Value to serialize; can **not** be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     * serializing Objects value contains, if any.
     */
    override fun serialize(value: Node?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value == null) return

        if (value.isAtomic()) {
            val atomicValue = value.getAtomicValue<Any>()
            serializers?.defaultSerializeValue(atomicValue, gen)
        }
        else {
            val anyValues = value.any()
            serializers?.defaultSerializeValue(anyValues, gen)
        }
    }
}

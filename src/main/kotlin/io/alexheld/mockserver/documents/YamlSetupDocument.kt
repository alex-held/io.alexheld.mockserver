package io.alexheld.mockserver.documents

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.annotation.*
import com.fasterxml.jackson.dataformat.yaml.*
import io.alexheld.mockserver.serialization.*


@JsonSerialize(using = DocumentSerializer::class)
class Document(@JsonIgnore val values: MutableList<Log> = mutableListOf())


class YamlSetupDocument(val documents: MutableList<YamlDocument> = mutableListOf())


class DocumentSerializer : JsonSerializer<Document>() {

    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value Value to serialize; can **not** be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     * serializing Objects value contains, if any.
     */
    override fun serialize(document: Document?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (document == null) throw UnsupportedOperationException("Document cannot be <null>!")

        val y = gen as YAMLGenerator
        val mapper = YAMLFormatter.getMapper()


        val singleWriter = mapper.writer()
        val sequenceWriter = singleWriter.writeValues(gen)

        try {
            for (value in document.values) {
                sequenceWriter.write(value)
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        }

    }

}

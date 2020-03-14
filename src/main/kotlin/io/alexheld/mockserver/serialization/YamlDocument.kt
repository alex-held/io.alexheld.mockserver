package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.annotation.*
import io.alexheld.mockserver.domain.models.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName("YAML")
open class YamlDocument(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()) {

    // @YamlTagAnnotation("apiVersion")
    var apiVersion: String by properties

    // @YamlTagAnnotation("kind")
    var kind: String by properties
}


class ApiVersionSerializer : JsonSerializer<ApiVersion>() {
    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value Value to serialize; can **not** be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     * serializing Objects value contains, if any.
     */
    override fun serialize(value: ApiVersion?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.version)
    }

}


@JsonSerialize(using = ApiVersionSerializer::class)
class ApiVersion(@JsonValue var version: String) {

    companion object {
        val latest: ApiVersion = ApiVersion("1.0")
        val V1_0: ApiVersion = ApiVersion("1.0")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApiVersion

        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        return version.hashCode()
    }


}


class YamlLogDocument(map: Map<String, Any> = mapOf()) : YamlDocument(map) {

    //@YamlTagAnnotation("id")
    var id: String by properties

    // @YamlTagAnnotation("timestamp")
    var timestamp: String by properties

    @JsonCreator
    constructor(
        apiVersion: String,
        id: String,
        timestamp: String,
        map: Map<String, Any> = mapOf()
    ) : this(map) {
        this.apiVersion = apiVersion
        this.id = id
        this.timestamp = timestamp
        this.kind = "Log"
    }

    constructor() : this("1.0", Generator.getId(), Generator.getTimestampString())

    companion object {

        fun match(incomingRequest: Request, setup: Setup): YamlLogDocument {
            return YamlLogDocument()
                .withNamed("events", Log.requestReceived(incomingRequest))
                .withNamed("events", Log.requestMatched(setup))
                .withNamed("events", Log.action(setup.action))
        }

    }
}

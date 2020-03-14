package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.domain.models.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName("YAML")
open class YamlDocument(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()) {

    var apiVersion: ApiVersion by properties
    var kind: String by properties
}


class ApiVersion {

    @JsonValue
    var version: String = "1.0.0"

    @JsonCreator
    constructor(version: String) {
        this.version = version
    }

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

    var id: String by properties
    var timestamp: String by properties
    var events: MutableList<Log> by properties

    @JsonCreator
    constructor(
        apiVersion: String,
        id: String,
        timestamp: String,
        map: Map<String, Any> = mapOf()
    ) : this(ApiVersion(apiVersion), id, timestamp, map)

    constructor(
        apiVersion: ApiVersion,
        id: String,
        timestamp: String,
        map: Map<String, Any> = mapOf()
    ) : this(map) {
        this.apiVersion = apiVersion
        this.id = id
        this.timestamp = timestamp
        this.kind = "Log"
    }

    constructor() : this(ApiVersion.latest, Generator.getId(), Generator.getTimestampString())
    constructor(apiVersion: ApiVersion) : this(apiVersion.version, Generator.getId(), Generator.getTimestampString())


    companion object {

        fun match(incomingRequest: Request, setup: Setup): YamlLogDocument {
            return YamlLogDocument()
                .withNamed("events", Log.requestReceived(incomingRequest))
                .withNamed("events", Log.requestMatched(setup))
                .withNamed("events", Log.action(setup.action))
        }

    }

}

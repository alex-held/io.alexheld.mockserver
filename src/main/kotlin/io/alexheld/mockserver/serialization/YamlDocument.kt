package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName("YAML")
open class YamlDocument(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()) {

    var apiVersion: String by properties
    var kind: String by properties
    var id: String by properties
    var timestamp: String by properties
}

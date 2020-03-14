package io.alexheld.mockserver.serialization

class LogNode(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()) {
    var event: String by properties
    var id: String by properties
    var timestamp: String by properties
}



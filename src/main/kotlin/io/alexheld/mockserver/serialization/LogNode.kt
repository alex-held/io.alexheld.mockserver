package io.alexheld.mockserver.serialization

class LogNode(map: MutableMap<String, Any> = mutableMapOf()) : DelegatingNode(map) {
    var event: String by properties
    var id: String by properties
    var timestamp: String by properties
}



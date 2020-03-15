package io.alexheld.mockserver.serialization

class LogNode(map: LinkedHashMap<String, Any> = linkedMapOf()) : DelegatingNode(map) {
    var event: String by properties
    var id: String by properties
    var timestamp: String by properties
}



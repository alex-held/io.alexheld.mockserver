package io.alexheld.mockserver.serialization


public class LogNode(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()) {
    var type: String by properties
    var id: String by properties
    var timestamp: String by properties
}


class YamlLog(map: Map<String, Any> = mapOf()) : YamlDocument(map){
    init { kind = "Log" }
    var type: String by properties
    var events: MutableList<Node> by properties
}

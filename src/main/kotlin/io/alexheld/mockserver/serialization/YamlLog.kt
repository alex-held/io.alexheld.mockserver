package io.alexheld.mockserver.serialization

import io.alexheld.mockserver.logging.*
import java.time.*
import java.util.*


public class LogNode(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()) {
    var event: String by properties
    var id: String by properties
    var timestamp: String by properties
}

fun Log.toYamlLog(event: String, selector: (Log) -> Any): YamlLog{
    val yaml = YamlLog(this.properties)
    yaml.properties[event] = selector(this)
    return yaml
}


class YamlLog(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()){

    constructor(type: LogMessageType, properties: Map<String, Any> = mapOf()): this(properties) {
        id = UUID.randomUUID().toString()
        event = type
        timestamp = Instant.now().toString()
    }


    var id: String by properties
    var timestamp: String by properties
    var event: LogMessageType by properties
    var events: MutableList<Node> by properties

    companion object {
        fun setupCreated(log: Log): YamlLog {
            val yaml = createYamlLog(log)
            yaml.withEvent(log.toYamlLog("setup") { s -> s.setup})
            return yaml
        }

        fun setupDeleted(log: Log): YamlLog {
            val yaml = createYamlLog(log)
            yaml.withEvent(log.toYamlLog("setup") { s -> s.setup})
            return yaml
        }

        fun requestMatched(log: Log): YamlLog{
            val yaml = createYamlLog(log)
            yaml.withEvent(log.toYamlLog("request"){l -> l.request})
            yaml.properties["setup"] = log.setup
            return yaml
        }

        fun requestReceived(log: Log): YamlLog{
            val yaml = createYamlLog(log)
            yaml.properties["request"] = log.request
            return yaml
        }

        fun actionResponse(log: Log): YamlLog{
            val yaml = createYamlLog(log)
            yaml.properties["action"] = log.setup.action
            return yaml
        }

        private fun createYamlLog(log: Log): YamlLog{
            val yaml = YamlLog()
            yaml.event = log.event
            yaml.id = log.id
            yaml.timestamp= log.timestamp.toString()
            return yaml
        }
    }

    fun withEvent(yamlLog: YamlLog): YamlLog  = withEvent(yamlLog.properties)
    fun withEvent(properties: Map<String, Any>): YamlLog{
        val log = YamlLog(properties)
        events.add(log)
        return this
    }
}

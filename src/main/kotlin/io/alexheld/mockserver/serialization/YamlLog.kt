package io.alexheld.mockserver.serialization

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import java.time.*
import java.util.*


class YamlLog(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()) {

    constructor(type: LogMessageType, properties: Map<String, Any> = mapOf()) : this(properties) {
        id = UUID.randomUUID().toString()
        event = type
        timestamp = Instant.now().toString()
    }


    var id: String by properties
    var timestamp: String by properties
    var event: LogMessageType by properties
    var events: MutableList<Node> by properties

    companion object {

        fun setupCreated(setup: Setup): YamlLog = YamlLog(
            LogMessageType.Setup_Created, mapOf(
                "setup" to setup
            )
        )

        fun setupDeleted(setup: Setup): YamlLog = YamlLog(
            LogMessageType.Setup_Deleted, mapOf(
                "setup" to setup
            )
        )

        fun setupDeletionFailed(): YamlLog = YamlLog(LogMessageType.Setup_Deletion_Failed)

        fun requestReceived(request: Request): YamlLog = YamlLog(
            LogMessageType.Request_Received, mapOf(
                "request" to request
            )
        )

        fun matched(request: Request, setup: Setup): YamlLog = YamlLog(
            LogMessageType.Request_Matched, mapOf(
                "events" to listOf(
                    YamlLog(
                        LogMessageType.Request_Received, mapOf(
                            "request" to request
                        )
                    ),
                    YamlLog(
                        LogMessageType.Request_Matched, mapOf(
                            "setup" to setup
                        )
                    )
                )
            )
        )


        fun listSetups(): YamlLog = YamlLog(
            LogMessageType.Operation, mapOf(
                "operation" to "list",
                "kind" to "Setup"
            )
        )

        fun listLogs(): YamlLog = YamlLog(
            LogMessageType.Operation, mapOf(
                "operation" to "list",
                "kind" to "Log"
            )
        )
    }


    fun withEvent(yamlLog: YamlLog): YamlLog = withEvent(yamlLog.properties)
    fun withEvent(properties: Map<String, Any>): YamlLog {
        val log = YamlLog(properties)
        events.add(log)
        return this
    }
}

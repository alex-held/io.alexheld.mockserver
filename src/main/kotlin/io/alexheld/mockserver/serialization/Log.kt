package io.alexheld.mockserver.serialization

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import java.time.*
import java.util.*


object Generator {

    var enableDebugGeneration: Boolean = false
    var defaultId: String = "00000000-0000-0000-0000-000000000000"
    var defaultTimestamp: Instant = Instant.EPOCH


    fun getId(): String {
        if (enableDebugGeneration)
            return defaultId
        return UUID.randomUUID().toString()
    }

    fun getTimestamp(): Instant {
        if (enableDebugGeneration)
            return defaultTimestamp
        return Instant.now()
    }

    fun getTimestampString(): String = getTimestamp().toString()

}


class Log(map: Map<String, Any> = mapOf()) : Node(map.toMutableMap()) {

    constructor(type: LogMessageType, properties: Map<String, Any> = mapOf()) : this(properties) {
        id = Generator.getId()
        timestamp = Generator.getTimestampString()
        event = type
    }


    var id: String by properties
    var timestamp: String by properties
    var event: LogMessageType by properties
    var events: MutableList<Node> by properties

    companion object {

        fun setupCreated(setup: Setup): Log = Log(
            LogMessageType.Setup_Created, mapOf(
                "setup" to setup
            )
        )

        fun setupDeleted(setup: Setup): Log = Log(
            LogMessageType.Setup_Deleted, mapOf(
                "setup" to setup
            )
        )

        fun setupDeletionFailed(): Log = Log(LogMessageType.Setup_Deletion_Failed)

        fun requestReceived(request: Request): Log = Log(
            LogMessageType.Request_Received, mapOf(
                "request" to request
            )
        )

        fun requestMatched(setup: Setup): Log = Log(
            LogMessageType.Request_Matched, mapOf(
                "setup" to setup
            )
        )


        fun listSetups(): Log = Log(
            LogMessageType.Operation, mapOf(
                "operation" to "list",
                "kind" to "Setup"
            )
        )

        fun listLogs(): Log = Log(
            LogMessageType.Operation, mapOf(
                "operation" to "list",
                "kind" to "Log"
            )
        )

        fun action(action: Action): Log = Log(
            LogMessageType.Action_Response, mapOf(
                "action" to action
            )
        )
    }


    fun withEvent(yamlLog: Log): Log = withEvent(yamlLog.properties)
    fun withEvent(properties: Map<String, Any>): Log {
        val log = Log(properties)
        events.add(log)
        return this
    }
}

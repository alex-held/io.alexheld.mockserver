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


class Log(map: MutableMap<String, Any> = mutableMapOf()) : DelegatingNode(map) {

    constructor(type: LogMessageType, properties: MutableMap<String, Any> = mutableMapOf()) : this(properties) {
        id = Generator.getId()
        timestamp = Generator.getTimestampString()
        event = type
    }


    var id: String by properties
    var timestamp: String by properties
    var event: LogMessageType by properties
    var events: LinkedList<DelegatingNode> by properties

    companion object {

        fun setupCreated(setup: Setup): Log = Log(
            LogMessageType.Setup_Created, mutableMapOf(
                "setup" to setup
            )
        )

        fun setupDeleted(setup: Setup): Log = Log(
            LogMessageType.Setup_Deleted, mutableMapOf(
                "setup" to setup
            )
        )

        fun setupDeletionFailed(): Log = Log(LogMessageType.Setup_Deletion_Failed)

        fun requestReceived(request: Request): Log = Log(
            LogMessageType.Request_Received, mutableMapOf(
                "request" to request
            )
        )

        fun requestMatched(setup: Setup): Log = Log(
            LogMessageType.Request_Matched, mutableMapOf(
                "setup" to setup
            )
        )


        fun listSetups(): Log = Log(
            LogMessageType.Operation, mutableMapOf(
                "operation" to "list",
                "kind" to "Setup"
            )
        )

        fun listLogs(): Log = Log(
            LogMessageType.Operation, mutableMapOf(
                "operation" to "list",
                "kind" to "Log"
            )
        )


        // fun operation(details: MutableMap<String, Any>) = Log(LogMessageType.Operation, details)

        fun action(action: Action): Log = Log(
            LogMessageType.Action_Response, mutableMapOf(
                "action" to action
            )
        )
    }


    fun withEvent(yamlLog: Log): Log = withEvent(yamlLog.properties)
    fun withEvent(properties: MutableMap<String, Any>): Log {
        val log = Log(properties)
        events.add(log)
        return this
    }
}


fun <TKey, TValue, THashMap : MutableMap<TKey, TValue>> THashMap.appendWith(other: Map<TKey, TValue>): THashMap {
    other.forEach {
        this.put(it.key, it.value)
    }
    return this
}

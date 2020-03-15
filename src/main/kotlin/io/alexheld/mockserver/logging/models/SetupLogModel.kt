package io.alexheld.mockserver.logging.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.serialization.*
import java.time.*

class SetupCreated(@JsonIgnore val map: MutableMap<String, Any> = mutableMapOf()) : ILog {
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
    val setup: Setup by map
}

class SetupDeleted(@JsonIgnore val map: MutableMap<String, Any> = mutableMapOf()) : ILog {
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
    val setup: Setup by map
}

class SetupDeletionFailed(@JsonIgnore val map: MutableMap<String, Any> = mutableMapOf()) : ILog {
    override var event: LogMessageType by map
    override var id: kotlin.String by map
    override var timestamp: String by map
}


class RequestMatched(map: MutableMap<String, Any> = mutableMapOf()) : DelegatingNode(map), ILog {
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
    var events: MutableList<ILog> by map
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class RequestReceived(@JsonIgnore val map: MutableMap<String, Any> = mutableMapOf()) : ILog {

    constructor(requestReceived: Request) : this(mutableMapOf()) {
        this.request = requestReceived
        this.id = "1334"
        this.timestamp = Instant.EPOCH.toString()
    }

    var request: Request by map
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
}


data class Operation(@JsonIgnore val map: MutableMap<String, Any> = mutableMapOf()) : ILog {
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
}


interface Identifyable {

    var id: String
    var timestamp: String
}

interface ILog : Identifyable {
    var event: LogMessageType
}

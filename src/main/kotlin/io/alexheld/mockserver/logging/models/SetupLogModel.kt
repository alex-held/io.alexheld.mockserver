package io.alexheld.mockserver.logging.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.serialization.*
import org.gradle.kotlin.dsl.*
import java.time.*

class SetupCreated(@JsonIgnore override var map: LinkedHashMap<String, Any> = linkedMapOf()) : ILog {
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
    val setup: Setup by map
}

class SetupDeleted(@JsonIgnore override var map: LinkedHashMap<String, Any> = linkedMapOf()) : ILog {
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
    val setup: Setup by map
}

class SetupDeletionFailed(@JsonIgnore override var map: LinkedHashMap<String, Any> = linkedMapOf()) : ILog {
    override var event: LogMessageType by map
    override var id: kotlin.String by map
    override var timestamp: String by map
}


class RequestMatched(@JsonIgnore override var map: LinkedHashMap<String, Any> = linkedMapOf()) : DelegatingNode(map), ILog {
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
    var events: MutableList<ILog> by map
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class RequestReceived(@JsonIgnore override var map: LinkedHashMap<String, Any> = linkedMapOf()) : ILog {

    constructor(requestReceived: Request) : this(linkedMapOf()) {
        this.request = requestReceived
        this.id = "1334"
        this.timestamp = Instant.EPOCH.toString()
    }

    var request: Request by map
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
}


data class Operation(@JsonIgnore override var map: LinkedHashMap<String, Any> = linkedMapOf()) : ILog {
    override var event: LogMessageType by map
    override var id: String by map
    override var timestamp: String by map
}


interface Identifyable {

    var id: String
    var timestamp: String
}

interface ILog : Identifyable {

    @get:JsonIgnore
    var map: LinkedHashMap<String, Any>
    var event: LogMessageType
}

package io.alexheld.mockserver.logging.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.logging.*


interface Identifyable {
    @get:JsonIgnore
    var id: String

    @get:JsonIgnore
    var timestamp: String
}

interface ILog : Identifyable {

    @get:JsonIgnore
    var map: MutableMap<String, Any>

    @get:JsonIgnore
    var event: LogMessageType
}

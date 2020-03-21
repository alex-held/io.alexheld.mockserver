package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import java.time.*

class RequestReceivedLog: LogDTO {

    var requestReceived: Request by content

    constructor(id: String, timestamp: Instant, content: MutableMap<String, Any?> = mutableMapOf()) : super(id, timestamp, LogMessageType.Request_Received, content)
    constructor(content: MutableMap<String, Any?> = mutableMapOf()) : super(content)
    constructor(id: String, timestamp: Instant, requestReceived: Request) : this(id, timestamp, mutableMapOf(
            "events" to mutableListOf(Pair("requestReceived", requestReceived))
        )
    )
}

package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import java.time.*

class RequestMatchedLog : LogDTO {

    var setup: Setup by content
    var requestReceived: Request? by content
    var action: Action by content

    constructor(content: MutableMap<String, Any?> = mutableMapOf()) : super(content)

    constructor(id: String, timestamp: Instant, content: MutableMap<String, Any?> = mutableMapOf()) : super(id, timestamp, LogMessageType.Request_Matched, content)

    constructor(id: String, timestamp: Instant, request: Request, setup: Setup) : this(id, timestamp, mutableMapOf(
            "requestReceived" to request,
            "setup" to setup,
            "action" to (setup.action )
        )
    )
}

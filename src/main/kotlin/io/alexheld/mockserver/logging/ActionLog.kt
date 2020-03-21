package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import java.time.*

class ActionLog : LogDTO {

    var action: Setup by content

    constructor(content: MutableMap<String, Any?> = mutableMapOf()) : super(content)
    constructor(id: String, timestamp: Instant, content: MutableMap<String, Any?> = mutableMapOf()) : super(id, timestamp, LogMessageType.Action_Response, content)
    constructor(id: String, timestamp: Instant, action: Action) : this(id, timestamp, mutableMapOf(
        "action" to action
    )
    )
}

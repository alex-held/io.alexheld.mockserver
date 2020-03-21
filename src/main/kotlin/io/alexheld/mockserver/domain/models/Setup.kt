package io.alexheld.mockserver.domain.models

import io.alexheld.mockserver.logging.*
import java.time.*


class Setup(content: MutableMap<String, Any?> = mutableMapOf()) : ContentMapBase(content) {

    var id: String by content
    var timestamp: Instant by content
    val request: Request by content
    val action: Action by content

    constructor(id: Int, time: Instant, request: Request, action: Action) : this(mutableMapOf(
        "id" to id.toString(),
        "timestamp" to time,
        "request" to request,
        "action" to action
    ))
}


class Action(content: MutableMap<String, Any?> = mutableMapOf()) : ContentMapBase(content) {

    var message: String? by content
    var statusCode: Int by content

    constructor(message: String?, statusCode: Int = 404) : this(mutableMapOf<String, Any?>(
        "message" to message,
        "statusCode" to statusCode
    ))
}

package io.alexheld.mockserver.logging

import java.time.*

class SetupDeletionFailedLog : LogDTO {
    constructor(content: MutableMap<String, Any?> = mutableMapOf()) : super(content)
    constructor(id: String, timestamp: Instant, content: MutableMap<String, Any?> = mutableMapOf()) : super(id, timestamp,
        LogMessageType.Setup_Deletion_Failed, content)
}

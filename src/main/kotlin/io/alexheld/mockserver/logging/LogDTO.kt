package io.alexheld.mockserver.logging

import WithLogMetadata
import io.alexheld.mockserver.serialization.*
import java.time.*


open class LogDTO(override val content: MutableMap<String, Any?> = mutableMapOf()) : ContentMapBase(content), WithLogMetadata {

  constructor(id: String, timestamp: Instant, type: LogMessageType, properties: Map<String, Any?> = mutableMapOf()) : this(
        mutableMapOf<String, Any?>(
            "id" to id,
            "timestamp" to timestamp,
            "type" to type.name
        ).appendWith(properties)
    )

}

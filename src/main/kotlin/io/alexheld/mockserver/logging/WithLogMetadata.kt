package io.alexheld.mockserver.logging

import java.time.*


/**
 * Adds minimal metadata to a [IdentifiableLog]
 */
interface WithLogMetadata {
    var id: String
    var timestamp: Instant
    var type: LogMessageType
}

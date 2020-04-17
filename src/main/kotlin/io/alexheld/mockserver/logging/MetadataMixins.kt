package io.alexheld.mockserver.logging

import java.time.*

interface MetadataMixin

/**
 * Adds an [ApiCategory] to a [IdentifiableLog]
 */
interface WithCategory : MetadataMixin {
    val apiCategory: ApiCategory
}


/**
 * Adds minimal metadata to a [IdentifiableLog]
 */
interface WithLogMetadata : MetadataMixin {
    var id: String
    var timestamp: Instant
    var type: LogMessageType
}

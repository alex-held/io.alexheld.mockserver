package io.alexheld.mockserver.logging

import java.time.*

interface MetadataMixin

interface WithCategory : MetadataMixin {
    val apiCategory: ApiCategory
}


/**
 * Adds minimal metadata to a [IdentifiableLog]
 */
interface WithLogMetadata : MetadataMixin{
    var id: String
    var timestamp: Instant
    var type: LogMessageType
}

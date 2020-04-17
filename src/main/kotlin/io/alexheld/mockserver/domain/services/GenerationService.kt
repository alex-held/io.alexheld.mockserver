package io.alexheld.mockserver.domain.services

import java.time.*

interface GenerationService {
    fun getId(): String
    fun getTimestamp(): Instant
    fun getTimestampString(): String = getTimestamp().toString()
}

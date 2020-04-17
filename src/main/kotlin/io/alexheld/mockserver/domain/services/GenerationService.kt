package io.alexheld.mockserver.domain.services

import java.time.*

interface GenerationService {

    fun getId(): String
    fun getTimestamp(): Instant
    fun getTimestampString(): String = Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).toString()
}

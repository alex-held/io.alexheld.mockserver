package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.serialization.*

interface LogRepository {
    fun list(): List<Log>
    fun delete(id: String): Log?
    fun add(log: Log): Log
}

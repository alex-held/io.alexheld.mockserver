package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.logging.*

interface LogRepository {
    fun list(): List<Log>
    fun delete(id: LogId): Log?
    fun add(log: Log): Log
}

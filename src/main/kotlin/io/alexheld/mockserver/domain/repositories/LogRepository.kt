package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.logging.*

interface LogRepository {
    fun list(): List<Log>
    fun delete(from: LogId): Log?
    fun add(log: Log): Log
}

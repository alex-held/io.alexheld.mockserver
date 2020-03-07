package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.logging.*

interface LogService {

    fun list(): List<Log>
    fun delete(id: String): Log?
    fun add(log: Log): Log
}

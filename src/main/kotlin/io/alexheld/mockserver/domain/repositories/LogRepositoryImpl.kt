package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.logging.*

class LogRepositoryImpl() : LogRepository {

    private val logs: MutableMap<LogId, Log> = mutableMapOf()
    override fun list(): List<Log> = logs.values.toList()
    override fun delete(id: LogId): Log? = logs.remove(id)

    override fun add(log: Log): Log {
        logs[log.id] = log
        return log
    }

}

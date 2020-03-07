package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*

class LogServiceImpl(private val logRepository: LogRepository) : LogService {

    override fun list(): List<Log>  = logRepository.list()
    override fun delete(id: String): Log?  = logRepository.delete(LogId.From(id))
    override fun add(log: Log) = logRepository.add(log)

}


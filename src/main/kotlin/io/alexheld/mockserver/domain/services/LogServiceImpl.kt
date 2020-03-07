package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*
import org.apache.logging.log4j.kotlin.*

class LogServiceImpl(private val logRepository: LogRepository) : LogService {

    override fun list(): List<Log> = logRepository.list()
    override fun delete(id: String): Log? = logRepository.delete(LogId.From(id))
    override fun add(log: Log) = logRepository.add(log)

    fun tryLog(log: Log): String? {
        if (MockConfig.LOG_LEVEL <= log.level)
            return null
        return log.writeLogMessage()
    }
}


public fun Log.writeLogMessage(): String {
   val formatted = format()
    logger().log(level, formatted)
    return formatted
}

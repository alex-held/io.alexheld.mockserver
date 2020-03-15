package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.serialization.*
import org.apache.logging.log4j.*

class LogServiceImpl(private val logRepository: LogRepository) : LogService {

    override fun list(): List<Log> = logRepository.list()
    override fun delete(id: String): Log? = logRepository.delete(id)
    override fun add(log: Log) = logRepository.add(log)

    fun tryLog(log: Log, level: Level = Level.INFO): String? {
        if (MockConfig.LOG_LEVEL <= level)
            return null
        return log.writeLogMessage(level)
    }
}



fun Log.writeLogMessage(level: Level = Level.INFO): String {
    throw NotImplementedError()
    /*val formatted = YAMLFormatter.serialize(this)
    logger().log(level, formatted)
    return formatted*/
}
